package org.dhbw.webapplicationgenerator.model.request.backend;

import lombok.Data;
import org.dhbw.webapplicationgenerator.model.request.Strategy;

@Data
public class Backend<T> {
    private boolean enabled;
    private Strategy strategy;
    private T data;
}
