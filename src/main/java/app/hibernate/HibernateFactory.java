package app.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateFactory {

    public SessionFactory getSessionFactory() {
        // wczytujemy plik hibernate.cfg.xml (jak przejdziemy ctrl + b na configure to zoabczymy )
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();

        // przekazujemy to aby iec to w postaci obiektu
        MetadataSources sources = new MetadataSources(registry);
        // tworzymy model orm coklwiek to znaczy
        Metadata metadata = sources.getMetadataBuilder().build();
        return metadata.getSessionFactoryBuilder().build();
    }



}

