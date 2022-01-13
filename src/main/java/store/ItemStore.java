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

import java.util.List;
import java.util.function.Function;

/**
 * @author ArvikV
 * @version 1.0
 * @since 07.01.2022
 * public void close() 3. Или реализуйте метод, или удалите
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
    public Item add(Item item) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.save(item);
        session.getTransaction().commit();
        session.close();
        return item;
    }

    @Override
    public User addUser(User user) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
        session.close();
        return user;
    }

    @Override
    public List<Item> findAll() {
        Session session = sf.openSession();
        session.beginTransaction();
        List result = session.createQuery("from Item").list();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    @Override
    public Item findById(int id) {
        Session session = sf.openSession();
        session.beginTransaction();
        Item result = session.get(Item.class, id);
        session.getTransaction().commit();
        session.close();
        return result;
    }

    @Override
    public void done(int id) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.createQuery(
                        "update Item set done = true where id =: id")
                .setParameter("id", id)
                .executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public boolean update(int id, Item item) {
        Session session = sf.openSession();
        session.beginTransaction();
        item.setId(id);
        session.update(item);
        session.getTransaction().commit();
        session.close();
        return true;
    }

    @Override
    public boolean updateItem(Item item) {
        return update(item.getId(), item);
    }

    @Override
    public List<Item> findByDone() {
        Session session = sf.openSession();
        session.beginTransaction();
        List result = session.createQuery("from Item where done = false").list();
        session.getTransaction().commit();
        session.close();
        return result;
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