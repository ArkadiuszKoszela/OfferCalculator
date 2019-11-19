package pl.koszela.spring.service;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

public interface NotificationInterface {

    static void notificationOpen(String message, NotificationVariant notificationVariant) {
        Notification notification = new Notification(message, 4000, Notification.Position.BOTTOM_CENTER);
        notification.addThemeVariants(notificationVariant);
        notification.open();
    }
}