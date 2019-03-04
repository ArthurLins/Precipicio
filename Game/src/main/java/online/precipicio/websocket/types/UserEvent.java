package online.precipicio.websocket.types;

import online.precipicio.websocket.headers.UserEvents;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface UserEvent {
    UserEvents id();
}
