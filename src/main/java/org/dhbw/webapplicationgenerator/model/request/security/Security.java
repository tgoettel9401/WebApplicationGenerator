package org.dhbw.webapplicationgenerator.model.request.security;

import lombok.Data;

@Data
public class Security {
    private boolean enabled = true;
    private String defaultUsername = "admin";
    private String defaultPassword = "secret";
    private String userTableName = "app_users";
    private String roleTableName = "roles";
}
