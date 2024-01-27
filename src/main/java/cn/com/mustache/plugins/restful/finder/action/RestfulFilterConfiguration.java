package cn.com.mustache.plugins.restful.finder.action;

import cn.com.mustache.plugins.restful.finder.constant.HttpMethodEnum;
import com.intellij.ide.util.gotoByName.ChooseByNameFilterConfiguration;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import com.intellij.openapi.project.Project;

/**
 * @author steven
 */
@State(name = "RestfulFilterConfiguration", storages = @Storage(StoragePathMacros.WORKSPACE_FILE))
class RestfulFilterConfiguration extends ChooseByNameFilterConfiguration<HttpMethodEnum> {

    public static RestfulFilterConfiguration getInstance(Project project) {
        return project.getService(RestfulFilterConfiguration.class);
    }

    @Override
    protected String nameForElement(HttpMethodEnum type) {
        return type.name();
    }
}
