package fr.bankaccountkata.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

        @Bean
        public Docket productApi() {
            return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                    .select()
                    .apis(RequestHandlerSelectors.basePackage("fr.bankaccountkata.controller"))
                    .paths(regex("/api.*"))
                    .build();

        }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Simple BankAccount API")
                .description("Simple API reference for Bank Account Kata")
                .contact("mokrane.kadri@softeam.fr")
                .licenseUrl("mokrane.kadri@softeam.fr").version("0.0.1").build();
    }
}
