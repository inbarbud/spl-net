package bgu.spl.net.api.bidi;

import java.nio.charset.StandardCharsets;

public class POST extends Operation {
    String content;

    public POST(byte[] byteArr, int len){
        op_code=5;
        parse(byteArr, len);
    }

    public void parse(byte[] byteArr, int len){
        content= new String(byteArr, 2, len, StandardCharsets.UTF_8);
    }
}
