<#-- @ftlvariable name="packageName" type="java.lang.String" -->
package ${packageName};

public class NotFoundException extends Exception {
    public NotFoundException(String title, Long id) {
        super(title + "with ID " + id + " has not been found");
    }
}
