package bgu.spl.net.api.bidi;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ACK_FOLLOW extends ACK {

    public int numOfUsers;
    public String[] userNameList;

    public ACK_FOLLOW(int numOfUsers, String[] userNameList){         //successful
        super(4);
        this.numOfUsers=numOfUsers;
        //this.userNameList=stringArrToString(userNameList, numOfUsers);
        this.userNameList=userNameList;
    }

//    public String stringArrToString(String[] arr, int len){
//        String output="";
//        byte zero=0;
//        for(int i=0;i<len-1;i++){
//            output= output+ arr[i]+zero;
//        }
//        output+=arr[len-1];
//        return output;
//    }

    public byte[] encode() {
        //TODO:sizes?
        ByteBuffer buf = ByteBuffer.allocate(1024);
        buf.put(shortToBytes((short)op_code));          //push opcodes
        buf.put(shortToBytes((short)second_op_code));
        buf.put(shortToBytes((short) numOfUsers));       //push numOfUsers
        for(int i=0;i<numOfUsers;i++){                 //push names
            for(int j=0;j<(userNameList[i]).length();j++){
                buf.putChar(userNameList[i].charAt(j));
            }
            buf.put((byte)0);
        }
        buf.put((byte)12);
        return buf.array();



//        byte[] opbytes = super.encode();
//        byte[] num = shortToBytes((short) numOfUsers);
//        byte[] names = userNameList.getBytes();
//        int length = 4 + num.length + names.length;
//        byte[] answer = new byte[length+1];
//        int j = 0;
//        for(int i=0;i<4;i++){
//            answer[j]=opbytes[i];
//            j++;
//        }
//        for(int i=0;i<num.length;i++){
//            answer[j]=num[i];
//            j++;
//        }
//        for(int i=0;i<names.length;i++){
//            answer[j]=names[i];
//            j++;
//        }
//        answer[length]='\0';
//
//        return answer;
    }
}
