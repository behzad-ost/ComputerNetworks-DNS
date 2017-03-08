package DNSserver.DNSApp;

import DNSserver.DNSAnswer;
import DNSserver.DNSRepository;
import DNSserver.DNSRequest.DNSRequest;
import DNSserver.DNSRequest.LookUpRequest;
import common.Domain;
import exceptions.DomainNotFoundInDBException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.logging.Logger;

public class RecursiveDNSHandler implements Runnable {
    private DNSApp dnsInstance;
    private Socket clientSocket;
    private String request;


    private PrintWriter localDNSWriter;

    public RecursiveDNSHandler(DNSApp dnsInstance, Socket socket, String request) throws IOException {
        this.dnsInstance = dnsInstance;
        this.clientSocket = socket;
        this.request = request;
        this.localDNSWriter = new PrintWriter(socket.getOutputStream());
    }

    Logger logger = Logger.getLogger(IterativeDNSHandler.class.getName());

    @Override
    public void run() {
        DNSRequest.RequestType type = DNSRequest.getRequestType(request);
        switch (type) {
            case LOOKUP:
                LookUpRequest lookUp = new LookUpRequest(new JSONObject(request));
                List<Domain> subDomains = Domain.subDomains(lookUp.getDomain());
                logger.info("num of subDomains: " + subDomains.size());
                // TODO: first check cache!

                boolean sentAnswer = false;
                for (Domain domain: subDomains) {
                    DNSRepository.DNSRepositoryAnswer dnsRepositoryAnswer = null;
                    try {
                        dnsRepositoryAnswer = dnsInstance.getDnsRepo().searchRepo(domain.toString());
                        DNSAnswer dnsAnswer;
                        dnsAnswer = new DNSAnswer(
                                dnsRepositoryAnswer.getValue(),
                                dnsRepositoryAnswer.getAnswerType(),
                                100,
                                DNSAnswer.StatusCode.SUCCESS
                        );
                        localDNSWriter.println(dnsAnswer.getJSONObjectFormat().toString());
                        localDNSWriter.flush();
                        sentAnswer = true;
                        break;
                    } catch (DomainNotFoundInDBException e) {
                        logger.info("cant find " + domain + " in DB");
                    }
                }

                if (!sentAnswer) {
                    localDNSWriter.println(DNSApp.getNotFoundInDBAnswer().getJSONObjectFormat().toString());
                    localDNSWriter.flush();
                }

                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case ADD:
                break;
            case UPDATE:
                break;
            default:
                break;
        }
    }
        // TODO: Ousat bezan!
}
