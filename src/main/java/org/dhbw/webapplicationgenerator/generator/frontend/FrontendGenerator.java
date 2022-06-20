package org.dhbw.webapplicationgenerator.generator.frontend;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.Project;
import org.dhbw.webapplicationgenerator.generator.base_project.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.generator.entity.DataType;
import org.dhbw.webapplicationgenerator.generator.model.ProjectDirectory;
import org.dhbw.webapplicationgenerator.util.ResourceFileHelper;
import org.dhbw.webapplicationgenerator.webclient.request.CreationRequest;
import org.dhbw.webapplicationgenerator.webclient.request.EntityAttribute;
import org.dhbw.webapplicationgenerator.webclient.request.EntityRelation;
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
import java.util.stream.Collectors;

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
                printWriter.println("<a class=\"nav-link\" th:href=\"@{/" + plural(entity.getName().toLowerCase(Locale.ROOT)) +
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
        String fileName = plural(entity.getName().toLowerCase(Locale.ROOT)) + HTML_FILE_ENDING;
        File file = new File(String.valueOf(Files.createFile(Path.of(TMP_PATH + fileName))));
        FileWriter fileWriter = new FileWriter(file);
        try (PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println("<html lang=\"en\" xmlns:th=\"http://www.thymeleaf.org\">");

            printWriter.println("<div th:insert=\"main.html\"></div>");
            printWriter.println("<body>");
            printWriter.println("<div class=\"container-fluid\" style=\"margin-top: 10px\">");
            printWriter.println("<h1>" + plural(entity.getName()) + "</h1> <br>");
            printWriter.println("<a th:href=\"@{/" + plural(entity.getName().toLowerCase(Locale.ROOT)) +
                    "/create}\"><input type=\"button\" class=\"btn btn-primary\" value=\"New\"\n" +
                    " style=\"margin-bottom: 10px\"></a>");

            printWriter.println("<table class=\"table table-bordered\">");

            printWriter.println("<tr>");
            for (EntityAttribute attribute : entity.getAttributes()) {
                printWriter.println("<th>" + attribute.getTitle() + "</th>");
            }
            printWriter.println("</tr>");


            printWriter.println("<tr th:each=\"" + entity.getName().toLowerCase(Locale.ROOT)
                    + " : ${" + plural(entity.getName().toLowerCase(Locale.ROOT)) + "}\">");

            for (EntityAttribute attribute : entity.getAttributes()) {
                printWriter.println("<td th:text=\"${" + entity.getName().toLowerCase(Locale.ROOT) + ".get"
                        + capitalize(attribute.getName()) + "()}\"></td>");
            }

            printWriter.println("<!-- Buttons -->");
            printWriter.println("<td>");
            printWriter.println("<a th:href=\"@{'/" + plural(entity.getName().toLowerCase(Locale.ROOT))
                    + "/edit/' + ${" + entity.getName().toLowerCase(Locale.ROOT) + ".getId()}}\">");
            printWriter.println("<input type=\"button\" class=\"btn btn-light\" value=\"Edit\">");
            printWriter.println(" </a>");
            printWriter.println("<a th:href=\"@{'/" + plural(entity.getName().toLowerCase(Locale.ROOT))
                    + "/delete/' + ${" + entity.getName().toLowerCase(Locale.ROOT) + ".getId()}}\">");
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
        String fileName = entity.getName().toLowerCase(Locale.ROOT) + "Details" + HTML_FILE_ENDING;
        File file = new File(String.valueOf(Files.createFile(Path.of(TMP_PATH + fileName))));
        FileWriter fileWriter = new FileWriter(file);
        try (PrintWriter printWriter = new PrintWriter(fileWriter)) {

            // Header
            printWriter.println("<html lang=\"en\" xmlns:th=\"http://www.thymeleaf.org\">");
            printWriter.println("<div th:insert=\"main.html\"></div>");
            printWriter.println("<body>");
            printWriter.println("<div class=\"container-fluid\" style=\"margin-top: 10px\">");

            // Update or Create
            printWriter.println("<h1 th:if=\"${" + entity.getName().toLowerCase(Locale.ROOT) +
                    ".getId() != null}\">Update an existing " + entity.getTitle() + "</h1>");
            printWriter.println("<h1 th:if=\"${" + entity.getName().toLowerCase(Locale.ROOT) +
                    ".getId() == null}\">Create a new " + entity.getTitle() + "</h1>");

            printWriter.println("<form th:action=\"@{'/" + plural(entity.getName().toLowerCase(Locale.ROOT)) +
                    "/save'}\" th:object=\"${" + entity.getName().toLowerCase(Locale.ROOT) + "}\" method=\"post\">");
            printWriter.println("<input type=\"hidden\" name=\"id\" th:value=\"${" +
                    entity.getName().toLowerCase(Locale.ROOT) + ".getId()}\">");

            for (EntityAttribute attribute : entity.getAttributes()) {
                printWriter.println("<label for=\"" + attribute.getName() + "\">" +
                        capitalize(attribute.getName()) + "</label>");
                DataType dataType = DataType.fromName(attribute.getDataType());
                printWriter.println("<input type=\"" + dataType.getInputType() + "\" id=\"" + attribute.getName().toLowerCase(Locale.ROOT) + "\" name=\"" +
                        attribute.getName().toLowerCase(Locale.ROOT) + "\" class=\"input-group\" th:value=\"${" +
                        entity.getName().toLowerCase(Locale.ROOT) + ".get" +
                        capitalize(attribute.getName().toLowerCase(Locale.ROOT)) + "()}\">");
                printWriter.println("<br>");
                printWriter.println("");
            }

            // For all toOne relation (hence not toMany) we add a simple select to choose the correct option.
            for (EntityRelation relation : entity.getRelations().stream().filter(relation -> !relation.getRelationType().isToMany()).collect(Collectors.toList())) {
                printWriter.println("<label for=\"" + relation.getEntity() + "\">" + capitalize(relation.getEntity()) + "</label>");
                printWriter.println("<select id=\"" + relation.getEntity() + "\" name=\"" + relation.getEntity() +
                        "Id" + "\" class=\"form-control\" th:value=\"${" + entity.getName() + ".get" +
                        capitalize(relation.getEntity()) + "()}\">");
                printWriter.println("<option th:value=\"null\">none</option>");
                printWriter.println("<option " +
                        "th:each=\"" + relation.getEntity() + " : ${" + plural(relation.getEntity()) + "}\" " +
                        "th:text=\"${" + relation.getEntity() + ".get" + capitalize(relation.getEntityObject().getReferenceAttribute().getName()) + "()}\" " +
                        "th:value=\"${" + relation.getEntity() + ".getId()}\" " +
                        "th:selected=\"${" + relation.getEntity() + ".equals(" + entity.getName() + ".get" + capitalize(relation.getEntity()) + "())}\")>" +
                        "</option>");
                printWriter.println("</select>");

                printWriter.println("<br>");
                printWriter.println("");
            }

            // For all toMany relation we add a multiple select to choose multiple options.
            for (EntityRelation relation : entity.getRelations().stream().filter(relation -> relation.getRelationType().isToMany()).collect(Collectors.toList())) {
                printWriter.println("<label for=\"" + plural(relation.getEntity()) + "\">" + capitalize(plural(relation.getEntity())) + "</label>");
                printWriter.println("<select multiple id=\"" + plural(relation.getEntity()) + "\" name=\"" + relation.getEntity() +
                        "Ids" + "\" class=\"form-control\" th:value=\"${" + entity.getName() + ".get" +
                        capitalize(plural(relation.getEntity())) + "()}\">");
                printWriter.println("<option th:value=\"null\">none</option>");
                printWriter.println("<option " +
                        "th:each=\"" + relation.getEntity() + " : ${" + plural(relation.getEntity()) + "}\" " +
                        "th:text=\"${" + relation.getEntity() + ".get" + capitalize(relation.getEntityObject().getReferenceAttribute().getName()) + "()}\" " +
                        "th:value=\"${" + relation.getEntity() + ".getId()}\" " +
                        "th:selected=\"${" + entity.getName() + ".get" + capitalize(plural(relation.getEntity())) + ".contains(" + relation.getEntity() + ")}\")>" +
                        "</option>");
                printWriter.println("</select>");

                printWriter.println("<br>");
                printWriter.println("");

            }

            printWriter.println("<a th:href=\"@{/" + plural(entity.getName().toLowerCase(Locale.ROOT)) +
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
