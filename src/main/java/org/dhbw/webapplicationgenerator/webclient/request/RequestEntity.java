package org.dhbw.webapplicationgenerator.webclient.request;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class RequestEntity {
    private String title;
    private Set<EntityAttribute> attributes = new HashSet<>();

    // TODO: Test and add logic for this.
    public String getEntityName() {
        String tempTitle = title.replace(" ", "");
        StringBuilder entityName = new StringBuilder();
        for (String titleParts : tempTitle.split("-")) {
            if (entityName.length() == 0) {
                entityName.append(titleParts);
            } else {
                entityName.append(titleParts.toCharArray()[0]);
            }
        }
        return entityName.toString();
    }
}
