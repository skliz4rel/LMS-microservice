package com.lms.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class GlobalCorsConfig {// extends WebSecurityConfigurerAdapter

    @Bean
    public WebMvcConfigurer corsConfigurer(){
        System.out.println("Got inside this configuration class bean");

        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("PUT","DELETE","POST","GET");
            }
        };
    }


   /* @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
       // http
                // ...
               http .redirectToHttps()
                .httpsRedirectWhen(e -> e.getRequest().getHeaders().containsKey("X-Forwarded-Proto"));
        return http.build();
    }*/


}
