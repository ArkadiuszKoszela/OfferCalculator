package pl.koszela.spring.service;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

public interface NotificationInterface {

    static void notificationOpen(String message, NotificationVariant notificationVariant) {
        com.vaadin.flow.component.notification.Notification notification = new com.vaadin.flow.component.notification.Notification(message, 4000, com.vaadin.flow.component.notification.Notification.Position.BOTTOM_CENTER);
        notification.addThemeVariants(notificationVariant);
        notification.open();
    }
}