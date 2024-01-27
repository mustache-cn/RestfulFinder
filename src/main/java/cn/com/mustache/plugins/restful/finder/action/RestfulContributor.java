package cn.com.mustache.plugins.restful.finder.action;

import java.util.List;
import java.util.Objects;

import cn.com.mustache.plugins.restful.finder.contract.RestfulItem;
import cn.com.mustache.plugins.restful.finder.resolver.SpringResolver;
import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * @author steven
 */
public class RestfulContributor implements ChooseByNameContributor {

    Module myModule;

    private List<RestfulItem> navItem;

    public RestfulContributor(Module myModule) {
        this.myModule = myModule;
    }

    @NotNull
    @Override
    public String @NotNull [] getNames(Project project, boolean onlyThisModuleChecked) {
        List<RestfulItem> itemList = onlyThisModuleChecked && myModule != null ?
                new SpringResolver().findRestfulAll(myModule) :
                new SpringResolver().findRestfulAll(project);
        navItem = itemList;
        return itemList.stream()
                .map(RestfulItem::getName)
                .filter(Objects::nonNull)
                .toArray(String[]::new);
    }

    @NotNull
    @Override
    public NavigationItem @NotNull [] getItemsByName(String name,
                                                     String pattern,
                                                     Project project,
                                                     boolean onlyThisModuleChecked) {
        return navItem.stream()
                .filter(item -> name.equals(item.getName()))
                .toArray(NavigationItem[]::new);
    }
}
