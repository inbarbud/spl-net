package bgu.spl.net.api.bidi;

import java.nio.charset.StandardCharsets;

public class STAT extends Operation {
    String username;

    public STAT(byte[] byteArr, int len){
        op_code=8;
        parse(byteArr, len);
    }
    public void parse(byte[] byteArr, int len){
        username= new String(byteArr, 2, len, StandardCharsets.UTF_8);
    }
}
