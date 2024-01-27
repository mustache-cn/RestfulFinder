package cn.com.mustache.plugins.restful.finder.uitl;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import cn.com.mustache.plugins.restful.finder.constant.SpringEnum;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.impl.java.stubs.index.JavaAnnotationIndex;
import com.intellij.psi.search.GlobalSearchScope;

/**
 * @author steven
 */
public class PsiUtil {

    public static PsiAnnotation findAnnotation(PsiClass psiClass, String annotation) {
        return Arrays.stream(psiClass.getAnnotations())
                .filter(an -> an.hasQualifiedName(annotation))
                .findFirst()
                .orElse(null);
    }

    public static PsiAnnotation findRestAnnotationSort(PsiAnnotation[] psiAnnotation) {
        return Arrays.stream(psiAnnotation)
                .filter(an -> SpringEnum.Rest.existsByAnnotation(an.getQualifiedName()))
                .findFirst().orElse(null);
    }

    public static Set<PsiClass> getProjectSetClass(Project project, GlobalSearchScope globalSearchScope) {
        Set<PsiClass> setClass = new HashSet<>();
        SpringEnum.Controller[] controllers = SpringEnum.Controller.values();
        for (SpringEnum.Controller controller : controllers) {
            Collection<PsiAnnotation> psiAnnotations = JavaAnnotationIndex.getInstance().get(controller.getShortName(), project, globalSearchScope);
            for (PsiAnnotation psiAnnotation : psiAnnotations) {
                PsiModifierList psiModifierList = (PsiModifierList) psiAnnotation.getParent();
                PsiElement psiElement = psiModifierList.getParent();
                PsiClass psiClass = (PsiClass) psiElement;
                setClass.add(psiClass);
            }
        }
        return setClass;
    }
}
