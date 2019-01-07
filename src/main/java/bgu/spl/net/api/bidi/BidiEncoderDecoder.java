package bgu.spl.net.api.bidi;

import bgu.spl.net.api.MessageEncoderDecoder;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class BidiEncoderDecoder<Operation> implements MessageEncoderDecoder<Operation> {

    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private int len = 0;

    /**
     * add the next byte to the decoding process
     *
     * @param nextByte the next byte to consider for the currently decoded
     * message
     * @return a message if this byte completes one or null if it doesnt.
     */
    public Operation decodeNextByte(byte nextByte){

        if (nextByte == 12) {
            short opcode= bytesToShort(bytes);
            Operation result = null;
            switch (opcode){
                case 1: result = (Operation) new REGISTER(bytes,len); break;
                case 2: result = (Operation) new LOGIN(bytes,len); break;
                case 3: result = (Operation) new LOGOUT(); break;
                case 4: result = (Operation) new FOLLOW(bytes,len); break;
                case 5: result = (Operation) new POST(bytes,len); break;
                case 6: result = (Operation) new PM(bytes,len); break;
                case 7: result = (Operation) new USERLIST(); break;
                case 8: result = (Operation) new STAT(bytes,len); break;
            }
            len = 0;
            bytes = new byte[1 << 10]; //start with 1k
            return result;
        }
        pushByte(nextByte);
        return null; //not a line yet
    }

    /**
     * encodes the given message to bytes array
     *
     * @param message the message to encode
     * @return the encoded bytes
     */
    public byte[] encode(Operation message){

        int opcode=((bgu.spl.net.api.bidi.Operation)message).getOp_code();
        switch (opcode){
            case 9: return ((NOTIFICATION)message).encode();
            case 10:{
                int second_opcode=((ACK)message).getSecond_Op_code();
                switch(second_opcode) {
                    case 4:
                        return ((ACK_FOLLOW)message).encode();
                    case 7:
                        return ((ACK_USERLIST)message).encode();
                    case 8:
                        return ((ACK_STAT)message).encode();
                }
                return ((ACK)message).encode();
            }
            case 11: return ((ERROR)message).encode();
        }
        return null;

    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len++] = nextByte;
    }

//    private String createOperation() {
//        //notice that we explicitly requesting that the string will be decoded from UTF-8
//        //this is not actually required as it is the default encoding in java.
//        String result = new String(bytes, 0, len, StandardCharsets.UTF_8);
//        len = 0;
//        return result;
//    }

    public short bytesToShort(byte[] byteArr)
    {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }

    public byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }

}


