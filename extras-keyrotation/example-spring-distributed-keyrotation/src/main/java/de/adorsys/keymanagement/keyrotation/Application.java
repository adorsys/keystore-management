package de.adorsys.keymanagement.keyrotation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@EnableScheduling
@SpringBootApplication(
        scanBasePackages = {
                "de.adorsys.keymanagement.keyrotation.config",
                "de.adorsys.keymanagement.keyrotation.service",
                "de.adorsys.keymanagement.keyrotation.controller"
        },
        exclude = {MongoAutoConfiguration.class} // REST demo app will use H2 configured by application.yml
)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
