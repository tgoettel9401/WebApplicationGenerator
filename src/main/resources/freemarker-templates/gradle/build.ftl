<#-- @ftlvariable name="groupId" type="java.lang.String" -->
<#-- @ftlvariable name="artifactId" type="java.lang.String" -->
<#-- @ftlvariable name="projectTitle" type="java.lang.String" -->
<#-- @ftlvariable name="projectDescription" type="java.lang.String" -->
<#-- @ftlvariable name="springBootVersion" type="java.lang.String" -->
<#-- @ftlvariable name="springDependencyManagementVersion" type="java.lang.String" -->
<#-- @ftlvariable name="javaVersion" type="java.lang.String" -->
<#-- @ftlvariable name="dependencies" type="java.util.List<org.dhbw.webapplicationgenerator.generator.backend.java.buildtool.Dependency>" -->
<#-- @ftlvariable name="dependencyManagementNeeded" type="java.lang.Boolean" -->
<#-- @ftlvariable name="plugins" type="java.util.List<org.dhbw.webapplicationgenerator.generator.backend.java.buildtool.Plugin>" -->
plugins {
    id 'java'
    id 'org.springframework.boot' version '${springBootVersion}'
    id 'io.spring.dependency-management' version '${springDependencyManagementVersion}'
    <#list plugins as plugin>
    id '${plugin.getArtifactId()}' version '${plugin.getVersion()}'
    </#list>
}

group = '${groupId}'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '${javaVersion}'

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter';
    testImplementation 'org.springframework.boot:spring-boot-starter-test';
    <#list dependencies as dependency>
    ${dependency.getGradleLine()}
    </#list>
}

<#if dependencyManagementNeeded>
dependencyManagement {
    imports {
        <#list dependencies as dependency>
            <#if dependency.isDependencyManagement()>
        mavenBom "${dependency.getGroupId()}:${dependency.getArtifactId()}:${dependency.getVersion()}"
            </#if>
        </#list>
    }
}
</#if>


tasks.named('test') {
    useJUnitPlatform()
}
