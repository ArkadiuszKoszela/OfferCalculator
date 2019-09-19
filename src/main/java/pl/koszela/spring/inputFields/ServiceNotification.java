package pl.koszela.spring.inputFields;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

public class ServiceNotification {

    public static void getNotificationSucces(String s){
        Notification notification = new Notification(s, 4000, Notification.Position.BOTTOM_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.open();
    }

    public static void getNotificationError(String s){
        Notification notification = new Notification(s, 4000, Notification.Position.BOTTOM_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.open();
    }

}
