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
  /*  <bean id="dataSource" 
    		 class="org.springframework.jdbc.datasource.DriverManagerDataSource">
    		    <property name="driverClassName">
    		         <value>${jdbc.driver}</value>
    		    </property>
    		    <property name="url">
    		         <value>${jdbc.url}</value>
    		    </property>
    		    <property name="username">
    		         <value>${jdbc.user}</value>
    		    </property>
    		    <property name="password">
    		         <value>${jdbc.password}</value>
    		    </property>
    		</bean> */
}