package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.bidi.BidiEncoderDecoder;
import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.srv.BaseServer;
import bgu.spl.net.srv.Server;

public class TPCMain {

    public static void main(String[] args) {
        Server.threadPerClient(Integer.parseInt(args[0]), () -> new BidiMessagingProtocolImpl<>(),() -> new BidiEncoderDecoder()).serve();
        //Server.threadPerClient(7777, () -> new BidiMessagingProtocolImpl<>(),() -> new BidiEncoderDecoder()).serve();

    }

}