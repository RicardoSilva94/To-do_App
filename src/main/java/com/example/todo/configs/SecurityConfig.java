package com.example.todo.configs;

import com.example.todo.security.JWTAuthenticationFilter;
import com.example.todo.security.JWTAuthorizationFilter;
import com.example.todo.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;


@Configuration
    @EnableWebSecurity
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    public class SecurityConfig {


    @Autowired
    private UserDetailsService userDetailsService;

        private AuthenticationManager authenticationManager;

        @Autowired
        private JWTUtil jwtUtil;


    // URLs públicas que não requerem autenticação
        private static final String[] PUBLIC_MATCHERS = {
                "/"
        };
        private static final String[] PUBLIC_MATCHERS_POST = {
                "/user",
                "/login"
        };

    // Configuração do filtro de segurança HTTP
    @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

            http.cors().and().csrf().disable();

        // Configura o gestor de autenticação
            AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
            authenticationManagerBuilder.userDetailsService(this.userDetailsService).passwordEncoder(bCryptPasswordEncoder());
            this.authenticationManager = authenticationManagerBuilder.build();

            http.authorizeRequests()
                    .antMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()
                    .antMatchers(PUBLIC_MATCHERS).permitAll()
                    .anyRequest().authenticated().and().authenticationManager(authenticationManager);

        // Adiciona os filtros personalizados para autenticação e autorização JWT
            http.addFilter(new JWTAuthenticationFilter(this.authenticationManager, this.jwtUtil));
            http.addFilter(new JWTAuthorizationFilter(this.authenticationManager, this.jwtUtil, this.userDetailsService));

        // Define a política de gestão de sessão como STATELESS (sem estado)
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

            return http.build();
        }

    // Configuração das permissões para CORS
        @Bean
        CorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
            configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE"));
            final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);
            return source;
        }

    // Configura um codificador de senhas para usar na autenticação
        @Bean
        public BCryptPasswordEncoder bCryptPasswordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }
