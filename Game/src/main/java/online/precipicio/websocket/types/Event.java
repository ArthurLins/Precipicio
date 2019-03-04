package online.precipicio.websocket.types;

public interface Event {

    void handle(ClientMessage message);
}
