package bgu.spl.net.api.bidi;
//import org.graalvm.util.Pair;
import java.util.LinkedList;

public class ConnectionsImpl<T> implements Connections<T> {

    private class Pair<K,V>{
        K key;
        V value;

        private Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }
        private V getRight() {
            return value;
        }

        private K getLeft() {
            return key;
        }
    }

    LinkedList<Pair<Integer, bgu.spl.net.srv.bidi.ConnectionHandler>> handlersList= new LinkedList<>();     //<id,handler>

    public ConnectionsImpl(){
    }

    public void connect(int connectionId,bgu.spl.net.srv.bidi.ConnectionHandler handler)
    {
        handlersList.add(new Pair<>(connectionId,handler));
    }

    //sends a message T to client represented
    //by the given connId
    public boolean send(int connectionId, T msg){
        boolean found=false;
        bgu.spl.net.srv.bidi.ConnectionHandler handler=null;
        for (Pair<Integer, bgu.spl.net.srv.bidi.ConnectionHandler> element: handlersList) {
            if(element.getLeft()==connectionId) {
                found = true;
                handler= element.getRight();
            }
        }
        if(!found)
            return false;
        handler.send(msg);
        return true;
    }

    //sends a message T to all active clients
    public void broadcast(T msg){
        for (Pair<Integer,bgu.spl.net.srv.bidi.ConnectionHandler> element: handlersList) {
            element.getRight().send(msg);
        }
    }
    //removes active client connId from map.
    public void disconnect(int connectionId){
        int index=0;
        boolean found=false;
        for (Pair<Integer,bgu.spl.net.srv.bidi.ConnectionHandler> element: handlersList) {
            if(element.getLeft()==connectionId) {
                found=true;
            }
            else {
                index++;
            }
        }
        if (found) {
            handlersList.remove(index);
        }
    }
    public int numOfUsers(){
        return handlersList.size();
    }
//    public int[] allUsers(){
//        int i=0;
//        int[] userList = new int[numOfUsers()];
//        for (Pair<Integer,bgu.spl.net.srv.bidi.ConnectionHandler> element: handlersList) {
//            userList[i] = element.getLeft();
//            i++;
//        }
//        return userList;
//    }



//    public boolean contains(int connectionId){
//        for (Pair<Integer, bgu.spl.net.srv.bidi.ConnectionHandler> element: handlersList) {
//            if(element.getLeft()==connectionId) {
//                return true;
//            }
//        }
//        return false;
//    }
}
