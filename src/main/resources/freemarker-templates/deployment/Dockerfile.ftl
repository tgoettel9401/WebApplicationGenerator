<#-- Variables of DataModel (for autocompletion of IDE)-->
<#-- @ftlvariable name="baseImage" type="java.lang.String" -->
FROM ${baseImage}
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
