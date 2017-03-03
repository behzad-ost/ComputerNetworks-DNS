package agent;

import DNSserver.DNSAnswer;
import DNSserver.DNSRepository;
import DNSserver.DNSRequest.DNSRequest;
import DNSserver.DNSRequest.LookUpRequest;
import common.AddressV4;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class IterativeClientHandler implements Runnable {
    Socket clientSocket;
    public IterativeClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        String request = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            request = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert request != null;
        String[] tokens = request.split(" ");
        switch (tokens[0]) {
            case "lookup":
                LookUpRequest lookUpRequest = new LookUpRequest(tokens[1], DNSRequest.RequestHandleType.Recursive);
                for (AddressV4 addressV4: Agent.rootsAddress) {
                    try {
                        Socket socketToRoot = new Socket(addressV4.getIP(), addressV4.getPort());
                        PrintWriter out = new PrintWriter(socketToRoot.getOutputStream());
                        out.print(lookUpRequest);
                        out.flush();

                        BufferedReader bf = new BufferedReader(new InputStreamReader(socketToRoot.getInputStream()));
                        String answer = bf.readLine();

                        DNSAnswer dnsAnswer = new DNSAnswer(new JSONObject(answer));
                        System.out.println(dnsAnswer);
                    } catch (IOException | DNSAnswer.InvalidDNSjsonAnswerException e) {
                        e.printStackTrace();
                    }
                }
                break;

            default:
        }
    }
}
