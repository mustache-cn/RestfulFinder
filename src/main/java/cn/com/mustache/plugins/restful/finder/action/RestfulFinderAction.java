package cn.com.mustache.plugins.restful.finder.action;

import java.awt.datatransfer.DataFlavor;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.swing.*;

import cn.com.mustache.plugins.restful.finder.constant.HttpMethodEnum;
import cn.com.mustache.plugins.restful.finder.contract.RestfulItem;
import com.intellij.featureStatistics.FeatureUsageTracker;
import com.intellij.ide.actions.GotoActionBase;
import com.intellij.ide.util.gotoByName.ChooseByNameFilter;
import com.intellij.ide.util.gotoByName.ChooseByNameItemProvider;
import com.intellij.ide.util.gotoByName.ChooseByNameModel;
import com.intellij.ide.util.gotoByName.ChooseByNamePopup;
import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author steven
 */
public class RestfulFinderAction extends GotoActionBase implements DumbAware {

    public RestfulFinderAction() {
    }

    @Override
    protected void gotoActionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            return;
        }

        FeatureUsageTracker.getInstance().triggerFeatureUsed("navigation.popup.service");

        ChooseByNameContributor[] chooseByNameContributors = {
                new RestfulContributor(e.getData(PlatformDataKeys.MODULE)),
        };

        final RestfulModel model = new RestfulModel(
                project,
                chooseByNameContributors
        );

        GotoActionCallback<HttpMethodEnum> callback = new GotoActionCallback<>() {
            @Override
            protected ChooseByNameFilter<HttpMethodEnum> createFilter(@NotNull ChooseByNamePopup popup) {
                return new RestfulFilter(popup, model, project);
            }

            @Override
            public void elementChosen(ChooseByNamePopup chooseByNamePopup, Object element) {
                if (element instanceof RestfulItem) {
                    RestfulItem navigationItem = (RestfulItem) element;
                    if (navigationItem.canNavigate()) {
                        navigationItem.navigate(true);
                    }
                }
            }
        };

        RestfulProvider provider = new RestfulProvider(getPsiContext(e));
        showNavigationPopup(
                e,
                model,
                callback,
                "Request Mapping Url matching pattern",
                true,
                true,
                (ChooseByNameItemProvider) provider
        );
    }

    @Override
    protected <T> void showNavigationPopup(AnActionEvent e,
                                           ChooseByNameModel model,
                                           final GotoActionCallback<T> callback,
                                           @Nullable final String findUsagesTitle,
                                           boolean useSelectionFromEditor,
                                           final boolean allowMultipleSelection,
                                           final ChooseByNameItemProvider itemProvider) {
        if (Objects.isNull(e.getProject())) {
            throw new RuntimeException("System error Please contact the developer!");
        }
        boolean mayRequestOpenInCurrentWindow =
                model.willOpenEditor() &&
                        FileEditorManagerEx.getInstanceEx(e.getProject()).hasSplitOrUndockedWindows();
        Pair<String, Integer> start = getInitialText(useSelectionFromEditor, e);
        String copiedURL = tryFindCopiedURL();

        String predefinedText = start.first == null ? copiedURL : start.first;

        showNavigationPopup(
                callback,
                findUsagesTitle,
                RestfulPopup.createPopup(
                        e.getProject(),
                        model,
                        itemProvider,
                        predefinedText,
                        mayRequestOpenInCurrentWindow,
                        start.second
                ),
                allowMultipleSelection
        );
    }

    private String tryFindCopiedURL() {
        String contents = CopyPasteManager.getInstance().getContents(DataFlavor.stringFlavor);
        if (contents == null || !contents.trim().startsWith("http")) {
            return null;
        }
        return contents.length() <= 120 ? contents : contents.substring(0, 120);
    }


    protected static class RestfulFilter extends ChooseByNameFilter<HttpMethodEnum> {
        RestfulFilter(final ChooseByNamePopup popup,
                      RestfulModel model,
                      final Project project) {
            super(popup, model, RestfulFilterConfiguration.getInstance(project), project);
        }

        @Override
        @NotNull
        protected List<HttpMethodEnum> getAllFilterValues() {
            return Arrays.asList(HttpMethodEnum.values());
        }

        @Override
        protected String textForFilterValue(@NotNull HttpMethodEnum value) {
            return value.name();
        }

        @Override
        protected Icon iconForFilterValue(@NotNull HttpMethodEnum value) {
            return null;
        }
    }

}
