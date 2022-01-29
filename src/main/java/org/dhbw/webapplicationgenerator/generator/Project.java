package org.dhbw.webapplicationgenerator.generator;

import lombok.Data;
import org.dhbw.webapplicationgenerator.generator.model.StructureElement;

@Data
public class Project {

    private String name;

    private StructureElement fileStructure;

}
