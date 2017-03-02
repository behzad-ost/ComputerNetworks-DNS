package DNSserver;

import common.ServerTransceiver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class RootDNS {
    private Map<String, String> table;

    RootDNS(){
        table = new HashMap<>();
        table.put("acm.ut.ac.ir","172.16.32.150:3000");
        //TODO:Fill Table
    }


    public static void main(String[] args) throws IOException {
        ServerTransceiver rootTransceiver = new ServerTransceiver(8081);
        rootTransceiver.accept();

//        new RootDNS();

        while (true){
            System.out.println("Wait for req");
            String request = rootTransceiver.receive();
            System.out.println(request);
        }
    }

}
