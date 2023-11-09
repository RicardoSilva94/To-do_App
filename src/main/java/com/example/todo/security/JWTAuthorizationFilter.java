package com.example.todo.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private JWTUtil jwtUtil;

    private UserDetailsService userDetailsService;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, UserDetailsService userDetailsService) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    // Método executado para filtrar as solicitações
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        // Verifica se o cabeçalho de autorização contém um token JWT válido
        String authorizationHeader =request.getHeader("Authorization");
        if (Objects.nonNull(authorizationHeader) && authorizationHeader.startsWith("Bearer")){
            String token = authorizationHeader.substring(7); // Remove o prefixo "Bearer" para obter o token
            UsernamePasswordAuthenticationToken auth = getAuthentication(token); // Obtém a autenticação com base no token
            if(Objects.nonNull(auth))
                SecurityContextHolder.getContext().setAuthentication(auth); // Define a autenticação no contexto de segurança
        }
        filterChain.doFilter(request, response); // Continua o processamento da solicitação
    }

    // Obtém a autenticação com base no token JWT
    private UsernamePasswordAuthenticationToken getAuthentication(String token){
        if(this.jwtUtil.isValidToken(token)){
            String username = this.jwtUtil.getUsername(token); // Obtém o username do token
            UserDetails user = this.userDetailsService.loadUserByUsername(username); // Carrega os details do user
            UsernamePasswordAuthenticationToken authenticatedUser = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            return authenticatedUser; // Retorna a autenticação
        }
        return null;
    }

}
//Este filtro é responsável por verificar se um token JWT válido está presente nas solicitações e, se estiver, autenticar o utilizador o com base no token, permitindo que ele aceda recursos protegidos.