package security;

//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static pl.koszela.spring.views.IncludeDataView.INCLUDE_DATA;
import static pl.koszela.spring.views.LoginView.LOGIN;

//@Configuration
//@EnableWebSecurity
public class SecurityConfiguration /*extends WebSecurityConfigurerAdapter*/ {
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//                .authorizeRequests()
////                .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll() //
//                .anyRequest().authenticated() //
//                .and().formLogin().loginPage(LOGIN).permitAll()
//                .and().logout().permitAll();
//    }
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails admin =
//                User.withDefaultPasswordEncoder()
//                        .username("admin")
//                        .password("admin1")
//                        .roles("ADMIN")
//                        .build();
//
//        UserDetails user =
//                User.withDefaultPasswordEncoder()
//                        .username("user")
//                        .password("user1")
//                        .roles("USER")
//                        .build();
//        return new InMemoryUserDetailsManager(admin, user);
//    }

}
