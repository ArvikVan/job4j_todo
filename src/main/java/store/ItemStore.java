package store;

import models.Item;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

/**
 * @author ArvikV
 * @version 1.0
 * @since 07.01.2022
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
        List result = session.createQuery(
                "update Item set done = true where id =: id"
        ).list();
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
}
