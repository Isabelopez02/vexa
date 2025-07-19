package com.vexa.vexa.config;

import com.vexa.vexa.service.UsuarioDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final JwtRequestFilter jwtRequestFilter;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private final UsuarioDetailsServiceImpl usuarioDetailsService;
  private final CustomSuccessHandler customSuccessHandler;

  public SecurityConfig(JwtRequestFilter jwtRequestFilter,
      JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
      UsuarioDetailsServiceImpl usuarioDetailsService,
      CustomSuccessHandler customSuccessHandler) {
    this.jwtRequestFilter = jwtRequestFilter;
    this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    this.usuarioDetailsService = usuarioDetailsService;
    this.customSuccessHandler = customSuccessHandler;
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return usuarioDetailsService;
  }

 @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(usuarioDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/", "/index", "/log", "/registrar", "/error",
                "/CSS/**", "/Img/**", "/Icons/**", "/js/**", "/JavaScript/**", "/Font/**", "/webjars/**")
            .permitAll()
            .requestMatchers("/inicio","/etiquetas/**").hasRole("ADMIN")
            .anyRequest().authenticated())
        .formLogin(form -> form
        .loginPage("/") 
            .loginProcessingUrl("/log")
                        .defaultSuccessUrl("/inicio", true)
            .failureUrl("/?error=true"))
            
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/")
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID")
            .permitAll());
            

    http.authenticationProvider(authenticationProvider());
    
    return http.build();
  }
}

/*
 * @Configuration
 * 
 * @EnableWebSecurity
 * public class SecurityConfig {
 * private final UsuarioDetailsServiceImpl usuarioDetailsService;
 * 
 * public SecurityConfig(UsuarioDetailsServiceImpl usuarioDetailsService) {
 * this.usuarioDetailsService = usuarioDetailsService;
 * }
 * 
 * @Bean
 * public SecurityFilterChain securityFilterChain(HttpSecurity http) throws
 * Exception {
 * http
 * 
 * .authorizeHttpRequests(auth -> auth
 * .requestMatchers("/", "/index", "/login", "/registrar", "/error",
 * "/CSS/**", "/Img/**", "/Icons/**", "/js/**", "/JavaScript/**", "/Font/**",
 * "/webjars/**")
 * .permitAll()
 * 
 * .requestMatchers("/inicio","/etiquetas/**").hasRole("ADMIN")
 * .anyRequest().authenticated())
 * .formLogin(form -> form
 * .loginPage("/") // el modal está en "/"
 * .loginProcessingUrl("/login")
 * .usernameParameter("username")
 * .passwordParameter("password")
 * .defaultSuccessUrl("/inicio", true)
 * .failureUrl("/?error=true"))
 * 
 * .logout(logout -> logout
 * .logoutUrl("/logout")
 * .logoutSuccessUrl("/") // te redirige al inicio
 * .permitAll());
 * http.authenticationProvider(authenticationProvider());
 * return http.build();
 * }
 * 
 * @Bean
 * public PasswordEncoder passwordEncoder() {
 * return new BCryptPasswordEncoder();
 * }
 * 
 * @Bean
 * public DaoAuthenticationProvider authenticationProvider() {
 * DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
 * authProvider.setUserDetailsService(usuarioDetailsService);
 * authProvider.setPasswordEncoder(passwordEncoder());
 * return authProvider;
 * }
 * 
 * }
 */