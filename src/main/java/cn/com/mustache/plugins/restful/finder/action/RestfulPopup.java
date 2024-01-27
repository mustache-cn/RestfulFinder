package cn.com.mustache.plugins.restful.finder.action;

import com.intellij.ide.util.gotoByName.ChooseByNameItemProvider;
import com.intellij.ide.util.gotoByName.ChooseByNameModel;
import com.intellij.ide.util.gotoByName.ChooseByNamePopup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author steven
 */
public class RestfulPopup extends ChooseByNamePopup {

    public static final Key<RestfulPopup> CHOOSE_BY_NAME_POPUP_IN_PROJECT_KEY = new Key<>("ChooseByNamePopup");

    protected RestfulPopup(@Nullable Project project,
                           @NotNull ChooseByNameModel model,
                           @NotNull ChooseByNameItemProvider provider,
                           @Nullable ChooseByNamePopup oldPopup,
                           @Nullable String predefinedText,
                           boolean mayRequestOpenInCurrentWindow,
                           int initialIndex) {
        super(
                project,
                model,
                provider,
                oldPopup,
                predefinedText,
                mayRequestOpenInCurrentWindow,
                initialIndex
        );
    }

    public static RestfulPopup createPopup(final Project project,
                                           @NotNull final ChooseByNameModel model,
                                           @NotNull ChooseByNameItemProvider provider,
                                           @Nullable final String predefinedText,
                                           boolean mayRequestOpenInCurrentWindow,
                                           final int initialIndex) {
        if (!StringUtil.isEmptyOrSpaces(predefinedText)) {
            return new RestfulPopup(
                    project,
                    model,
                    provider,
                    null,
                    predefinedText,
                    mayRequestOpenInCurrentWindow,
                    initialIndex
            );
        }

        final RestfulPopup oldPopup = project == null
                ? null
                : project.getUserData(CHOOSE_BY_NAME_POPUP_IN_PROJECT_KEY);
        if (oldPopup != null) {
            oldPopup.close(false);
        }
        RestfulPopup newPopup = new RestfulPopup(
                project,
                model,
                provider,
                oldPopup,
                predefinedText,
                mayRequestOpenInCurrentWindow,
                initialIndex
        );

        if (project != null) {
            project.putUserData(CHOOSE_BY_NAME_POPUP_IN_PROJECT_KEY, newPopup);
        }
        return newPopup;
    }

}
