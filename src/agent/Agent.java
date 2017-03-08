package agent;

import common.AddressV4;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class Agent {
    static List<AddressV4> rootsAddress;
    private static boolean isRecursive;
    private static Logger logger = Logger.getLogger(Agent.class.getName());
    public static void main(String[] args) throws IOException, AddressV4.InvalidIPv4Exception {
        setConfiguration();

        ServerSocket serverSocket = new ServerSocket(12345);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            logger.info("New client add on " + clientSocket);
            if (isRecursive) {
                logger.info("Starting RECURSIVE Handler Thread!");
                new Thread(new RecursiveClientHandler(clientSocket)).start();
            }
            else {
                logger.info("Starting ITERATIVE Handler Thread!");
                new Thread(new IterativeClientHandler(clientSocket)).start();
            }
        }
    }

    private static void setConfiguration() {
        setRootsAddress(new LinkedList<AddressV4>());
        for (String item: Config.rootsAddress) {
            try {
                logger.info("Adding " + new AddressV4(item).toString() + " to root server list (" + item + ")");
                getRootsAddress().add(new AddressV4(item));
            } catch (AddressV4.InvalidIPv4Exception e) {
                e.printStackTrace();
            }
        }
        isRecursive = Config.isRecursive;
    }

    private static List<AddressV4> getRootsAddress() {
        return rootsAddress;
    }

    private static void setRootsAddress(List<AddressV4> rootsAddress) {
        Agent.rootsAddress = rootsAddress;
    }
}
