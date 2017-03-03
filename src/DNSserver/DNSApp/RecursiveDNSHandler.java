package DNSserver.DNSApp;

import java.net.Socket;

public class RecursiveDNSHandler implements Runnable {
    private DNSApp rootInstance;
    private Socket clientSocket;
    private String request;
    public RecursiveDNSHandler(DNSApp rootInstance, Socket socket, String request) {
        this.rootInstance = rootInstance;
        this.clientSocket = socket;
        this.request = request;
    }
    @Override
    public void run() {
        // TODO: Ousat bezan!
    }
}
