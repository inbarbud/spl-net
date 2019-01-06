package bgu.spl.net.api.bidi;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ClientsData {

    private ConcurrentHashMap<String,String> passwords;             //<username,password>
    private ConcurrentHashMap<String, Boolean> logins;              //<username,logged_in>
    private ConcurrentHashMap<String, Integer> connectionIds;       //<username,connectionID>
    private ConcurrentHashMap<String, ConcurrentHashMap<Integer,String>> followers; //<username,<connectionID, username>>
    private ConcurrentHashMap<String, Integer> following;           //<username, followingNum>
    //private ReadWriteLock rwLock;
    private ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> userMessages; //<username,messages queue>

    private static class ClientsDataHolder{
        private static ClientsData instance = new ClientsData();
    }

    private ClientsData() {

        //rwLock = new ReentrantReadWriteLock();
        passwords = new ConcurrentHashMap<>();
        logins = new ConcurrentHashMap<>();
        connectionIds = new ConcurrentHashMap<>();
        followers= new ConcurrentHashMap<>();
        following= new ConcurrentHashMap<>();
        userMessages = new ConcurrentHashMap<>();
    }

    public static ClientsData getInstance() {
        return ClientsDataHolder.instance;
    }

    public boolean isRegistered(String username){
        return logins.containsKey(username);
    }

    public boolean isRegistered(int connectionID){
        return connectionIds.containsValue(connectionID);
    }

    public void registerClient(String username, String password, int connectionID){      //if doesn't exist, add to data structures
        passwords.put(username,password);
        logins.put(username,false);
        connectionIds.put(username,connectionID);
        ConcurrentHashMap<Integer,String> followersList= new ConcurrentHashMap<>();       //create a followers structure
        followers.put(username,followersList);
        following.put(username,0);
        userMessages.put(username,new ConcurrentLinkedQueue<>());

    }

    public boolean isCorrectPassword(String username, String password){
        String pas= passwords.get(username);
        return (pas.equals(password));
    }

    public void loginClient(String username){
        logins.put(username,true);
    }
//    public void logoutClient(String username){
//        logins.put(username,false);
//    }

    public void logoutClient(int connectionID){
        logins.put(getUserName(connectionID),false);
    }

    public boolean isLoggedIn(String username){
        return logins.get(username);
    }

    public boolean isLoggedIn(int connectionID){
        boolean found=false;
        String username="";
        for (ConcurrentHashMap.Entry<String, Integer> entry : connectionIds.entrySet()) {
            if(connectionID == entry.getValue()){
                found=true;
                username = entry.getKey();
            }
        }
        if(found) {
            return logins.get(username);
        }
        else
            return false;
    }

    public int getID(String username){
        return connectionIds.get(username);
    }

    public String getUserName(int connectionID){
        String username="";
        for (ConcurrentHashMap.Entry<String, Integer> entry : connectionIds.entrySet()) {
            if(connectionID == entry.getValue()){
                username = entry.getKey();
            }
        }
        return username;
    }

    public boolean checkIfFollowing(int connectionID, String destName){
        String username= getUserName(connectionID);
        return followers.get(destName).containsValue(username);
    }

    public ConcurrentHashMap<Integer,String> getFollowersList(int connectionID )
    {
        return followers.get(connectionID);
    }

    public void saveMessage(int connectionID ,String message){
        userMessages.get(getUserName(connectionID)).add(message);
    }

//    public String userListToString(int[] userList){
//        String usersString = "";
//        for(int i=0;i<userList.length;i++)
//            usersString = usersString + getUserName(userList[i]) + " ";
//        return usersString;
//    }

    public String[] getRegisteredUserNames(){
        String[] userNames= new String[logins.size()];
        int i=0;
            for (ConcurrentHashMap.Entry<String, Boolean> entry : logins.entrySet()) {
                userNames[i]= entry.getKey();
                i++;
            }
            return userNames;

    }
    public int getNumOfPosts(int connectionID){
        return (userMessages.get(getUserName(connectionID))).size();
    }
    public int getNumOfFollowing(int connectionID){
        return following.get(getUserName(connectionID));
    }
    public int getNumOfFollowers(int connectionID){
        return (followers.get(getUserName(connectionID))).size();
    }
    public void followPerson(int connectionID, String destName){
        String sourceName= getUserName(connectionID);
        following.put(sourceName,following.get(destName)+1);

        ConcurrentHashMap<Integer,String> followinglist= followers.get(destName);
        followinglist.put(connectionID,sourceName);
    }

    public void unfollowPerson(int connectionID, String destName){
        String sourceName= getUserName(connectionID);
        following.put(sourceName,following.get(destName)-1);

        ConcurrentHashMap<Integer,String> followinglist= followers.get(destName);
        followinglist.remove(connectionID);
    }


}
