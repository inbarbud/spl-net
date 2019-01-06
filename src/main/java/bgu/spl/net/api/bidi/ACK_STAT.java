package bgu.spl.net.api.bidi;

import java.nio.ByteBuffer;

public class ACK_STAT extends ACK {

    int NumPosts;
    int NumFollowers;
    int NumFollowing;

    public ACK_STAT(int NumPosts, int NumFollowers, int NumFollowing){
        super(8);
        this.NumPosts=NumPosts;
        this.NumFollowers=NumFollowers;
        this.NumFollowing=NumFollowing;
    }

    public byte[] encode() {
        //TODO:sizes?
        ByteBuffer buf = ByteBuffer.allocate(1024);
        buf.put(shortToBytes((short)op_code));          //push opcodes
        buf.put(shortToBytes((short)second_op_code));
        buf.put(shortToBytes((short) NumPosts));       //push numOfUsers
        buf.put(shortToBytes((short) NumFollowers));       //push numOfUsers
        buf.put(shortToBytes((short) NumFollowing));       //push numOfUsers
        buf.put((byte)0);
        buf.put((byte)12);
        return buf.array();







//        byte[] opbytes = super.encode();
//        byte[] posts = shortToBytes((short) NumPosts);
//        byte[] followers = shortToBytes((short) NumFollowers);
//        byte[] following = shortToBytes((short) NumFollowing);
//        int length = 4 + posts.length + followers.length + following.length;
//        byte[] answer = new byte[length+1];
//        int j = 0;
//        for(int i=0;i<4;i++){
//            answer[j]=opbytes[i];
//            j++;
//        }
//        for(int i=0;i<posts.length;i++){
//            answer[j]=posts[i];
//            j++;
//        }
//        for(int i=0;i<followers.length;i++){
//            answer[j]=followers[i];
//            j++;
//        }
//        for(int i=0;i<following.length;i++){
//            answer[j]=following[i];
//            j++;
//        }
//        answer[length]='\0';
//
//        return answer;
    }
}
