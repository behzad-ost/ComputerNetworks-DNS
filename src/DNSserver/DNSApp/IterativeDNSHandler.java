package DNSserver.DNSApp;

import DNSserver.DNSAnswer;
import DNSserver.DNSRepository;
import DNSserver.DNSRequest.DNSRequest;
import DNSserver.DNSRequest.LookUpRequest;
import common.AddressV4;
import common.Domain;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class IterativeDNSHandler implements Runnable {
    private DNSApp dnsInstance;
    private Socket clientSocket;
    private String request;
    public IterativeDNSHandler(DNSApp dnsInstance, Socket socket, String request) {
        this.dnsInstance = dnsInstance;
        this.clientSocket = socket;
        this.request = request;
    }

    private void sendAnswer(DNSAnswer dnsAnswer) throws IOException {
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
        out.println(dnsAnswer);
        out.flush();
    }

    @Override
    public void run() {
        DNSRequest.RequestType type = DNSRequest.getRequestType(request);

        switch (type) {
            case LookUp:
                LookUpRequest lookUp = new LookUpRequest(new JSONObject(request));
                List<Domain> subDomains = Domain.subDomains(lookUp.getDomain());
                // TODO: first check cache!

                for (Domain domain: subDomains) {
                    DNSRepository.DNSRepositoryAnswer dnsRepositoryAnswer;
                    dnsRepositoryAnswer = dnsInstance.getDnsRepo().searchRepo(domain.toString());

                    DNSAnswer dnsAnswer = null;
                    try {
                        dnsAnswer = new DNSAnswer(new AddressV4(dnsRepositoryAnswer.getValue()),
                                                                dnsRepositoryAnswer.getAnswerType(),
                                                            100,
                                                                DNSAnswer.StatusCode.success);
                    } catch (AddressV4.InvalidIPv4Exception e) {
//                        e.printStackTrace();
                        dnsAnswer = new DNSAnswer(null, null,0, DNSAnswer.StatusCode.notInDatabase);
                    }
                    if (dnsRepositoryAnswer != null) {
                        try {
                            sendAnswer(dnsAnswer);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                }
                break;
            case AddNewDomain:
                break;
            case UpdateCurrentDomain:
                break;
            default:
                break;
        }
    }
}
