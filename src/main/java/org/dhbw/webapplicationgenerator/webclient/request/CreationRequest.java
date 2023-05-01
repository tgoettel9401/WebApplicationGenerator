package org.dhbw.webapplicationgenerator.webclient.request;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class CreationRequest {
    private CreationRequestProject project;
    private CreationRequestDocker docker;
    private Set<RequestEntity> entities = new HashSet<>();
}
