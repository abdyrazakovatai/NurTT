package dev.nurtt.config;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class WebConfigurer {
    @Bean
    public WebMvcAutoConfiguration corsConfiguration() {
        return new WebMvcAutoConfiguration(){

            public void addCorsMappings(CorsRegistry registry){
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST","PUT","DELETE", "OPTIONS")
                        .allowedHeaders("*");
            }
        };

    }
}
