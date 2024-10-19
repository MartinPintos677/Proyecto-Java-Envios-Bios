package com.example.envios_bios.configuracion;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.FlashMapManager;
import org.springframework.web.servlet.support.SessionFlashMapManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity //Para cuando implementemos security
public class ConfiguracionSeguridad {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/").permitAll()
                .requestMatchers("/index").permitAll()
                .requestMatchers("/categorias/**").hasAuthority("empleado")
                .requestMatchers("/clientes/registrarcliente").anonymous()
                .requestMatchers("/clientes/**").hasAuthority("cliente")
                .requestMatchers("/empleados/**").hasAuthority("empleado")
                .requestMatchers("/mierror").permitAll()
                .requestMatchers("/estadosRastreos/**").hasAuthority("empleado")
                .requestMatchers("/paquetes/**").hasAnyAuthority("empleado", "cliente")
                .requestMatchers("/sucursales/**").hasAuthority("empleado")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login").permitAll()
                .successHandler(new SimpleUrlAuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
                            throws IOException, ServletException {
                        FlashMap flashMap = new FlashMap();
                        flashMap.put("mensaje", "Has ingresado a tu cuenta.");

                        FlashMapManager flashMapManager = new SessionFlashMapManager();
                        flashMapManager.saveOutputFlashMap(flashMap, request, response);

                        super.onAuthenticationSuccess(request, response, authentication);
                    }

                }))
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll()
                .logoutSuccessHandler(new SimpleUrlLogoutSuccessHandler() {

                    @Override
                    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
                            throws IOException, ServletException {
                        FlashMap flashMap = new FlashMap();
                        flashMap.put("mensaje", "Has salido de tu cuenta.");

                        FlashMapManager flashMapManager = new SessionFlashMapManager();
                        flashMapManager.saveOutputFlashMap(flashMap, request, response);

                        super.onLogoutSuccess(request, response, authentication);
                    }

                }));
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
            .requestMatchers("/css/**", "/imagenes/**");
    }
    
}
