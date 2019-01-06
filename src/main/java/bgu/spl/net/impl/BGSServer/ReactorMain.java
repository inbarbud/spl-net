package bgu.spl.net.impl.BGSServer;


import bgu.spl.net.api.bidi.BidiEncoderDecoder;
import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.srv.Server;

public class ReactorMain {

    public static void main(String[] args) {
        Server.reactor(Integer.parseInt(args[1]),Integer.parseInt(args[0]), () -> new BidiMessagingProtocolImpl<>(),() -> new BidiEncoderDecoder()).serve();
    }
}