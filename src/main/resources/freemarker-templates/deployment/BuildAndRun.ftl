<#-- Variables of DataModel (for autocompletion of IDE)-->
<#-- @ftlvariable name="buildCommand" type="java.lang.String" -->
<#-- @ftlvariable name="imageName" type="java.lang.String" -->
${buildCommand}
docker build --platform=linux/arm64 -t ${imageName} .
docker run -p 8080:8080 ${imageName}
