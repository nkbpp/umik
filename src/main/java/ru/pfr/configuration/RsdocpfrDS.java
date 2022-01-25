/*
package ru.pfr.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "rsdocpfrEntityManager",
        basePackages = "ru.pfr.repo.rsdoc_pfr",
        transactionManagerRef = "rsdocpfrTransactionManager"
)
public class RsdocpfrDS {
    @Bean(name = "rsdocpfrDataSource")
    @ConfigurationProperties(prefix = "rsdocpfr.spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }


    @Bean(name = "rsdocpfrEntityManager")
    public LocalContainerEntityManagerFactoryBean
    rsdoc_pfrEntityManagerFactory(
            @Qualifier("rsdocpfrDataSource") DataSource dataSource,
            EntityManagerFactoryBuilder builder
    ) {
        return
                builder
                        .dataSource(dataSource)
                        .packages("ru.pfr.model.rsdoc_pfr")//
                        .persistenceUnit("rsdoc_pfr")//
                        .build();//
    }


    @Bean(name = "rsdocpfrTransactionManager")
    public PlatformTransactionManager rsdoc_pfrTransactionManager(
            @Qualifier("rsdocpfrEntityManager") EntityManagerFactory
                    rsdocpfrEntityManagerFactory
    ) {
        return new JpaTransactionManager(rsdocpfrEntityManagerFactory);
    }
}
*/
