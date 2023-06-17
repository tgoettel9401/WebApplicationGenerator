package org.dhbw.webapplicationgenerator.model.request.backend;

import lombok.Getter;
import org.dhbw.webapplicationgenerator.generator.backend.java.buildtool.Dependency;
import org.dhbw.webapplicationgenerator.webclient.exception.WagException;

@Getter
public enum DatabaseProduct {
    POSTGRESQL("org.postgresql", "postgresql", "", "", "org.hibernate.dialect.PostgreSQLDialect"),
    H2("com.h2database", "h2", "", "runtime", "org.hibernate.dialect.H2Dialect");

    private final String driverGroup;
    private final String driverArtifact;
    private final String driverVersion;
    private final String driverScope;
    private final String driverDialect;

    DatabaseProduct(String driverGroup, String driverArtifact, String driverVersion, String driverScope, String driverDialect) {
        this.driverGroup = driverGroup;
        this.driverArtifact = driverArtifact;
        this.driverVersion = driverVersion;
        this.driverScope = driverScope;
        this.driverDialect = driverDialect;
    }

    public Dependency getDependency() {
        return new Dependency(driverGroup, driverArtifact, driverVersion, driverScope, "", false);
    }

    public static DatabaseProduct fromKey(String key) {
        switch (key.toLowerCase()) {
            case "postgres":
            case "postgresql":
                return POSTGRESQL;
            case "h2":
                return H2;
            default:
                throw new WagException("Invalid database product supplied");
        }
    }

}
