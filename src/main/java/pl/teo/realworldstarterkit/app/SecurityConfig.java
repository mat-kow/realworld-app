package pl.teo.realworldstarterkit.app;

import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.teo.realworldstarterkit.app.jwt.JwtTokenVerifier;
import pl.teo.realworldstarterkit.app.jwt.JwtUsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;

@Configuration
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtUsernamePasswordAuthenticationFilter(authenticationManager(), secretKey()))
                .addFilterAfter(new JwtTokenVerifier(secretKey()), JwtUsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                    .antMatchers("/api/users"). permitAll()
                .anyRequest().authenticated();
    }



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecretKey secretKey() {
        String key = "longAndSecure longAndSecure longAndSecure longAndSecure longAndSecure longAndSecure";
        return Keys.hmacShaKeyFor(key.getBytes());
    }
}
