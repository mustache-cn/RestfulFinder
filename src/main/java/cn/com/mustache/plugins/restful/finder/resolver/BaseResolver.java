package cn.com.mustache.plugins.restful.finder.resolver;

import java.util.List;

import cn.com.mustache.plugins.restful.finder.contract.RestfulItem;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;

/**
 * @author steven
 */
public interface BaseResolver {
    List<RestfulItem> findRestfulAll(Module module);

    List<RestfulItem> findRestfulAll(Project project);
}
