package toone;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

/**
 * @author ArvikV
 * @version 1.0
 * @since 09.01.2022
 */
public class HbmRun {
        public static void main(String[] args) {
            final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .configure().build();
            try {
                SessionFactory sf = new MetadataSources(registry)
                        .buildMetadata().buildSessionFactory();
                Session session = sf.openSession();
                session.beginTransaction();

                JUser one = JUser.of("Petr");
                JUser two = JUser.of("Andrei");

                Role admin = Role.of("ADMIN");
                admin.getjUsers().add(one);
                admin.getjUsers().add(two);

                session.save(admin);

                session.getTransaction().commit();
                session.close();
            }  catch (Exception e) {
                e.printStackTrace();
            } finally {
                StandardServiceRegistryBuilder.destroy(registry);
            }
        }

        public static <T> T create(T model, SessionFactory sf) {
            Session session = sf.openSession();
            session.beginTransaction();
            session.save(model);
            session.getTransaction().commit();
            session.close();
            return model;
        }

        public static <T> List<T> findAll(Class<T> cl, SessionFactory sf) {
            Session session = sf.openSession();
            session.beginTransaction();
            List<T> list = session.createQuery("from " + cl.getName(), cl).list();
            session.getTransaction().commit();
            session.close();
            return list;
        }
}
