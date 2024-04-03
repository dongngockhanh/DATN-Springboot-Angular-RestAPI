//package com.project.shopapp.Z_ProcessingProvincialData;
//
//import com.project.shopapp.Z_ProcessingProvincialData.ProvincialModel.Commune;
//import com.project.shopapp.Z_ProcessingProvincialData.ProvincialModel.District;
//import com.project.shopapp.Z_ProcessingProvincialData.ProvincialModel.Province;
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
//import javax.sql.DataSource;
//import java.util.HashMap;
//
//
//@Configuration
//@EnableTransactionManagement
//@EnableJpaRepositories(
//        basePackages = "com.project.shopapp.Z_ProcessingProvincialData",
//        entityManagerFactoryRef = "provincialEntityManagerFactory",
//        transactionManagerRef = "provincialTransactionManager"
//)
//public class ServerDatasourceConfig {
//    @Bean
//    @ConfigurationProperties("spring.datasource.provincial")
//    public DataSourceProperties provincialDataSourceProperties(){
//        return new DataSourceProperties();
//    }
//
//    @Bean
//    @ConfigurationProperties("spring.datasource.provincial.configuration")
//    public DataSource provincialDataSource(){
//        return provincialDataSourceProperties().initializeDataSourceBuilder()
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
//    @Bean(name = "provincialEntityManagerFactory")
//    public LocalContainerEntityManagerFactoryBean provincialEntityManagerFactoryBean(EntityManagerFactoryBuilder builder){
//        return builder.dataSource(provincialDataSource()).packages(Province.class,District.class,Commune.class).build();
//    }
//
//    @Bean(name = "provincialTransactionManager")
//    public PlatformTransactionManager provincialTransactionManager(
//        final @Qualifier("provincialEntityManagerFactory")
//        LocalContainerEntityManagerFactoryBean provincialEntityManagerFactory){
//        return new JpaTransactionManager(provincialEntityManagerFactory.getObject());
//    }
//
//}
