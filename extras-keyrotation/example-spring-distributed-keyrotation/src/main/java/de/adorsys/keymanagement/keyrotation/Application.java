package de.adorsys.keymanagement.keyrotation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(
        scanBasePackages = {
                "de.adorsys.keymanagement.keyrotation.config",
                "de.adorsys.keymanagement.keyrotation.service",
                "de.adorsys.keymanagement.keyrotation.controller"
        },
        scanBasePackageClasses = DataSourceAutoConfiguration.class, // Forcing JDBC
        exclude = MongoAutoConfiguration.class // REST demo app will use H2 configured by application.yml
)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
