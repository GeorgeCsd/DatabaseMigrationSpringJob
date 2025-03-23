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
 * This class defines two DataSources, two EntityManagerFactories,
 * and a transaction manager.
 */
@Configuration
public class DatabaseConfig {

    /**
     * Creates a DataSource for the MySQL database.
     * The connection properties are loaded from application.properties
     * using the prefix "spring.universitydatasource".
     *
     * @return A configured DataSource for MySQL.
     */
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.universitydatasource")
    public DataSource universitydatasource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * Creates a DataSource for the PostgreSQL database.
     * The connection properties are loaded from application.properties
     * using the prefix "spring.postgresdatasource".
     *
     * @return A configured DataSource for PostgreSQL.
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.postgresdatasource")
    public DataSource postgresdatasource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * Creates an EntityManagerFactory for the PostgreSQL database.
     * This allows the application to manage JPA entities associated with PostgreSQL.
     *
     * @return An EntityManagerFactory configured for PostgreSQL.
     */
    @Bean
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
     * Creates an EntityManagerFactory for the MySQL database.
     * This allows the application to manage JPA entities associated with MySQL.
     *
     * @return An EntityManagerFactory configured for MySQL.
     */
    @Bean
    @Primary
    public EntityManagerFactory mysqlEntityManagerFactory() {
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
     * Configures a transaction manager for the MySQL database.
     * The transaction manager ensures that database operations
     * are handled in a transactional manner.
     *
     * @return A JpaTransactionManager configured for MySQL.
     */
    @Bean
    @Primary
    public JpaTransactionManager jpaTransactionManager() {
        JpaTransactionManager jpaTransactionManager = new
                JpaTransactionManager();

        jpaTransactionManager.setDataSource(universitydatasource());
        jpaTransactionManager.setEntityManagerFactory(mysqlEntityManagerFactory());

        return jpaTransactionManager;
    }
}
