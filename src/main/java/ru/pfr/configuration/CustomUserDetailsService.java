package ru.pfr.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.pfr.model.umikbd.User;
import ru.pfr.service.bdumik.UserService;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(final String username) {
        User byLoginuser = userService.findByLoginuser(username);
        String ROLE = "OPFR";
        if (byLoginuser.getLogin().equals("admin")) {
            ROLE = "ADMIN";
        }
        return new UserPrincipal(byLoginuser, Collections.<GrantedAuthority>singletonList(new SimpleGrantedAuthority("ROLE_" + ROLE)));
    }
}
