package org.dhbw.webapplicationgenerator.model.request.frontend;

import lombok.Data;
import org.dhbw.webapplicationgenerator.model.request.Strategy;

@Data
public class Frontend<T> {
    private boolean enabled;
    private Strategy strategy;
    private T data;
}
