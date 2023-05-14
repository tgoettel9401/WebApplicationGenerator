<#-- @ftlvariable name="groupId" type="java.lang.String" -->
<#-- @ftlvariable name="artifactId" type="java.lang.String" -->
<#-- @ftlvariable name="projectTitle" type="java.lang.String" -->
<#-- @ftlvariable name="projectDescription" type="java.lang.String" -->
<#-- @ftlvariable name="springBootVersion" type="java.lang.String" -->
<#-- @ftlvariable name="javaVersion" type="java.lang.String" -->
<#-- @ftlvariable name="dependencies" type="java.util.List<org.dhbw.webapplicationgenerator.generator.backend.java.buildtool.Dependency>" -->
<#-- @ftlvariable name="dependencyManagementNeeded" type="java.lang.Boolean" -->
<#-- @ftlvariable name="plugins" type="java.util.List<org.dhbw.webapplicationgenerator.generator.backend.java.buildtool.Plugin>" -->
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>${springBootVersion}</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>${groupId}</groupId>
    <artifactId>${artifactId}</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>${projectTitle}</name>
    <description>${projectDescription}</description>
    <properties>
        <java.version>${javaVersion}</java.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <#list dependencies as dependency>
            <#if !dependency.isDependencyManagement()>
        <dependency>
            <groupId>${dependency.getGroupId()}</groupId>
            <artifactId>${dependency.getArtifactId()}</artifactId>
            <#if dependency.getVersion() != "">
                <version>${dependency.getVersion()}</version>
            </#if>
            <#if dependency.getType() != "">
                <type>${dependency.getType()}</type>
            </#if>
            <#if dependency.getScope() != "">
                <scope>${dependency.getScope()}</scope>
            </#if>
        </dependency>
            </#if>
        </#list>
    </dependencies>

    <#if dependencyManagementNeeded>
    <dependencyManagement>
        <dependencies>
            <#list dependencies as dependency>
                <#if dependency.isDependencyManagement()>
            <dependency>
                <groupId>${dependency.getGroupId()}</groupId>
                <artifactId>${dependency.getArtifactId()}</artifactId>
                <#if dependency.getVersion() != "">
                    <version>${dependency.getVersion()}</version>
                </#if>
                <#if dependency.getType() != "">
                    <type>${dependency.getType()}</type>
                </#if>
                <#if dependency.getScope() != "">
                    <scope>${dependency.getScope()}</scope>
                </#if>
            </dependency>
                </#if>
            </#list>
        </dependencies>
    </dependencyManagement>
    </#if>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
            <#list plugins as plugin>
            <plugin>
                <groupId>${plugin.getGroupId()}</groupId>
                <artifactId>${plugin.getArtifactId()}</artifactId>
                <version>${plugin.getVersion()}</version>
                <executions>
                    <#list plugin.getExecutions() as execution>
                    <execution>
                        <goals>
                        <#list execution.getGoals() as goal>
                            <goal>${goal.getCommand()}</goal>
                        </#list>
                        </goals>
                    </#list>
                    </execution>
                </executions>
            </plugin>
            </#list>
        </plugins>
    </build>

</project>
