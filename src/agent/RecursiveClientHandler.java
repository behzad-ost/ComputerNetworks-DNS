package agent;

import DNSserver.DNSAnswer;
import DNSserver.DNSRequest.DNSRequest;
import DNSserver.DNSRequest.LookUpRequest;
import common.AddressV4;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;

public class RecursiveClientHandler implements Runnable {
    private Socket clientSocket;
    private BufferedReader clientReadBuffer;
    private PrintWriter clientWriteBuffer;

    public RecursiveClientHandler(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.clientReadBuffer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.clientWriteBuffer = new PrintWriter(clientSocket.getOutputStream());
    }

    @Override
    public void run() {
        Logger logger = Logger.getLogger("RECURSIVE Thread");
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
                logger.info("lookup request: " + tokens[1]);
                LookUpRequest lookUpRequest = new LookUpRequest(tokens[1], DNSRequest.RequestHandleType.ITERATIVE);
                logger.info("lookUpRequest Obj: " + lookUpRequest.toString());
                for (AddressV4 addressV4: Agent.rootsAddress) {
                    try {
                        logger.info("connecting to: " + addressV4);
                        Socket socketToRoot = new Socket(addressV4.getIP(), addressV4.getPort());
                        PrintWriter rootWriteBuffer = new PrintWriter(socketToRoot.getOutputStream());
                        rootWriteBuffer.println(lookUpRequest.getJSONObjectFormat().toString());
                        rootWriteBuffer.flush();

                        BufferedReader rootReadBuffer = new BufferedReader(new InputStreamReader(socketToRoot.getInputStream()));
                        String answer = rootReadBuffer.readLine();
                        logger.info("Got from root: " + answer);

                        DNSAnswer dnsAnswer = new DNSAnswer(new JSONObject(answer));
                        clientWriteBuffer.println(dnsAnswer.getJSONObjectFormat().toString());
                        clientWriteBuffer.flush();
                        logger.info("Sent to client: " + dnsAnswer.getJSONObjectFormat().toString());
                        break;
                    } catch (IOException | DNSAnswer.InvalidDNSJsonAnswerException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                logger.info("not a supported request!");
                clientWriteBuffer.println("Oops! Supported requests are lookup, update and add!");
                clientWriteBuffer.flush();
        }
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
