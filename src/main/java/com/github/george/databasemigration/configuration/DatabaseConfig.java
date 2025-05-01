package com.github.george.databasemigration.configuration;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

/**
 * This class defines three DataSources(two target databases in MySQL and one source database in PostgreSQL), three EntityManagerFactories
 * and two transaction managers.
 */
@Configuration
public class DatabaseConfig {


    /**
     * Creates a DataSource for the PostgreSQL database.
     * The connection properties are loaded from application.properties
     * using the prefix "spring.postgresdatasource".
     * Marks this bean as the primary DataSource because multiple DataSource beans are defined.
     * Spring will prefer this bean when injecting a DataSource unless another is explicitly qualified.
     *
     * @return A configured DataSource for PostgreSQL.
     */
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.postgresdatasource")
    public DataSource postgresdatasource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * Creates a DataSource for the MySQL database.
     * The connection properties are loaded from application.properties
     * using the prefix "spring.universitydatasource".
     *
     * @return A configured DataSource for MySQL.
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.universitydatasource")
    public DataSource universitydatasource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * Creates a DataSource for the MySQL database.
     * The connection properties are loaded from application.properties
     * using the prefix "spring.namedatasource".
     *
     * @return A configured DataSource for MySQL.
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.namedatasource")
    public DataSource namedatasource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * Creates an EntityManagerFactory for the PostgreSQL database.
     * This allows the application to manage JPA entities associated with PostgreSQL.
     * Marks this EntityManagerFactory as the primary one used by Spring JPA if no
     * specific qualifier is provided
     *
     * @return An EntityManagerFactory configured for PostgreSQL.
     */
    @Primary
    @Bean(name = "postgresqlEntityManagerFactory")
    public EntityManagerFactory postgresqlEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean lem =
                new LocalContainerEntityManagerFactoryBean();

        lem.setDataSource(postgresdatasource());
        lem.setPackagesToScan("com.github.george.databasemigration.entity.postgresql");
        lem.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        lem.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        lem.afterPropertiesSet();

        return lem.getObject();
    }

    /**
     * Creates an EntityManagerFactory for the MySQL namedb database.
     * This allows the application to manage JPA entities associated with MySQL.
     *
     * @return An EntityManagerFactory configured for MySQL.
     */
    @Bean
    public EntityManagerFactory nameEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean lem =
                new LocalContainerEntityManagerFactoryBean();

        lem.setDataSource(namedatasource());
        lem.setPackagesToScan("com.github.george.databasemigration.entity.mysql");
        lem.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        lem.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        lem.afterPropertiesSet();

        return lem.getObject();
    }

    /**
     * Creates an EntityManagerFactory for the MySQL university database.
     * This allows the application to manage JPA entities associated with MySQL.
     *
     * @return An EntityManagerFactory configured for MySQL.
     */
    @Bean(name = "studentEntityManagerFactory")
    public EntityManagerFactory studentEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean lem =
                new LocalContainerEntityManagerFactoryBean();

        lem.setDataSource(universitydatasource());
        lem.setPackagesToScan("com.github.george.databasemigration.entity.mysql");
        lem.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        lem.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        lem.afterPropertiesSet();

        return lem.getObject();
    }

    /**
     * Configures a transaction manager for the MySQL university database.
     * The transaction manager ensures that database operations
     * are handled in a transactional manner.
     *
     * @return A JpaTransactionManager configured for MySQL.
     */
    @Bean(name = "studentEntityTransactionManager")
    public JpaTransactionManager studentEntityTransactionManager() {
        JpaTransactionManager jpaTransactionManager = new
                JpaTransactionManager();

        jpaTransactionManager.setDataSource(universitydatasource());
        jpaTransactionManager.setEntityManagerFactory(studentEntityManagerFactory());

        return jpaTransactionManager;
    }

    /**
     * Configures a transaction manager for the MySQL namedb database.
     * The transaction manager ensures that database operations
     * are handled in a transactional manner.
     *
     * @return A JpaTransactionManager configured for MySQL.
     */
    @Bean(name = "studentNameTransactionManager")
    public JpaTransactionManager studentNameTransactionManager() {
        JpaTransactionManager jpaTransactionManager = new
                JpaTransactionManager();

        jpaTransactionManager.setDataSource(universitydatasource());
        jpaTransactionManager.setEntityManagerFactory(nameEntityManagerFactory());

        return jpaTransactionManager;
    }
}
