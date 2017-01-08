package com.yyh.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.yyh.domain.Authority;
import com.yyh.domain.User;
import com.yyh.security.jwt.JWTConfigurer;
import com.yyh.security.jwt.TokenProvider;
import com.yyh.service.UserService;
import com.yyh.web.rest.vm.LoginVM;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class UserJWTController {

    @Inject
    private TokenProvider tokenProvider;

    @Inject
    private AuthenticationManager authenticationManager;

    @Inject
    private UserService userService;

    @PostMapping("/authenticate")
    @Timed
    public ResponseEntity<?> authorize(@Valid @RequestBody LoginVM loginVM, HttpServletResponse response) {

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());

        try {
            Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            boolean rememberMe = (loginVM.isRememberMe() == null) ? false : loginVM.isRememberMe();
            String jwt = tokenProvider.createToken(authentication, rememberMe);
            response.addHeader(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);
            User user = userService.getUserWithAuthorities();
            Set<Authority> authorities = user.getAuthorities();
            Map<String, Object> result = new HashMap<>();
            result.put("user", user);
            result.put("jwt", new JWTToken(jwt));
            result.put("authorityForAdmin", false);
            result.put("authorityForUser", false);
            for (Authority authority : authorities) {
                if (authority.getName().equals("ROLE_ADMIN")) {
                    result.put("authorityForAdmin", true);
                } else if (authority.getName().equals("ROLE_USER")) {
                    result.put("authorityForUser", true);
                }
            }
            return ResponseEntity.ok(result);
        } catch (AuthenticationException exception) {
            return new ResponseEntity<>(Collections.singletonMap("AuthenticationException",exception.getLocalizedMessage()), HttpStatus.UNAUTHORIZED);
        }
    }
}
