package org.dhbw.webapplicationgenerator.webclient;

import lombok.Data;

@Data
public class ProjectRequest {
    private String title;
    private String group;
    private String artifact;

    private boolean havingWeb;
    private boolean havingJpa;

}
