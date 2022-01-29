package org.dhbw.webapplicationgenerator.util;

import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Objects;

@Service
public class ResourceFileHelper {

    public File getFile(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        String fileNameWithPath = "files/" + fileName;
        return new File(Objects.requireNonNull(classLoader.getResource(fileNameWithPath)).getFile());
    }

}
