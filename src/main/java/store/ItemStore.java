package store;

import models.Item;
import models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

/**
 * @author ArvikV
 * @version 1.0
 * @since 07.01.2022
 * public void close() 3. Или реализуйте метод, или удалите
 * шаблон wrapper применен ко всем методам
 */
public class ItemStore implements Store, AutoCloseable {
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    private static final class Lazy {
        private static final Store INST = new ItemStore();
    }

    private ItemStore() {
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    @Override
    public void close() throws Exception {
        sf.close();
    }

    /**
     * Применен шаблон wrapper и лямбды
     * @param command на входе функциональный интерфейс,
     *                который принимает один аргумент и выдает результат
     * @param <T> параметризированный метод
     * @return на выходе результат и закрытая сессия
     */
    private <T> T tx(final Function<Session, T> command) {
        final Session session = sf.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            T rsl = command.apply(session);
            tx.commit();
            return  rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public Serializable add(Item item) {
        return this.tx(session -> session.save(item));
    }

    @Override
    public Serializable addUser(User user) {
        return this.tx(session -> session.save(user));
    }

    @Override
    public List<Item> findAll() {
        return this.tx(session -> session.createQuery("from Item").list());
    }

    @Override
    public Item findById(int id) {
        return this.tx(session -> session.get(Item.class, id));
    }

    @Override
    public void done(int id) {
        this.tx(session -> session.createQuery(
                "update Item set done = true where id =: id")
                .setParameter("id", id)
                .executeUpdate()
        );
    }

    @Override
    public boolean update(int id, Item item) {
       return this.tx(session -> {
           item.setId(id);
           session.update(item);
           return true;
       });
    }

    @Override
    public boolean updateItem(Item item) {
        return update(item.getId(), item);
    }

    @Override
    public List<Item> findByDone() {
        return this.tx(session -> session.createQuery("from Item where done = false").list());
    }

    @Override
    public User findUserByName(String name) {
        return this.tx(
                session -> {
                    Query query = session.createQuery("from User where name = :name");
                    query.setParameter("name", name);
                    User user = (User) query.uniqueResult();
                    return user;
                }
        );
    }
}