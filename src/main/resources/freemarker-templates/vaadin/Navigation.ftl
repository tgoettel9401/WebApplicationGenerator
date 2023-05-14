<#-- @ftlvariable name="packageName" type="java.lang.String" -->
<#-- @ftlvariable name="entities" type="java.util.List<org.dhbw.webapplicationgenerator.model.request.datamodel.Entity>" -->
package ${packageName};

import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class Navigation extends VerticalLayout {

    public Navigation() {
        MenuBar menuBar = new MenuBar();

        <#list entities as entity>
        MenuItem ${entity.getNamePlural()}Item = menuBar.addItem("${entity.getTitlePlural()}");
        ${entity.getNamePlural()}Item.addClickListener((event) -> navigate("${entity.getNamePlural()}"));
        </#list>

        add(menuBar);
    }

    private void navigate(String path) {
        this.removeAll();
        this.getUI().get().navigate(path);
    }

}
