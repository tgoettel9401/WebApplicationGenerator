package org.dhbw.webapplicationgenerator.generator.frontend;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.Project;
import org.dhbw.webapplicationgenerator.generator.base_project.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.generator.entity.DataType;
import org.dhbw.webapplicationgenerator.generator.model.ProjectDirectory;
import org.dhbw.webapplicationgenerator.util.ResourceFileHelper;
import org.dhbw.webapplicationgenerator.webclient.request.EntityAttribute;
import org.dhbw.webapplicationgenerator.webclient.request.CreationRequest;
import org.dhbw.webapplicationgenerator.webclient.request.RequestEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class FrontendGenerator extends FileFolderGenerator {

    private static final String TMP_PATH = ".tmp/";
    private static final String HTML_FILE_ENDING = ".html";

    private final ResourceFileHelper resourceFileHelper;

    public Project create(Project project, CreationRequest request) {

        ProjectDirectory resourcesDir = getResourcesDirectory(project);

        try {
            createFrontend(request, resourcesDir);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return project;
    }

    private void createFrontend(CreationRequest request, ProjectDirectory parent) throws IOException {

        ProjectDirectory templatesDir = addDirectory("templates", Optional.of(parent));

        addFile(resourceFileHelper.getFile("main.html"), templatesDir);
        addFile(resourceFileHelper.getFile("footer.html"), templatesDir);
        addFile(createNavbarFile(request.getEntities()), templatesDir);

        // TODO: Add dashboard first.

        for (RequestEntity entity : request.getEntities()) {
            addFile(createOverviewFile(entity), templatesDir);
            addFile(createDetailsFile(entity), templatesDir);
        }

    }

    private File createNavbarFile(Set<RequestEntity> entities) throws IOException {
        String fileName = "navbar" + HTML_FILE_ENDING;
        File file = new File(String.valueOf(Files.createFile(Path.of(TMP_PATH + fileName))));
        FileWriter fileWriter = new FileWriter(file);
        try (PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println("<html lang=\"en\" xmlns:th=\"http://www.thymeleaf.org\">");
            printWriter.println("<div th:fragment=\"navbar\">");
            printWriter.println("<nav class=\"navbar navbar-expand-lg navbar-dark bg-primary\">");
            printWriter.println("<div class=\"container-fluid\">");
            printWriter.println("<a class=\"navbar-brand\" href=\"/\">Home</a>");
            printWriter.println("<div class=\"collapse navbar-collapse\">");
            printWriter.println("<ul class=\"navbar-nav\">");

            for (RequestEntity entity : entities) {
                printWriter.println("<li class=\"nav-item\">");
                printWriter.println("<a class=\"nav-link\" th:href=\"@{/" + plural(entity.getTitle().toLowerCase(Locale.ROOT)) +
                    "}\">" + plural(entity.getTitle()) + "</a>");
                printWriter.println("</li>");
            }
            printWriter.println("</ul>");
            printWriter.println("</div>");
            printWriter.println("</div>");
            printWriter.println("</nav>");
            printWriter.println("</div>");
            printWriter.println("</html>");
        }
        return file;
    }

    private File createOverviewFile(RequestEntity entity) throws IOException {
        String fileName = plural(entity.getEntityName().toLowerCase(Locale.ROOT)) + HTML_FILE_ENDING;
        File file = new File(String.valueOf(Files.createFile(Path.of(TMP_PATH + fileName))));
        FileWriter fileWriter = new FileWriter(file);
        try (PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println("<html lang=\"en\" xmlns:th=\"http://www.thymeleaf.org\">");

            printWriter.println("<div th:insert=\"main.html\"></div>");
            printWriter.println("<body>");
            printWriter.println("<div class=\"container-fluid\" style=\"margin-top: 10px\">");
            printWriter.println("<h1>" + plural(entity.getEntityName()) + "</h1> <br>");
            printWriter.println("<a th:href=\"@{/" + plural(entity.getTitle().toLowerCase(Locale.ROOT)) +
                    "/create}\"><input type=\"button\" class=\"btn btn-primary\" value=\"New\"\n" +
                    " style=\"margin-bottom: 10px\"></a>");

            printWriter.println("<table class=\"table table-bordered\">");

            printWriter.println("<tr>");
            for (EntityAttribute attribute : entity.getAttributes()) {
                printWriter.println("<th>" + attribute.getTitle() + "</th>");
            }
            printWriter.println("</tr>");


            printWriter.println("<tr th:each=\"" + entity.getEntityName().toLowerCase(Locale.ROOT)
                    + " : ${" + plural(entity.getEntityName().toLowerCase(Locale.ROOT)) + "}\">");

            for (EntityAttribute attribute : entity.getAttributes()) {
                printWriter.println("<td th:text=\"${" + entity.getEntityName().toLowerCase(Locale.ROOT) + ".get"
                        + capitalize(attribute.getTitle()) + "()}\"></td>");
                // TODO: Add Link for marked attribute.
                /*printWriter.println("<td><a th:href=\"@{'/teachers/edit/' + ${course.getTeacher().getId()}}\"\n" +
                        " th:text=\"${course.getTeacher().getName()}\"></a></td>");*/

                // TODO: Add size for related entities.
                //printWriter.println("<td th:text=\"${course.getStudents().size()}\"></td>");
            }

            printWriter.println("<!-- Buttons -->");
            printWriter.println("<td>");
            printWriter.println("<a th:href=\"@{'/" + plural(entity.getEntityName().toLowerCase(Locale.ROOT))
                    + "/edit/' + ${" + entity.getEntityName().toLowerCase(Locale.ROOT) + ".getId()}}\">");
            printWriter.println("<input type=\"button\" class=\"btn btn-light\" value=\"Edit\">");
            printWriter.println(" </a>");
            printWriter.println("<a th:href=\"@{'/" + plural(entity.getEntityName().toLowerCase(Locale.ROOT))
                    + "/delete/' + ${" + entity.getEntityName().toLowerCase(Locale.ROOT) + ".getId()}}\">");
            printWriter.println("<input type=\"button\" class=\"btn btn-danger\" value=\"Delete\">");
            printWriter.println("</a>");
            printWriter.println("</td>");
            printWriter.println("</tr>");
            printWriter.println("</table>");

            printWriter.println("<div th:insert=\"footer.html\">");
            printWriter.println("</div>");
            printWriter.println("</div>");
            printWriter.println("</body>");
        }
        return file;
    }

    private File createDetailsFile(RequestEntity entity) throws IOException {
        String fileName = entity.getEntityName().toLowerCase(Locale.ROOT) + "Details" + HTML_FILE_ENDING;
        File file = new File(String.valueOf(Files.createFile(Path.of(TMP_PATH + fileName))));
        FileWriter fileWriter = new FileWriter(file);
        try (PrintWriter printWriter = new PrintWriter(fileWriter)) {

            // Header
            printWriter.println("<html lang=\"en\" xmlns:th=\"http://www.thymeleaf.org\">");
            printWriter.println("<div th:insert=\"main.html\"></div>");
            printWriter.println("<body>");
            printWriter.println("<div class=\"container-fluid\" style=\"margin-top: 10px\">");

            // Update or Create
            printWriter.println("<h1 th:if=\"${" + entity.getTitle().toLowerCase(Locale.ROOT) +
                    ".getId() != null}\">Update an existing " + entity.getTitle() + "</h1>");
            printWriter.println("<h1 th:if=\"${" + entity.getTitle().toLowerCase(Locale.ROOT) +
                    ".getId() == null}\">Create a new " + entity.getTitle() + "</h1>");

            printWriter.println("<form th:action=\"@{'/" + plural(entity.getTitle().toLowerCase(Locale.ROOT)) +
                    "/save'}\" th:object=\"${" + entity.getTitle().toLowerCase(Locale.ROOT) + "}\" method=\"post\">");
            printWriter.println("<input type=\"hidden\" name=\"id\" th:value=\"${" +
                    entity.getTitle().toLowerCase(Locale.ROOT) + ".getId()}\">");

            for (EntityAttribute attribute : entity.getAttributes()) {
                printWriter.println("<label for=\"" + attribute.getTitle().toLowerCase(Locale.ROOT) + "\">" +
                        capitalize(attribute.getTitle().toLowerCase(Locale.ROOT)) + "</label>");
                DataType dataType = DataType.fromName(attribute.getDataType());
                printWriter.println("<input type=\"" + dataType.getInputType() + "\" id=\"" + attribute.getTitle().toLowerCase(Locale.ROOT) + "\" name=\"" +
                        attribute.getTitle().toLowerCase(Locale.ROOT) + "\" class=\"input-group\" th:value=\"${" +
                        entity.getTitle().toLowerCase(Locale.ROOT) + ".get" +
                        capitalize(attribute.getTitle().toLowerCase(Locale.ROOT)) + "()}\">");
                printWriter.println("<br>");
            }

            printWriter.println("<a th:href=\"@{/" + plural(entity.getTitle().toLowerCase(Locale.ROOT)) +
                    "}\"><input type=\"button\" class=\"btn btn-warning\" value=\"Cancel\"></a>");

            printWriter.println("<input type=\"submit\" class=\"btn btn-primary\" value=\"Save\"> <br>");

            printWriter.println("</form>");
            printWriter.println("<div th:insert=\"footer.html\">");
            printWriter.println("</div>");
            printWriter.println("</div>");
            printWriter.println("</body>");

        }
        return file;
    }

}
