package cn.com.mustache.plugins.restful.finder.resolver;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import cn.com.mustache.plugins.restful.finder.constant.HttpMethodEnum;
import cn.com.mustache.plugins.restful.finder.constant.RestfulException;
import cn.com.mustache.plugins.restful.finder.constant.SpringEnum;
import cn.com.mustache.plugins.restful.finder.contract.RestfulItem;
import cn.com.mustache.plugins.restful.finder.uitl.PsiUtil;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiLiteralExpression;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiNameValuePair;
import com.intellij.psi.PsiPolyadicExpression;
import com.intellij.psi.search.GlobalSearchScope;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

/**
 * @author steven
 */
public class SpringResolver implements BaseResolver {

    private boolean multiModule = false;

    public List<RestfulItem> findRestfulAll(Module module) {
        GlobalSearchScope globalSearchScope = GlobalSearchScope.moduleScope(module);
        List<RestfulItem> list = getRestServiceItemList(module, globalSearchScope);
        return new ArrayList<>(list);
    }

    public List<RestfulItem> findRestfulAll(Project project) {
        List<RestfulItem> itemList = new ArrayList<>();
        assert project != null;
        Module[] modules = ModuleManager.getInstance(project).getModules();
        multiModule = modules.length > 1;
        for (Module module : modules) {
            GlobalSearchScope globalSearchScope = GlobalSearchScope.moduleScope(module);
            List<RestfulItem> list = getRestServiceItemList(module, globalSearchScope);
            itemList.addAll(list);
        }
        return itemList;
    }

    private List<RestfulItem> getRestServiceItemList(Module module, GlobalSearchScope globalSearchScope) {
        List<RestfulItem> apiList = new ArrayList<>();
        Set<PsiClass> setClass = PsiUtil.getProjectSetClass(module.getProject(), globalSearchScope);
        for (PsiClass psiClass : setClass) {
            this.handleSelectClass(module, psiClass, apiList);
        }
        return apiList;
    }

    private void handleSelectClass(Module module, PsiClass selectedClass, List<RestfulItem> apiList) {
        PsiMethod[] psiMethods = selectedClass.getMethods();
        for (PsiMethod psiMethodTarget : psiMethods) {
            //去除私有方法
            try {
                if (!psiMethodTarget.getModifierList().hasModifierProperty(PsiModifier.PRIVATE)) {
                    handleActionInfo(module, selectedClass, apiList, psiMethodTarget);
                }
            } catch (Exception ex) {
                throw new RestfulException(100, ex);
            }
        }
    }

    private void handleActionInfo(Module module, PsiClass selectedClass, List<RestfulItem> apiList, PsiMethod psiMethodTarget) {
        List<RestfulItem> apis = actionPerformed(module, selectedClass, psiMethodTarget);
        if (CollectionUtils.isEmpty(apis)) {
            return;
        }
        List<RestfulItem> filterApis = apis.stream().filter(api -> StringUtils.isNotBlank(api.getPath())).collect(Collectors.toList());
        apiList.addAll(filterApis);
    }

    private List<RestfulItem> actionPerformed(Module module, PsiClass selectedClass, PsiMethod psiMethodTarget) {
        RestfulItem restfulItem = new RestfulItem();
        // Gets the value in the RequestMapping above the class
        Set<String> pathClass = getPathClass(selectedClass);
        Set<String> pathMethod = handlePsiAnnotationMethod(psiMethodTarget, restfulItem);
        if (pathMethod == null) {
            return null;
        }

        List<RestfulItem> methodApis = new ArrayList<>();
        pathClass.stream().flatMap(c -> pathMethod.stream().map(m -> this.handlePath(c, m))).forEach(item -> {
            RestfulItem api = new RestfulItem();
            api.setPath(item.replace("//", "/"));
            api.setMethod(restfulItem.getMethod());
            api.setPsiClass(selectedClass);
            api.setPsiMethod(psiMethodTarget);
            api.setPsiElement(psiMethodTarget);
            api.setNavigationElement(psiMethodTarget);
            api.setModule(multiModule ? module : null);
            methodApis.add(api);
        });
        return methodApis;
    }

