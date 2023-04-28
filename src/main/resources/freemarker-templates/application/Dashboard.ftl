<#-- @ftlvariable name="entities" type="java.util.Set<org.dhbw.webapplicationgenerator.webclient.request.RequestEntity>" -->
<html lang="en" xmlns:th="http://www.thymeleaf.org">

<div th:insert="main.html"></div>
<body>
    <div class="container-fluid" style="margin-top: 10px">
    <h1>Dashboard</h1>
    <br>
    <ul>
        <#list entities as entity>
        <li>
            <a th:href="@{${entity.getNamePlural()}}">${entity.getTitlePlural()}</a>
        </li>
        </#list>
    </ul>
    </div>
    <div th:insert="footer.html"></div>
</body>
