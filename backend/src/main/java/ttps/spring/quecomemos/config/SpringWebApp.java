package ttps.spring.quecomemos.config;

import java.util.Arrays;
import java.util.Properties;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.OperationCustomizer;

import ttps.spring.quecomemos.domain.Rol;
import ttps.spring.quecomemos.domain.Usuario;
import ttps.spring.quecomemos.repos.RolRepository;
import ttps.spring.quecomemos.repos.UsuarioRepository;
import ttps.spring.quecomemos.util.PasswordService;

import javax.sql.DataSource;
import jakarta.persistence.EntityManagerFactory;

@Configuration
@ComponentScan(basePackages = "ttps.spring")
@EnableTransactionManagement
public class SpringWebApp implements WebApplicationInitializer {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setUsername("root");
        driverManagerDataSource.setPassword("12345678");
        driverManagerDataSource.setUrl("jdbc:mysql://localhost:3306/quecomemos");
        driverManagerDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return driverManagerDataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource());
        emf.setPackagesToScan("ttps.spring.quecomemos");
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

    @Bean
    public OpenAPI openApiSpec() {
        return new OpenAPI().components(new Components()
                .addSchemas("ApiErrorResponse", new ObjectSchema()
                        .addProperty("status", new IntegerSchema())
                        .addProperty("code", new StringSchema())
                        .addProperty("message", new StringSchema())
                        .addProperty("fieldErrors", new ArraySchema().items(
                                new Schema<ArraySchema>().$ref("ApiFieldError"))))
                .addSchemas("ApiFieldError", new ObjectSchema()
                        .addProperty("code", new StringSchema())
                        .addProperty("message", new StringSchema())
                        .addProperty("property", new StringSchema())
                        .addProperty("rejectedValue", new ObjectSchema())
                        .addProperty("path", new StringSchema())));
    }

    @Bean
    public OperationCustomizer operationCustomizer() {
        // add error type to each operation
        return (operation, handlerMethod) -> {
            operation.getResponses().addApiResponse("4xx/5xx", new ApiResponse()
                    .description("Error")
                    .content(new Content().addMediaType("*/*", new MediaType().schema(
                            new Schema<MediaType>().$ref("ApiErrorResponse")))));
            return operation;
        };
    }

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true"); // Enable for debugging

        return mailSender;
    }

    @Bean
    public CommandLineRunner init(RolRepository rolRepository, UsuarioRepository usuarioRepository,
            PasswordService passwordService) {
        return args -> {
            // Crear roles por defecto
            Rol adminRole = new Rol("ADMIN");
            Rol managerRole = new Rol("MANAGER");
            Rol userRole = new Rol("USER");

            if (rolRepository.findAll().isEmpty()) {
                adminRole = rolRepository.save(adminRole);
                managerRole = rolRepository.save(managerRole);
                userRole = rolRepository.save(userRole);
            } else {
                adminRole = rolRepository.findByNombreRol("ADMIN");
                managerRole = rolRepository.findByNombreRol("MANAGER");
                userRole = rolRepository.findByNombreRol("USER");
            }

            // Crear usuarios por defecto
            if (usuarioRepository.findAll().isEmpty()) {
                Usuario admin = new Usuario(40534804, "Juan", "Perez", "admin@mail.com",
                        passwordService.hashPassword("admin"), "/usuario_admin.webp", adminRole);
                Usuario manager = new Usuario(40534805, "Maria", "Gomez", "manager@mail.com",
                        passwordService.hashPassword("manager"), "/usuario_manager.webp", managerRole);
                Usuario user = new Usuario(40534806, "Pedro", "Gonzalez", "user@mail.com",
                        passwordService.hashPassword("user"), "/usuario_user.webp", userRole);

                usuarioRepository.saveAll(Arrays.asList(admin, manager, user));
            }
        };
    }

    public void onStartup(ServletContext container) throws ServletException {
        // Create the 'root' Spring application context
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(AppConfig.class);

        // ContextLoaderListener - Manage the lifecycle of the root application context
        container.addListener(new ContextLoaderListener(rootContext));

        // DispatcherServlet - Register and map the dispatcher servlet
        ServletRegistration.Dynamic dispatcher = container.addServlet("DispatcherServlet",
                (Servlet) new DispatcherServlet(rootContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

    }

    @Override
    public void onStartup(jakarta.servlet.ServletContext servletContext) throws jakarta.servlet.ServletException {
        // TODO Auto-generated method stub

    }
}