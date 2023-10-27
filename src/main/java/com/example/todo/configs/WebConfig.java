package com.example.todo.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // Indica que esta classe é uma classe de configuração do Spring.
@EnableWebMvc // Habilita o suporte ao framework MVC do Spring.
public class WebConfig implements WebMvcConfigurer {

    // Configuração para permitir requisições de origens diferentes (CORS)
    //Este método configura as regras de CORS (Cross-Origin Resource Sharing).
    // Permite requisições de origens diferentes, especificando origens permitidas, cabeçalhos permitidos e métodos HTTP permitidos.
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") //permitir qualquer requisição que venha de fora // Define que a configuração se aplica a todas as URLs
        .allowedOrigins("*").allowedHeaders("*").allowedMethods("GET", "POST", "PUT", "Patch", "DELETE", "OPTIONS");
    }
}
// allowerOrigins permite origens de qualquer lugar
// // allowedHeaders Permite todos os cabeçalhos HTTP