package bgu.spl.net.api.bidi;

import java.io.IOException;

public interface Connections<T> {

    void connect(int connectionId,bgu.spl.net.srv.bidi.ConnectionHandler handler);

    boolean send(int connectionId, T msg);

    void broadcast(T msg);

    void disconnect(int connectionId);

    int numOfUsers();

//    int[] allUsers();

    //boolean contains(int connectionId);
}
