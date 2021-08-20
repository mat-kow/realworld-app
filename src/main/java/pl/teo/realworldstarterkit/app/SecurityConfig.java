package pl.teo.realworldstarterkit.app;

import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.teo.realworldstarterkit.app.jwt.JwtTokenVerifier;

import javax.crypto.SecretKey;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(new JwtTokenVerifier(secretKey()), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                    .antMatchers("/h2-console/**").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/users", "/api/users/login"). permitAll()
                    .antMatchers(HttpMethod.GET, "/api/profiles/**", "/api/articles/**", "/api/tags")
                        .permitAll()
                    .antMatchers(HttpMethod.POST, "/api/profiles/**").authenticated()
                    .antMatchers(HttpMethod.DELETE, "/api/profiles/**").authenticated()
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
