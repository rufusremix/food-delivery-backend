package com.rufus.store.restaurants;

import com.rufus.store.common.SecurityRules;
import com.rufus.store.users.Role;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class RestaurantSecurityRules implements SecurityRules {
    @Override
    public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        registry.requestMatchers(HttpMethod.GET, "/restaurants/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/restaurants/**").hasRole(Role.ADMIN.name())
                .requestMatchers(HttpMethod.PUT, "/restaurants/**").hasRole(Role.ADMIN.name())
                .requestMatchers(HttpMethod.DELETE, "/restaurants/**").hasRole(Role.ADMIN.name());
    }
}
