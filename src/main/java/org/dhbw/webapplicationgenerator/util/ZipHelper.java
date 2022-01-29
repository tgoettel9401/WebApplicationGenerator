package org.dhbw.webapplicationgenerator.util;

import org.dhbw.webapplicationgenerator.generator.Project;
import org.dhbw.webapplicationgenerator.generator.model.ProjectFile;
import org.dhbw.webapplicationgenerator.generator.model.StructureElement;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class ZipHelper {

    public ByteArrayOutputStream zip(Project project) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
        addElementToZip(project.getFileStructure(), zipOutputStream);
        zipOutputStream.close();
        byteArrayOutputStream.close();
        return byteArrayOutputStream;
    }

    private void addElementToZip(StructureElement element, ZipOutputStream zipOut) throws IOException {
        if (element.getLevel().isDirectory()) {
            addDirectoryToZip(element, zipOut);
        } else {
            addFileToZip(element, zipOut);
        }
    }

    private void addDirectoryToZip(StructureElement element, ZipOutputStream zipOut) throws IOException {
        ZipEntry zipEntry = new ZipEntry(element.getPath());
        zipOut.putNextEntry(zipEntry);
        zipOut.closeEntry();
        for (StructureElement nestedElement : element.getChildren()) {
            addElementToZip(nestedElement, zipOut);
        }
    }

    private void addFileToZip(StructureElement element, ZipOutputStream zipOut) throws IOException {
        ProjectFile fileWrapper = ((ProjectFile) element);
        try (FileInputStream fileInputStream = new FileInputStream(fileWrapper.getFile())) {
            ZipEntry zipEntry = new ZipEntry(fileWrapper.getPath());
            zipOut.putNextEntry(zipEntry);
            byte[] bytes = new byte[1024];
            int length;
            while ((length = fileInputStream.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
        }
        zipOut.closeEntry();
    }

}
