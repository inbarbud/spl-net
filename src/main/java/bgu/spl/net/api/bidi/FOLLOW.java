package bgu.spl.net.api.bidi;

import java.nio.charset.StandardCharsets;

public class FOLLOW extends Operation {

    int follow;
    int NumOfUsers;
    String[] UserNameList;

    public FOLLOW(byte[] byteArr, int len){
        op_code=4;
        parse(byteArr, len);
    }

    public void parse(byte[] byteArr, int len){
        this.follow=byteArr[2];
        this.NumOfUsers=((byteArr[3] & 0xff) << 8) | (byteArr[4] & 0xff);
        UserNameList= new String[NumOfUsers];

        int count=0;
        int left = 5;
        int right=5;
        int zeros=NumOfUsers-1;
        while(zeros>0){
            boolean finito = false;
            for(int i=left;!finito && i<len;i++){
                right = i;
               if(byteArr[i] == 0) {
                   finito = true;
                   zeros--;
               }
           }
            UserNameList[count] = new String(byteArr,left, right, StandardCharsets.UTF_8);
            count++;
            left = right;
        }
    }
}
