<#-- @ftlvariable name="packageName" type="java.lang.String" -->
<#-- @ftlvariable name="imports" type="java.util.List<java.lang.String>" -->
<#-- @ftlvariable name="entities" type="java.util.Set<org.dhbw.webapplicationgenerator.webclient.request.RequestEntity>" -->
package ${packageName};

<#list imports as import>
import ${import};
</#list>

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("dashboard");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/register").setViewName("register");
        registry.addViewController("/users").setViewName("users");
        <#list entities as entity>
        registry.addViewController("/${entity.getNamePlural()}").setViewName("${entity.getNamePlural()}");
        </#list>
    }
}
