package bgu.spl.net.api.bidi;

import java.nio.charset.StandardCharsets;

public class PM extends Operation {
    String username;
    String content;

    public PM(byte[] byteArr, int len){
        op_code=6;
        parse(byteArr, len);
    }
    public void parse(byte[] byteArr, int len){
        int count=2;
        boolean finished=false;
        while (!finished){                      //not finished
            if(byteArr[count]==0)
                finished=true;
            else
                count++;
        }
        username= new String(byteArr, 2, count, StandardCharsets.UTF_8);
        content= new String(byteArr,count,len,StandardCharsets.UTF_8);
    }
}
