package devpilot.backend.config;


import java.util.Arrays;
import java.util.List;

import org.hibernate.sql.Delete;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration 
public class CorsConfig {

    @Bean 
    CorsConfigurationSource corsConfigSource(
        @Value("${ app.cora.allowed-origins}") String allowedOrigin
    ){
        CorsConfiguration config=new CorsConfiguration();
        List<String> origins=Arrays.stream(allowedOrigin.split(","))
        .map(String::trim)
        .filter(s -> !s.isEmpty())
        .toList();

        config.setAllowedOrigins(origins);
        config.setAllowedMethods(List.of("GET","PUT","POST","DELETE","PATCH","OPTIONS"));
        config.setAllowCredentials(true);
        config.setAllowedHeaders(List.of("*"));
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
