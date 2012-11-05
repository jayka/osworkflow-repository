package com.opensymphony.workflow.service.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class Util {

    private Util() {

    }

    public static Authentication getActualLoggedUser() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

}
