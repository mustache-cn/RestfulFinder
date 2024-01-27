package cn.com.mustache.plugins.restful.finder.uitl;

import java.util.Objects;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.project.Project;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

/**
 * @author steven
 */
public class NotificationUtil {

    public static void notify(@Nullable Project project,
                              @Nullable String content,
                              @Nullable NotificationType notificationType) {
        if (StringUtils.isEmpty(content) || Objects.isNull(notificationType)) {
            throw new RuntimeException("notificationType or content isEmpty");
        }
        Notification notification = new Notification("RestfulFinder Notification Group", "", content, notificationType);
        Notifications.Bus.notify(notification, project);
    }

}
