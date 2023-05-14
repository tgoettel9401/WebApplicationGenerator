<#-- @ftlvariable name="packageName" type="java.lang.String" -->
package ${packageName};

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class MainView extends VerticalLayout {

    public MainView() {
        add(new Navigation());
    }

}
