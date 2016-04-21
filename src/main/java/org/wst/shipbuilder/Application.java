package org.wst.shipbuilder;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Logger;

import javax.persistence.EntityManagerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;


@SpringBootApplication
@Configuration
@EnableJpaRepositories
public class Application {

	static Logger log = Logger.getLogger(Application.class.getName());
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public DriverManagerDataSource dataSource()  {
    	
    	DriverManagerDataSource dataSource = new DriverManagerDataSource();
    	String dbUrlEnv = System.getenv("DATABASE_URL");
    	String dbHost = "localhost";
    	int dbPort = 5432;
    	String dbPath = "wst-site";
    	String dbUsername = "wst-site";
    	String dbPassword = "wst-site1";
    	if (dbUrlEnv != null) {
    		//dbUrlEnv = dbUrlEnv.replace("jdbc:postgresql", "http");
    		URI dbUrl;
			try {
				dbUrl = new URI(dbUrlEnv);
	    		dbHost = dbUrl.getHost();
	    		dbPort = dbUrl.getPort();
	    		dbPath = dbUrl.getPath();
	    		dbUsername = dbUrl.getUserInfo().split(":")[0];
	    		dbPassword = dbUrl.getUserInfo().split(":")[1];
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
    	}
    	log.info("url = " + "jdbc:postgresql://" + dbHost + ":" + dbPort + dbPath);
    	log.info("user = " + dbUsername);
    	log.info("password = " + dbPassword);
    	dataSource.setDriverClassName("org.postgresql.Driver");
    	dataSource.setUrl("jdbc:postgresql://" + dbHost + ":" + dbPort + dbPath);
    	dataSource.setUsername(dbUsername);
    	dataSource.setPassword(dbPassword);
    	return dataSource;
    }
    @Bean
    public EntityManagerFactory entityManagerFactory() {

      HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
      vendorAdapter.setGenerateDdl(true);

      LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
      factory.setJpaVendorAdapter(vendorAdapter);
      factory.setPackagesToScan("org.wst.shipbuilder.data");
      factory.setDataSource(dataSource());
      factory.afterPropertiesSet();

      return factory.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {

      JpaTransactionManager txManager = new JpaTransactionManager();
      txManager.setEntityManagerFactory(entityManagerFactory());
      return txManager;
    }
   
}