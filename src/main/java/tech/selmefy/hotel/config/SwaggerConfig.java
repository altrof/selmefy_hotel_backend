package tech.selmefy.hotel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.Collections.singletonList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
public class SwaggerConfig {

    private final List<AuthorizationScope> scopes = singletonList(new AuthorizationScope("global", "Global scope"));

    @Bean
    public Docket api() {
        final Set<String> defaultContentType = newHashSet(APPLICATION_JSON_VALUE);

        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .produces(defaultContentType)
            .consumes(defaultContentType)
            .securityContexts(singletonList(securityContext()))
            .securitySchemes(List.of(apiKey()))
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.any())
            .build()
            .pathMapping("/");
    }

    ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("Selmefy Hotel REST API")
            .description("Hotel API Documentation")
            .version("1")
            .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    private List<SecurityReference> defaultAuth() {
        return singletonList(new SecurityReference("JWT", scopes.toArray(new AuthorizationScope[1])));

    }
}
