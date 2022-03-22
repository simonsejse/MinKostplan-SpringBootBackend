package dk.minkostplan.backend.security.rememberme;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
/*
@Configuration
@EnableTransactionManagement
@PropertySource({ "classpath:persistence-h2.properties" })
public class DatabaseConfig {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/pornshare");
        dataSource.setUsername("root");
        dataSource.setPassword("");
        return dataSource;
    }
}
*/