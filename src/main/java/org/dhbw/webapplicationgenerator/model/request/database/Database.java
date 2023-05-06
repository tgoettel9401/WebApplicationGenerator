package org.dhbw.webapplicationgenerator.model.request.database;

import lombok.Data;
import org.dhbw.webapplicationgenerator.model.request.Strategy;

@Data
public class Database<T> {
    private boolean enabled;
    private Strategy strategy;
    private T data;
}
