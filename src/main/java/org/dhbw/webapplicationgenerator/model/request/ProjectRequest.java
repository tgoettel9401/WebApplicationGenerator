package org.dhbw.webapplicationgenerator.model.request;

import lombok.Data;
import org.dhbw.webapplicationgenerator.model.request.backend.Backend;
import org.dhbw.webapplicationgenerator.model.request.datamodel.DataModel;
import org.dhbw.webapplicationgenerator.model.request.database.Database;
import org.dhbw.webapplicationgenerator.model.request.deployment.Deployment;
import org.dhbw.webapplicationgenerator.model.request.frontend.Frontend;

@Data
public class ProjectRequest {
    private String title;
    private String description;
    private Deployment<?> deployment;
    private Backend<?> backend;
    private Frontend<?> frontend;
    private Database<?> database;
    private DataModel dataModel;

    public boolean isDeploymentEnabled() {
        return deployment != null && deployment.isEnabled();
    }

    public boolean isBackendEnabled() {
        return backend != null && backend.isEnabled();
    }

    public boolean isFrontendEnabled() {
        return frontend != null && frontend.isEnabled();
    }

    public boolean isDatabaseEnabled() {
        return database != null && database.isEnabled();
    }

    public String getTitleWithoutSpaces() {
        return title.replace(" ", "");
    }

}
