package org.dhbw.webapplicationgenerator.webclient.request;

import lombok.Data;

@Data
public class CreationRequestProject {
    private String title;
    private String group;
    private String artifact;
    private String description;

    public String getTitleWithoutSpaces() {
        return title.replace(" ", "");
    }
}
