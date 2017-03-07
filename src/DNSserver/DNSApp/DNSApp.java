package DNSserver.DNSApp;

import DNSserver.DNSAnswer;
import DNSserver.DNSRepository;
import DNSserver.DNSRequest.DNSRequest;
import common.AddressV4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.logging.Logger;


public class DNSApp {
    private DNSRepository dnsRepo;
    private ServerSocket serverSocket;

    DNSRepository getDnsRepo() {
        return dnsRepo;
    }

    private DNSApp(int port) throws IOException {
        dnsRepo = new DNSRepository();
        dnsRepo.addDataInRepo("acm.ut.ac.ir", Collections.singletonList("182.0.0.1"), DNSRepository.DNSRecord.Type.A);
        serverSocket = new ServerSocket(port);
    }

    final static Logger logger = Logger.getLogger(DNSApp.class.getName());

    private static int DNS_PORT = 8888;
    public static void main(String[] args) throws IOException {
        DNSApp rootInstance = new DNSApp(DNS_PORT);
        logger.info("Sever is up at localhost:" + DNS_PORT);
        while (true) {
            Socket clientSocket = rootInstance.serverSocket.accept();
            logger.info("Accepted a new Local DNS on " + clientSocket);
            BufferedReader bf = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String request = bf.readLine();
            logger.info("Got a new Request: " + request);
            DNSRequest.RequestHandleType handleType = DNSRequest.getRequestHandleType(request);

            assert handleType != null;
            if (handleType.equals(DNSRequest.RequestHandleType.ITERATIVE)) {
                logger.info("Starting ITERATIVE Handler Thread ....");
                new Thread(new IterativeDNSHandler(rootInstance, clientSocket, request)).start();
            } else if (handleType.equals(DNSRequest.RequestHandleType.RECURSIVE)) {
                logger.info("Starting RECURSIVE Handler Thread ....");
                new Thread(new RecursiveDNSHandler(rootInstance, clientSocket, request)).start();
            }
        }
    }

    static DNSAnswer getNotFoundInDBAnswer() {
        return new DNSAnswer("0.0.0.0", DNSRepository.DNSRecord.Type.A, 0, DNSAnswer.StatusCode.NOT_IN_DATABASE);
    }
}
