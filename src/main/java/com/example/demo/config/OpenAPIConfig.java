package com.example.demo.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenAPIConfig {

  @Value("${swagger.openapi.dev-url}")
  private String devUrl;

  @Bean
  public OpenAPI myOpenAPI() {
    Server devServer = new Server();
    devServer.setUrl(devUrl);
    devServer.setDescription("Server URL in Development environment");

    Contact contact = new Contact();
    contact.setEmail("celopacob@gmail.com");
    contact.setName("Marcelo Pacobahyba");
    contact.setUrl("https://www.linkedin.com/in/marcelo-pacobahyba");

    License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

    Info info = new Info()
        .title("Gutentex Based Books API")
        .version("1.0")
        .contact(contact)
        .description("This API exposes endpoints to search and rate books.")
        .license(mitLicense);

    return new OpenAPI().info(info).servers(List.of(devServer));
  }
}