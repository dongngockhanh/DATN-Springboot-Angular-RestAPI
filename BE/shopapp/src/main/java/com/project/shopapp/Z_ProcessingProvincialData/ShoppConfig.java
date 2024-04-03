//package com.project.shopapp.Z_ProcessingProvincialData;
//
//import com.project.shopapp.models.User;
//import com.zaxxer.hikari.HikariDataSource;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import javax.sql.DataSource;
//import java.util.HashMap;
//
//
//@Configuration
//@EnableTransactionManagement
//@EnableJpaRepositories(
//        basePackages = "com.project.shopapp.Repositories",
//        entityManagerFactoryRef = "provincialEntityManagerFactory",
//        transactionManagerRef = "provincialTransactionManager"
//)
//public class ShoppConfig {
//    @Bean
//    @ConfigurationProperties("spring.datasource.shop")
//    public DataSourceProperties shopDataSourceProperties(){
//        return new DataSourceProperties();
//    }
//
//    @Bean
//    @ConfigurationProperties("spring.datasource.shop.configuration")
//    public DataSource shopDataSource(){
//        return shopDataSourceProperties().initializeDataSourceBuilder()
//                .type(HikariDataSource.class).build();
//    }
//
//    @Bean
//    public EntityManagerFactoryBuilder entityManagerFactoryBuilder(){
//        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//        vendorAdapter.setGenerateDdl(false);
//        return new EntityManagerFactoryBuilder(vendorAdapter,new HashMap<>(),null);
//    }
//
//    @Bean(name = "shopEntityManagerFactory")
//    public LocalContainerEntityManagerFactoryBean shopEntityManagerFactoryBean(EntityManagerFactoryBuilder builder){
//        return builder.dataSource(shopDataSource()).packages("com.project.shopapp.Repositories").build();
//    }
//
//    @Bean(name = "shopTransactionManager")
//    public PlatformTransactionManager shopTransactionManager(
//            final @Qualifier("shopEntityManagerFactory")
//                    LocalContainerEntityManagerFactoryBean shopEntityManagerFactory){
//        return new JpaTransactionManager(shopEntityManagerFactory.getObject());
//    }
//}
