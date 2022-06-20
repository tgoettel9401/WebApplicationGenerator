package org.dhbw.webapplicationgenerator.webclient.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.dhbw.webapplicationgenerator.webclient.exception.ValidationException;
import org.dhbw.webapplicationgenerator.webclient.exception.WagException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Getter
public enum Cardinality {
    ONE ("0", "1"), MANY("n", "N", "m", "M");

    private Set<String> keys;

    Cardinality(String... keys) {
        this.keys = new HashSet<>(Arrays.asList(keys));
    }

    @JsonCreator
    static Cardinality fromKey(String key) throws WagException {
        for (Cardinality cardinality : values()) {
            for (String cKey : cardinality.getKeys()) {
                if (key.equals(cKey)) {
                    return cardinality;
                }
            }
        }
        throw new WagException("Specified Key " + key + " cannot be found as Cardinality");
    }

}
