package bgu.spl.net.api.bidi;

import java.util.concurrent.ConcurrentHashMap;

public class BidiMessagingProtocolImpl<Operation> implements BidiMessagingProtocol<Operation> {

    int ID;
    Connections<Operation> connections;
    ClientsData clients;
    boolean terminate=false;

    /**
     * Used to initiate the current client protocol with it's personal connection ID and the connections implementation
     **/
    public void start(int connectionId, Connections<Operation> connections){
        this.ID=connectionId;
        this.connections=connections;
        this.clients=ClientsData.getInstance();
    }

    public void process(Operation message){
        int opcode= ((bgu.spl.net.api.bidi.Operation)message).getOp_code();
        switch(opcode){
            case 1: processRegister(message); break;
            case 2: processLogin(message); break;
            case 3: processLogOut(message); break;
            case 4: processFollow(message); break;
            case 5: processPost(message); break;
            case 6: processPM(message); break;
            case 7: processUserList(message); break;
            case 8: processStat(message); break;
        }
    }

    /**
     * @return true if the connection should be terminated
     */
    public boolean shouldTerminate(){
        return terminate;
    }

    private void processRegister(Operation message){
        if(clients.isRegistered(((REGISTER)message).username)){     //if already exist
            SendError(1);
        }
        else{
            clients.registerClient(((REGISTER)message).username, ((REGISTER)message).password, ID);
            ACK ack = new ACK(1);
            System.out.println(((REGISTER) message).username + " registered!");
            connections.send(ID,(Operation)ack);
        }
    }
    private void processLogin(Operation message){
        boolean shouldError=false;
        //If the user doesn’t exist
        if(!clients.isRegistered(((LOGIN)message).username)){
            shouldError=true;
        }
        //password doesn’t match the one entered for the username
        if(!clients.isCorrectPassword(((LOGIN)message).username, ((LOGIN)message).password)){
            shouldError=true;
        }
        //the current client has already succesfully logged in
        if(clients.isLoggedIn(((LOGIN)message).username))
        {
            shouldError=true;
        }
        if (shouldError){       //send error
            SendError(2);
        }
        else{
            //login user
            clients.loginClient(((LOGIN)message).username);
            ACK ack = new ACK(2);
            System.out.println(((LOGIN) message).username + " logged in!");
            connections.send(ID,(Operation)ack);
        }
    }
    private void processLogOut(Operation message){
        boolean shouldError=false;
        //If the user doesn’t exist
        if(!clients.isRegistered(ID)){
            shouldError=true;
        }
        //user is not logged in
        if(!clients.isLoggedIn(ID))
        {
            shouldError=true;
        }
        if (shouldError){       //send error
            SendError(3);
        }
        else{
            clients.logoutClient(ID);
            connections.disconnect(ID);
            terminate=true;
            ACK ack = new ACK(3);
            System.out.println((clients.getUserName(ID) + " logged out!"));
            connections.send(ID,(Operation)ack);
        }
    }
    private void processFollow(Operation message) {
        int successful_follows = 0;
        String[] successful_UserNameList = new String[((FOLLOW) message).NumOfUsers];
        boolean shouldError = false;

        //If the user doesn’t exist
        if (!clients.isRegistered(ID)) {
            shouldError = true;
        }
        //user is not logged in
        if (!clients.isLoggedIn(ID)) {
            shouldError = true;
        }
        if (!shouldError && ((FOLLOW) message).follow == 0) {                 //follow
            //for each follow request try to execute
            //count how many successful
            for (String person : ((FOLLOW) message).UserNameList) {
                if (clients.isRegistered(person) && !clients.checkIfFollowing(ID, person)) {
                    clients.followPerson(ID,person);
                    successful_UserNameList[successful_follows] = person;
                    successful_follows++;
                }
            }
        } else if (!shouldError) {                                              //unfollow
            for (String person : ((FOLLOW) message).UserNameList) {
                if (clients.isRegistered(person) && clients.checkIfFollowing(ID, person)) {
                    clients.unfollowPerson(ID,person);
                    successful_UserNameList[successful_follows] = person;
                    successful_follows++;
                }
            }
        }
        if(shouldError | successful_follows==0)
            SendError(4);
        else{
            ACK_FOLLOW ack = new ACK_FOLLOW(successful_follows,successful_UserNameList);
            connections.send(ID,(Operation)ack);
        }
    }
    private void processPost(Operation message){
        if(clients.isRegistered(ID) && clients.isLoggedIn(ID) ){
            //ConcurrentHashMap<Integer,String> followersList = clients.getFollowersList(ID);
            for (ConcurrentHashMap.Entry<Integer, String> entry : clients.getFollowersList(ID).entrySet()) {
                connections.send(entry.getKey(),message);
            }
            String temp = ((POST)message).content;
            int left=0;
            int right = 0;
            while(left<temp.length())
            {
                if(temp.charAt(left) == '@'){
                    right=left+1;
                    while(temp.charAt(right) !=  ' ')
                        right ++;

                    String tempPMuser = temp.substring(left,right);
                    if(clients.isRegistered(tempPMuser)) {
                        connections.send(clients.getID(tempPMuser), message);
                    }
                    left = right + 1;
                }
            }
            //saved to a data structure
            clients.saveMessage(ID,((POST)message).content);
            ACK ack = new ACK(5);
            connections.send(ID,(Operation)ack);
        }

        else{
            SendError(5);
        }
    }

    private void processPM(Operation message){
        boolean shouldError=false;

        //If the user doesn’t exist
        if(!clients.isRegistered(ID)){
            shouldError=true;
        }
        //sending user is not logged in
        if(!clients.isLoggedIn(ID))
        {
            shouldError=true;
        }
        //the reciepient username isn’t registered
        if(!clients.isRegistered(((PM)message).username)){
            shouldError=true;
        }
        if (shouldError){       //send error
            SendError(6);
        }
        else{
            connections.send(clients.getID(((PM)message).username), message);
            //save to a data structure in the application, along with post messages
            clients.saveMessage(ID,((PM)message).content);
            ACK ack = new ACK(6);
            connections.send(ID,(Operation)ack);
        }
    }
    private void processUserList(Operation message){
        //user is not logged in
        if(!clients.isLoggedIn(ID)){
            SendError(7);
        }
        else{
            ACK_USERLIST ack = new ACK_USERLIST(connections.numOfUsers(), clients.getRegisteredUserNames());
            connections.send(ID,(Operation)ack);
        }
    }
    private void processStat(Operation message){
        boolean shouldError=false;
        //must be logged in
        if(!clients.isLoggedIn(((STAT)message).username)){
            shouldError=true;
        }
        if(!clients.isRegistered(((STAT)message).username)){
            shouldError=true;
        }
        if (shouldError){       //send error
            SendError(8);
        }
        else{
            ACK_STAT ack = new ACK_STAT(clients.getNumOfPosts(ID),clients.getNumOfFollowers(ID),clients.getNumOfFollowing(ID));
            connections.send(ID,(Operation)ack);
        }
    }
    private void SendError(int opCode) {
        ERROR err= new ERROR(opCode);
        connections.send(ID,(Operation)err);
    }

}


