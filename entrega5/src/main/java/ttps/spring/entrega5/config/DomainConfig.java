package ttps.spring.entrega5.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EntityScan("ttps.spring.entrega5.domain")
@EnableJpaRepositories("ttps.spring.entrega5.repos")
@EnableTransactionManagement
public class DomainConfig {
}
