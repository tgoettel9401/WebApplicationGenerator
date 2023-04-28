package org.dhbw.webapplicationgenerator.util;

import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Optional;

@Service
public class ResourceFileHelper {

    /**
     * Provides the specified fileName from the Classpath
     * @param fileName FileName that should be found in Files-folder
     * @return File from Classpath
     * @throws FileNotFoundException if the File with the fileName is not found
     */
    public File getFile(String fileName) throws FileNotFoundException {
        Optional<File> file1 = searchInResourceDirectory("files", fileName);
        Optional<File> file2 = searchInResourceDirectory("freemarker-templates", fileName);
        if (file1.isPresent()) {
            return file1.get();
        }
        if (file2.isPresent()) {
            return file2.get();
        }
        throw new FileNotFoundException(fileName + " was not found in files or freemarker-templates");
    }

    public Optional<File> searchInResourceDirectory(String directory, String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        String fileNameWithPath = directory + "/" + fileName;
        if (classLoader.getResourceAsStream(fileNameWithPath) == null) {
            return Optional.empty();
        } else {
            InputStream inputStream = Objects.requireNonNull(classLoader.getResourceAsStream(fileNameWithPath));
            File file = new File(".tmp2/" + fileName);
            try {
                Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return Optional.of(file);
        }
    }


}
