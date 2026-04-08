package com.minicloud.portal.config;

import com.minicloud.portal.view.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends VaadinWebSecurity {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Allow public access to the images and styles
        http.authorizeHttpRequests(auth -> 
            auth.requestMatchers(new AntPathRequestMatcher("/images/*.png"),
                               new AntPathRequestMatcher("/line-awesome/**/*.svg")).permitAll()
        );

        super.configure(http);

        // This is the login view to use for Vaadin
        setLoginView(http, LoginView.class);
    }
}
