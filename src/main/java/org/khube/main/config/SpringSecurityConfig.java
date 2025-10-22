package org.khube.main.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
@Configuration
public class SpringSecurityConfig {

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange(exchanges -> exchanges
                        .pathMatchers(HttpMethod.GET, "/**")
                        .permitAll()
                        .pathMatchers("/employee/**").authenticated()
                        .pathMatchers("/department/**").authenticated()
                        .pathMatchers("/address/**").authenticated()
                        .pathMatchers("/company/**").authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(Customizer.withDefaults())
               );

        http.csrf(ServerHttpSecurity.CsrfSpec::disable);

        return  http.build();
    }

//    // Convert JWT roles from realm_access.roles or client roles to Spring authorities
//    private Converter<Jwt, ? extends AbstractAuthenticationToken> grantedAuthoritiesExtractor() {
//        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter(); // default 'scope' -> SCOPE_*
//        return new JwtAuthenticationConverter() {{
//            setJwtGrantedAuthoritiesConverter(jwt -> {
//                Collection<GrantedAuthority> authorities = new HashSet<>(converter.convert(jwt));
//                // map realm_access.roles -> ROLE_{role}
//                Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
//                if (realmAccess != null && realmAccess.containsKey("roles")) {
//                    @SuppressWarnings("unchecked")
//                    Collection<String> roles = (Collection<String>) realmAccess.get("roles");
//                    roles.forEach(r -> authorities.add(new SimpleGrantedAuthority("ROLE_" + r)));
//                }
//                return authorities;
//            });
//        }};
//    }
}
