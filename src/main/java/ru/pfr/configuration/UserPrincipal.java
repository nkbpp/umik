package ru.pfr.configuration;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.pfr.model.umikbd.User;

import java.util.Collection;

@Getter
public class UserPrincipal implements UserDetails {

    private final User user;

    private final Collection<? extends GrantedAuthority> role;

    public UserPrincipal(User user, Collection<? extends GrantedAuthority> role) {
        this.user = user;
        this.role = role;
    }

    @Override
    public String getUsername() {
        return user.getLogin();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role;
    }

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

}

