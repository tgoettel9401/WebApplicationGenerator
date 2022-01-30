package org.dhbw.webapplicationgenerator.util;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;

@Service
public class ResourceFileHelper {

    /**
     * Provides the specified fileName from the Classpath
     * @param fileName FileName that should be found in Files-folder
     * @return File from Classpath
     * @throws FileNotFoundException if the File with the fileName is not found
     */
    public File getFile(String fileName) throws FileNotFoundException {
        ClassLoader classLoader = getClass().getClassLoader();
        String fileNameWithPath = "files/" + fileName;
        if (classLoader.getResource(fileNameWithPath) == null) {
            throw new FileNotFoundException(fileNameWithPath);
        } else {
            return new File(Objects.requireNonNull(classLoader.getResource(fileNameWithPath)).getFile());
        }
    }

}
