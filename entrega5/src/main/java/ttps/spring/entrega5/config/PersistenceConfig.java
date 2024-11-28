package ttps.spring.entrega5.config;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;


@Configuration
@ComponentScan(basePackages = "ttps.spring")
@EnableTransactionManagement
public class PersistenceConfig {
	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
		driverManagerDataSource.setUsername("root");
		driverManagerDataSource.setPassword("12345678");
		driverManagerDataSource.setUrl("jdbc:mysql://localhost:3306/quecomemosjakarta");
		driverManagerDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		return driverManagerDataSource;
	}
	

	/**
	 * El localContainerEntityManagerFactoryBean es un componente de Spring que facilita la configuración de la 
	 * fábrica de administradores de entidades de JPA. Este bean permite la integración de JPA con el contenedor
	 * de Spring, proporcionando una forma sencilla de gestionar las entidades y las transacciones.
	 * @return
	 */
	
	@Bean 
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		emf.setDataSource(dataSource());
		emf.setPackagesToScan("ttps.spring.entrega5");
		emf.setEntityManagerFactoryInterface(jakarta.persistence.EntityManagerFactory.class);
		JpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
		emf.setJpaVendorAdapter(jpaVendorAdapter);
		
		// Add Hibernate DDL property
        emf.getJpaPropertyMap().put("hibernate.hbm2ddl.auto", "update");
		return emf;		
	}
		
	@Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }
	
}
