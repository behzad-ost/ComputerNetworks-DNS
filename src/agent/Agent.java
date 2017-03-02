package agent;


import common.AddressV4;
import common.ServerTransceiver;
import common.Transceiver;
import org.json.JSONObject;

import java.io.IOException;

public class Agent {
    private Config config;
    public Agent() throws AddressV4.InvalidIPv4Exception, IOException {
        config = new Config();
    }

    public static void main(String[] args) throws IOException, AddressV4.InvalidIPv4Exception {
        Agent agent = new Agent();
//        agent.showConfig();
        ServerTransceiver agentServer = new ServerTransceiver(12345);
        Transceiver rootTransceiver = new Transceiver("localhost", 8081);
                                        //TODO:get root address from config
        agentServer.accept();
        while (true){
            String request = agentServer.receive();
            if (request == null || request == "" || request == "\n")
                break;

            String[] tokens = request.split("\\s+");
            String command = tokens[0];
            switch (command) {
                case "lookup":
                    try {
//                        JSONObject obj = new JSONObject();
//                        obj.put("domain", tokens[1]);
//                        obj.put("requestType", tokens[2]);
//                        rootTransceiver.send(obj.toString());

                        System.out.println("lookup request.");
                    } catch (Exception ex) {
                        System.out.println("bad request");
                    }
                    break;
                case "update":
                    try {
                        System.out.println("Handle update request");

                    } catch (Exception ex) {

                    }
                    break;
                case "add":
                    try {
                        System.out.println("Handle add request");

                    } catch (Exception ex) {

                    }
                    break;
                default:
                    System.out.println(request);
            }
        }

//        rootTransceiver.send("Hi");

        // TODO: get request on socket and .....
    }

    private void showConfig() {
        config.showConfig();
    }
}
