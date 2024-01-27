package cn.com.mustache.plugins.restful.finder.action;

import java.util.Collection;

import cn.com.mustache.plugins.restful.finder.constant.HttpMethodEnum;
import cn.com.mustache.plugins.restful.finder.contract.RestfulItem;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.ide.util.gotoByName.CustomMatcherModel;
import com.intellij.ide.util.gotoByName.FilteringGotoByModel;
import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.psi.codeStyle.MinusculeMatcher;
import com.intellij.psi.codeStyle.NameUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author steven
 */
public class RestfulModel extends FilteringGotoByModel<HttpMethodEnum> implements DumbAware, CustomMatcherModel {

    protected RestfulModel(@NotNull Project project,
                           @NotNull ChooseByNameContributor[] contributors) {
        super(project, contributors);
    }

    @Nullable
    @Override
    protected HttpMethodEnum filterValueFor(NavigationItem item) {
        if (item instanceof RestfulItem) {
            return ((RestfulItem) item).getMethod();
        }
        return null;
    }

    @Nullable
    @Override
    protected synchronized Collection<HttpMethodEnum> getFilterItems() {
        return super.getFilterItems();
    }

    @Override
    public String getPromptText() {
        return "Enter service URL path :";
    }

    @NotNull
    @Override
    public String getNotInMessage() {
        return "Not find in project";
    }

    @NotNull
    @Override
    public String getNotFoundMessage() {
        return "Not find in project";
    }

    @Override
    public boolean loadInitialCheckBoxState() {
        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance(myProject);
        return propertiesComponent.isTrueValue("GoToRestService.OnlyCurrentModule");
    }

    @Override
    public void saveInitialCheckBoxState(boolean state) {
        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance(myProject);
        if (propertiesComponent.isTrueValue("GoToRestService.OnlyCurrentModule")) {
            propertiesComponent.setValue(
                    "GoToRestService.OnlyCurrentModule",
                    Boolean.toString(state)
            );
        }
    }

    @Nullable
    @Override
    public String getFullName(@NotNull Object element) {
        return getElementName(element);
    }

    @NotNull
    @Override
    public String @NotNull [] getSeparators() {
        return new String[]{"/", "?"};
    }

    @Nullable
    @Override
    public String getCheckBoxName() {
        return "Only this module";
    }

    @Override
    public boolean willOpenEditor() {
        return true;
    }

    @Override
    public boolean matches(@NotNull String popupItem, @NotNull String userPattern) {
        if (userPattern.equals("/")) {
            return true;
        }

        MinusculeMatcher matcher = NameUtil.buildMatcher(
                "*" + userPattern,
                NameUtil.MatchingCaseSensitivity.NONE
        );
        return matcher.matches(popupItem);
    }

    @NotNull
    @Override
    public String removeModelSpecificMarkup(@NotNull String pattern) {
        return super.removeModelSpecificMarkup(pattern);
    }

}
