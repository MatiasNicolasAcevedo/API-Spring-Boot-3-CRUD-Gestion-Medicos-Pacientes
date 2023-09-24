package med.voll.api.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // se utiliza para marcar una clase como una fuente de definiciones de beans para el contenedor de Spring.
@EnableWebSecurity // Se utiliza para activar y configurar la seguridad web en una aplicación Spring. Esta anotación marca una clase de configuración como responsable de definir las reglas de seguridad para las solicitudes web en la aplicación.
public class SecurityConfigurations {

    @Autowired
    private SecurityFilter securityFilter;

    // bean en Spring es un componente gestionado por el contenedor de Spring que representa una instancia de una clase Java.
    @Bean // Esta anotación permite la creación personalizada y la configuración de beans, lo que brinda un alto grado de control sobre cómo se gestionan los componentes en el contexto de Spring.
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf().disable()
                           .sessionManagement()
                           .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // le indicamos el tipo de sesion.
                           .and().authorizeRequests()
                           .requestMatchers(HttpMethod.POST, "/login")
                           .permitAll()
                           .anyRequest()
                           .authenticated()
                           .and().addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class) //valida que el usaurio existe y esta autenticado.
                           .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder PasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
