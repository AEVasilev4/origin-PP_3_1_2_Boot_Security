package ru.kata.spring.boot_security.demo.configs;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Component
public class SuccessUserHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        // Получаем роли авторизованного пользователя
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        //   сначала проверяем ADMIN
        if (roles.contains("ROLE_ADMIN")) {
            response.sendRedirect("/admin");
        }
        // проверяем USER
        else if (roles.contains("ROLE_USER")) {
            response.sendRedirect("/user");
        }
        // дефолтный редирект
        else {
            response.sendRedirect("/login");
        }
    }
}






