package bgu.spl.net.api.bidi;

import java.nio.ByteBuffer;

public class NOTIFICATION  extends Operation{

    int notificationType;
    String postingUser;
    String content;

    public NOTIFICATION(int NotificationType, String PostingUser, String Content){
        op_code=9;
        this.notificationType=NotificationType;
        this.postingUser=PostingUser;
        this.content=Content;
    }
    public byte[] encode(){
        //TODO:size?
        ByteBuffer buf = ByteBuffer.allocate(1024);
        buf.put(shortToBytes((short)op_code));                   //push opcode
        buf.put(shortToBytes((short)notificationType));          //push notiType
        buf.put(postingUser.getBytes());                         //push postingUserName
        buf.put(content.getBytes());                             //push postingUserName

        buf.put((byte)0);
        buf.put((byte)12);
        return buf.array();


//
//        byte[] opbytes = ACK.encode();
//        byte[] type = shortToBytes((short) notificationType);
//        byte[] user = postingUser.getBytes();
//        byte[] con = content.getBytes();
//        int length = 4 + type.length + user.length + con.length;
//        byte[] answer = new byte[length+1];
//        int j = 0;
//        for(int i=0;i<4;i++){
//            answer[j]=opbytes[i];
//            j++;
//        }
//        for(int i=0;i<type.length;i++){
//            answer[j]=type[i];
//            j++;
//        }
//        for(int i=0;i<user.length;i++){
//            answer[j]=user[i];
//            j++;
//        }
//        for(int i=0;i<con.length;i++){
//            answer[j]=con[i];
//            j++;
//        }
//        answer[length]='\0';
//
//        return answer;

    }
}
