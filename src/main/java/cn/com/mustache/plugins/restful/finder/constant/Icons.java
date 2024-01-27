package cn.com.mustache.plugins.restful.finder.constant;

import javax.swing.*;

import com.intellij.openapi.util.IconLoader;

/**
 * @author steven
 */
public interface Icons {

    Icon GET = IconLoader.getIcon("/icons/G.svg", Icons.class);

    Icon POST = IconLoader.getIcon("/icons/P.svg", Icons.class);

    Icon PUT = IconLoader.getIcon("/icons/P2.svg", Icons.class);

    Icon PATCH = IconLoader.getIcon("/icons/P3.svg", Icons.class);

    Icon DELETE = IconLoader.getIcon("/icons/D.svg", Icons.class);

    Icon UNKNOWN = IconLoader.getIcon("/icons/U.svg", Icons.class);

}
