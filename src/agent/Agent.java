package agent;

import common.AddressV4;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class Agent {
    static List<AddressV4> rootsAddress;
    private static boolean isRecursive;
    public static void main(String[] args) throws IOException, AddressV4.InvalidIPv4Exception {
        setConfiguration();

        ServerSocket serverSocket = new ServerSocket();
        while (true) {
            Socket clientSocket = serverSocket.accept();
            if (isRecursive)
                new Thread(new RecursiveClientHandler(clientSocket)).start();
            else
                new Thread(new IterativeClientHandler(clientSocket)).start();
        }
    }

    private static void setConfiguration() {
        setRootsAddress(new LinkedList<>());
        for (String item: Config.rootsAddress) {
            try {
                getRootsAddress().add(new AddressV4(item));
            } catch (AddressV4.InvalidIPv4Exception e) {
                e.printStackTrace();
            }
        }
        isRecursive = Config.isRecursive;
    }

    public static List<AddressV4> getRootsAddress() {
        return rootsAddress;
    }

    public static void setRootsAddress(List<AddressV4> rootsAddress) {
        Agent.rootsAddress = rootsAddress;
    }
}
