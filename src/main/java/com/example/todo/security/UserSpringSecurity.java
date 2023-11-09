package com.example.todo.security;

import com.example.todo.models.enums.ProfileEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
public class UserSpringSecurity implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities; // Collection é uma interface que representa uma coleção de objetos.
    //GrantedAuthority é uma interface do Spring Security que representa uma autoridade associada a um user

    // Construtor que inicializa um objeto UserSpringSecurity
    public UserSpringSecurity(Long id, String username, String password, Set<ProfileEnum> profileEnums) {
        this.id = id;
        this.username = username;
        this.password = password;
        // Mapeia os ProfileEnum para objetos SimpleGrantedAuthority e coleta numa lista de autoridades
        this.authorities = profileEnums.stream().map(x -> new SimpleGrantedAuthority(x.getDescription())).collect(Collectors.toList());
    }

    // Métodos da interface UserDetails (implementados para o Spring Security)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // Verifica se o user possui um determinado perfil
    public boolean hasRole(ProfileEnum profileEnum){
    return getAuthorities().contains(new SimpleGrantedAuthority(profileEnum.getDescription()));
    }

}
