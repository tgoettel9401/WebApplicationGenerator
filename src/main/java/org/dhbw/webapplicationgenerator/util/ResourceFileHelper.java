package org.dhbw.webapplicationgenerator.util;

import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
        if (classLoader.getResourceAsStream(fileNameWithPath) == null) {
            throw new FileNotFoundException(fileNameWithPath);
        } else {
            InputStream inputStream = Objects.requireNonNull(classLoader.getResourceAsStream(fileNameWithPath));
            File file = new File(".tmp2/" + fileName);
            try {
                Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return file;
        }
    }

}
