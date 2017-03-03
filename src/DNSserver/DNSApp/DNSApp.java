package DNSserver.DNSApp;

import DNSserver.DNSRepository;
import DNSserver.DNSRequest.DNSRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;


public class DNSApp implements DNSServer {
    private DNSRepository dnsRepo;
    private ServerSocket serverSocket;

    public DNSRepository getDnsRepo() {
        return dnsRepo;
    }

    DNSApp(int port) throws IOException {
        dnsRepo = new DNSRepository();
        dnsRepo.addDataInRepo("acm.ut.ac.ir", Collections.singletonList("182.0.0.1"), DNSRepository.DNSRecord.Type.A);
        serverSocket = new ServerSocket(port);
    }


    public static void main(String[] args) throws IOException {
        DNSApp rootInstance = new DNSApp(1234);
        while (true) {
            Socket clientSocket = rootInstance.serverSocket.accept();

            BufferedReader bf = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String request = bf.readLine();
            DNSRequest.RequestHandleType handleType = DNSRequest.getRequestHandleType(request);

            if (handleType.equals(DNSRequest.RequestHandleType.Iterative)) {
                new Thread(new IterativeDNSHandler(rootInstance, clientSocket, request)).start();
            } else if (handleType.equals(DNSRequest.RequestHandleType.Recursive)) {
                new Thread(new RecursiveDNSHandler(rootInstance, clientSocket, request)).start();
            }
        }
    }

    @Override
    public void listenForRequest() throws IOException {
    }

    @Override
    public void handleRequest() {

    }
}
