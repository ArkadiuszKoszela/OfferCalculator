package pl.koszela.spring.service;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

public class ServiceNotification {

    public static void getNotificationSucces(String message) {
        Notification notification = new Notification(message, 4000, Notification.Position.BOTTOM_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.open();
    }

    public static void getNotificationError(String message) {
        Notification notification = new Notification(message, 4000, Notification.Position.BOTTOM_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.open();
    }
}