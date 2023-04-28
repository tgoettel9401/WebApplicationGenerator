package org.dhbw.webapplicationgenerator.generator.util;

import freemarker.template.Template;
import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.webclient.exception.WagException;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@Service
@AllArgsConstructor
public class FreemarkerTemplateProcessor {

    private static final String TMP_PATH = ".tmp/";

    private final FreeMarkerConfigurer freemarkerConfigurer;

    /**
     * Gets a templateFilename, dataModel and newFilename. It processes the template and returns the created file.
     * The template has to be available with the given filename in the resource folder freemarker-templates.
     * @param templateFilename The template filename to be processed
     * @param dataModel All variables (data model) needed for the template
     * @param newFilename The name the created file should has
     * @return File created based on the template and data model
     */
    public File process(String templateFilename, Map<String, Object> dataModel, String newFilename) {
        try {
            // Load template and initialize both File and FileWriter
            Template template = freemarkerConfigurer.getConfiguration().getTemplate(templateFilename);
            File file = new File(String.valueOf(Files.createFile(Path.of(TMP_PATH + newFilename))));
            FileWriter fileWriter = new FileWriter(file);

            // Process template, then create and return the file.
            template.process(dataModel, fileWriter);
            return file;

        } catch (Exception e) {
            e.printStackTrace();
            throw new WagException("Processing template " + templateFilename + " with new name " + newFilename +
                    " failed with an exception (message: " + e.getMessage());
        }
    }

}
