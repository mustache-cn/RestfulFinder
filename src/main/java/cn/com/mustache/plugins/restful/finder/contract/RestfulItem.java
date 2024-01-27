package cn.com.mustache.plugins.restful.finder.contract;

import javax.swing.*;

import cn.com.mustache.plugins.restful.finder.constant.HttpMethodEnum;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.Nullable;

/**
 * @author steven
 */
public class RestfulItem implements NavigationItem {

    /**
     * Rest path
     */
    private String path;

    /**
     * Request method
     */
    private HttpMethodEnum method;

    /**
     * module
     */
    private Module module;

    private PsiMethod psiMethod;

    private PsiClass psiClass;

    private PsiElement psiElement;

    private Navigatable navigationElement;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public HttpMethodEnum getMethod() {
        return method;
    }

    public void setMethod(HttpMethodEnum method) {
        this.method = method;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public PsiMethod getPsiMethod() {
        return psiMethod;
    }

    public void setPsiMethod(PsiMethod psiMethod) {
        this.psiMethod = psiMethod;
    }

    public PsiClass getPsiClass() {
        return psiClass;
    }

    public void setPsiClass(PsiClass psiClass) {
        this.psiClass = psiClass;
    }

    public Navigatable getNavigationElement() {
        return navigationElement;
    }

    public void setNavigationElement(Navigatable navigationElement) {
        this.navigationElement = navigationElement;
    }

    @Override
    public @Nullable @NlsSafe String getName() {
        return this.path;
    }

    public PsiElement getPsiElement() {
        return psiElement;
    }

    public void setPsiElement(PsiElement psiElement) {
        this.psiElement = psiElement;
    }

    @Override
    public @Nullable ItemPresentation getPresentation() {
        return new RestfulItemPresentation();
    }

    @Override
    public void navigate(boolean b) {
        if (navigationElement != null) {
            navigationElement.navigate(b);
        }
    }

    @Override
    public boolean canNavigate() {
        return navigationElement.canNavigate();
    }

    @Override
    public boolean canNavigateToSource() {
        return true;
    }

    private class RestfulItemPresentation implements ItemPresentation {

        @Nullable
        @Override
        public String getPresentableText() {
            return path;
        }

        @Nullable
        @Override
        public String getLocationString() {
            String location;
            if (module != null) {
                location = String.format("%s#%s#%s", module.getName(), psiClass.getName(), psiMethod.getName());
            } else {
                location = String.format("%s#%s", psiClass.getName(), psiMethod.getName());
            }
            return "(" + location + ")";
        }


        @Nullable
        @Override
        public Icon getIcon(boolean unused) {
            return method.getIcon();
        }
    }
}
