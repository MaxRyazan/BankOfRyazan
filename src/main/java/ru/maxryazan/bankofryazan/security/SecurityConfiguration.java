package ru.maxryazan.bankofryazan.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .and()
                .sessionManagement(session -> session.maximumSessions(1))
                .authorizeRequests()
                .antMatchers("/investments/main", "/main", "/forgot", "/restore", "/").permitAll()
                .antMatchers("/main/personal-area", "/main/personal-area/**").authenticated()
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/main/personal-area")
                .and()
                .httpBasic();
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
