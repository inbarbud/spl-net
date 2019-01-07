package bgu.spl.net.api.bidi;

import java.nio.charset.StandardCharsets;

public class LOGIN extends Operation {
    String username;
    String password;

    public LOGIN(byte[] byteArr, int len){
        op_code=2;
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
        username= new String(byteArr, 2, count-2, StandardCharsets.UTF_8);
        password= new String(byteArr,count+1,len-count-2,StandardCharsets.UTF_8);
    }
}
