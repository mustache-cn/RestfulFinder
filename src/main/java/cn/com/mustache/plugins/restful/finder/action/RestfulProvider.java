package cn.com.mustache.plugins.restful.finder.action;

import java.util.List;

import cn.com.mustache.plugins.restful.finder.uitl.StringUtil;
import com.intellij.ide.util.gotoByName.ChooseByNameViewModel;
import com.intellij.ide.util.gotoByName.DefaultChooseByNameItemProvider;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.psi.PsiElement;
import com.intellij.util.Processor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author steven
 */
public class RestfulProvider extends DefaultChooseByNameItemProvider {

    public RestfulProvider(@Nullable PsiElement context) {
        super(context);
    }

    @NotNull
    @Override
    public List<String> filterNames(@NotNull ChooseByNameViewModel base,
                                    @NotNull String @NotNull [] names,
                                    @NotNull String pattern) {
        return super.filterNames(base, names, pattern);
    }

    @Override
    public boolean filterElements(@NotNull ChooseByNameViewModel base,
                                  @NotNull String pattern,
                                  boolean everywhere,
                                  @NotNull ProgressIndicator indicator,
                                  @NotNull Processor<Object> consumer) {
        pattern = StringUtil.removeRedundancyMarkup(pattern);
        return super.filterElements(base, pattern, everywhere, indicator, consumer);
    }
}
