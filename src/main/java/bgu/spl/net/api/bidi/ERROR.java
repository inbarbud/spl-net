package bgu.spl.net.api.bidi;

import java.nio.ByteBuffer;

public class ERROR extends Operation {

    public int second_op_code;

    public ERROR(int second_op_code){
        op_code=11;
        this.second_op_code=second_op_code;
    }

    public byte[] encode(){
        ByteBuffer buf = ByteBuffer.allocate(5);
        buf.put(shortToBytes((short)op_code));
        buf.put(shortToBytes((short)second_op_code));
        buf.put(4, (byte)0);
        buf.put((byte)12);
        return buf.array();




//        byte[] bytes= new byte[5];
//        byte[] first= shortToBytes((short)op_code);
//        byte[] second= shortToBytes((short)second_op_code);
//        for(int i=0;i<2;i++) {
//            bytes[i] = first[i];
//            bytes[i + 2] = second[i];
//        }
//        bytes[4]='\0';
//        return bytes;
    }
}
