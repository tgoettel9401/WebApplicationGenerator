package org.dhbw.webapplicationgenerator.webclient.validation;

import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.webclient.exception.ValidationException;

public interface RequestValidator {
    void validate(ProjectRequest request) throws ValidationException;
}
