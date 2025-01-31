package com.vikas.EZmanage.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/demo")
public class DemoController {

    @GetMapping
    public String hi() {
        return "HIII!!!";
    }

    @GetMapping("/session")
    public String getSession(HttpServletRequest httpServletRequest) {
        return "Session : " + httpServletRequest.getSession().getId();
    }

    @GetMapping("/csrf")
    public CsrfToken getCsrf(HttpServletRequest httpServletRequest) {
        return (CsrfToken) httpServletRequest.getAttribute("_csrf");
    }
}
