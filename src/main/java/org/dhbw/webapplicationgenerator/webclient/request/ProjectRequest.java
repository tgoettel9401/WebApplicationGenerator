package org.dhbw.webapplicationgenerator.webclient.request;

import lombok.Data;

@Data
public class ProjectRequest {
    private String title;
    private String group;
    private String artifact;
    private String description;

    private boolean havingWeb;
    private boolean havingJpa;

}
