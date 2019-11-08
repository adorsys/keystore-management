package de.adorsys.keymanagement.keyrotation;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@OpenAPIDefinition
@SpringBootApplication(
        scanBasePackages = {
                "de.adorsys.keymanagement.keyrotation.config",
                "de.adorsys.keymanagement.keyrotation.service",
                "de.adorsys.keymanagement.keyrotation.controller"
        },
        exclude = {MongoAutoConfiguration.class} // REST demo app will use H2 configured by application.yml
)
@SuppressWarnings("checkstyle:HideUtilityClassConstructor") // Spring entry point
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
