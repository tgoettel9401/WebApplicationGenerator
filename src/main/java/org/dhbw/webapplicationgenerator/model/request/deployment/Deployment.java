package org.dhbw.webapplicationgenerator.model.request.deployment;

import lombok.Data;
import org.dhbw.webapplicationgenerator.model.request.Strategy;

@Data
public class Deployment<T> {
    private boolean enabled;
    private Strategy strategy;
    private T data;
}
