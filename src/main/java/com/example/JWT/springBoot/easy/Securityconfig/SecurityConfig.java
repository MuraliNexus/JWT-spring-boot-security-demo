package com.example.JWT.springBoot.easy.Securityconfig;

import com.example.JWT.springBoot.easy.CustomUserdetailsService.CustomUserDetails;
import com.example.JWT.springBoot.easy.Filters.JwtAuthFilter;
import com.example.JWT.springBoot.easy.Repoistory.UserRepoistory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserRepoistory userRepoistory;
    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(UserRepoistory userRepoistory, JwtAuthFilter jwtAuthFilter) {
        this.userRepoistory = userRepoistory;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/sigin", "/authenticate").permitAll()
                                .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider());
        //TODO no httpbasic needed because this is our own custom jwtfilter
                //.httpBasic(Customizer.withDefaults());
        //todo placing our jwt filter and making it before usernamepasswordauthfilter;
         http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepoistory.findByName(username)
                .map(CustomUserDetails::new) // Convert Users → UserDetails
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
}
