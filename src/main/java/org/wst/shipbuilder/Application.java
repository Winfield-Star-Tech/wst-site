package org.wst.shipbuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@SpringBootApplication
@Configuration
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    @Bean
    public String myBean() {
    	return "foo";
    }

    @Bean
    public DriverManagerDataSource dataSource() {
    	DriverManagerDataSource dataSource = new DriverManagerDataSource();
    	dataSource.setDriverClassName("org.postgresql.Driver");
    	dataSource.setUrl("jdbc:postgresql://localhost:5432/shipmaker");
    	dataSource.setUsername("shipmaker");
    	dataSource.setPassword("shipmaker");
    	return dataSource;
    }
    @Bean
    public ItemManufactureDAO itemManufactureDAO() {
    	return new ItemManufactureDAO();
    }
    @Bean
    public InvTypeDAO invTypeDAO() {
    	return new InvTypeDAO();
    }
}