    private String handlePath(String pathClass, String pathMethod) {
        String path = Paths.get(pathClass, pathMethod).toString();
        if (!path.startsWith("/")) {
            path = String.format("/%s", path);
        }
        return path.replaceAll("//+", "/").replace("\\", "/");
    }

    private Set<String> handlePsiAnnotationMethod(PsiMethod psiMethodTarget, RestfulItem restfulItem) {
        PsiAnnotation psiAnnotation = PsiUtil.findRestAnnotationSort(psiMethodTarget.getAnnotations());
        if (psiAnnotation == null) {
            return null;
        }
        if (restfulItem != null) {
            this.handleMethod(psiAnnotation, restfulItem);
        }
        return handleRequestMappingPaths(psiAnnotation);
    }

    private void handleMethod(PsiAnnotation psiAnnotation, RestfulItem restfulItem) {
        SpringEnum.Rest restEnum = SpringEnum.Rest.getByAnnotation(psiAnnotation.getQualifiedName());
        if (restEnum.getMethod().equals(HttpMethodEnum.UNKNOWN)) {
            PsiAnnotationMemberValue psiAnnotationMemberValue = psiAnnotation.findAttributeValue("method");
            if (psiAnnotationMemberValue != null && psiAnnotationMemberValue.getLastChild() != null) {
                restfulItem.setMethod(HttpMethodEnum.parse(psiAnnotationMemberValue.getLastChild().getText()));
            } else {
                restfulItem.setMethod(HttpMethodEnum.GET);
            }
            return;
        }
        restfulItem.setMethod(restEnum.getMethod());
    }

    private Set<String> getPathClass(PsiClass psiClass) {
        PsiAnnotation psiAnnotation = PsiUtil.findAnnotation(psiClass, SpringEnum.Rest.REQUEST_MAPPING.getAnnotation());
        Set<String> pathClass = handleRequestMappingPaths(psiAnnotation);
        if (pathClass.isEmpty()) {
            pathClass.add("/");
        }
        return pathClass;
    }

    @NotNull
    private Set<String> handleRequestMappingPaths(PsiAnnotation psiAnnotation) {
        Set<String> path = new HashSet<>();
        if (psiAnnotation != null) {
            PsiNameValuePair[] psiNameValuePairs = psiAnnotation.getParameterList().getAttributes();
            if (psiNameValuePairs.length > 0) {
                if (psiNameValuePairs[0].getLiteralValue() != null) {
                    path.add(psiNameValuePairs[0].getLiteralValue());
                } else {
                    PsiAnnotationMemberValue psiAnnotationMemberValue = psiAnnotation.findAttributeValue("value");
                    if (psiAnnotationMemberValue != null) {
                        Arrays.stream(psiAnnotationMemberValue.getChildren())
                                .filter(it -> it instanceof PsiLiteralExpression)
                                .forEach(it -> path.add(Objects.requireNonNull(((PsiLiteralExpression) it).getValue()).toString()));
                        Arrays.stream(psiAnnotationMemberValue.getChildren())
                                .filter(it -> it instanceof PsiPolyadicExpression)
                                .forEach(it -> {
                                    concatPath(path, (PsiPolyadicExpression) it);
                                });
                    }
                }
            }
        }
        return path;
    }

    private void concatPath(Set<String> path, PsiPolyadicExpression it) {
        StringBuilder stringBuilder = new StringBuilder();
        for (PsiExpression operand : it.getOperands()) {
            if (operand instanceof PsiLiteralExpression) {
                stringBuilder.append(((PsiLiteralExpression) operand).getValue());
            }
        }
        path.add(stringBuilder.toString());
    }
}