package org.dhbw.webapplicationgenerator.webclient.request;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class ProjectRequest {
    private String title;
    private String group;
    private String artifact;
    private String description;

    private Set<RequestEntity> entities = new HashSet<>();

    private boolean havingWeb;
    private boolean havingJpa;

}
