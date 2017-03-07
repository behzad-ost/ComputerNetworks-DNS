package common;

public class AddressV4 {
    private int part1;
    private int part2;
    private int part3;
    private int part4;
    private int port;

    public static AddressV4 getNullAddress() {
        try {
            return new AddressV4("0.0.0.0:0");
        } catch (InvalidIPv4Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public AddressV4(String ip, int port) throws InvalidIPv4Exception {
        String[] parsed = ip.split("\\.");
        part1 = Integer.parseInt(parsed[0]);
        if (part1 < 0 || part1 > 255)
            throw new InvalidIPv4Exception();

        part2 = Integer.parseInt(parsed[1]);
        if (part2 < 0 || part2 > 255)
            throw new InvalidIPv4Exception();

        part3 = Integer.parseInt(parsed[2]);
        if (part3 < 0 || part3 > 255)
            throw new InvalidIPv4Exception();

        part4 = Integer.parseInt(parsed[3]);
        if (part4 < 0 || part4 > 255)
            throw new InvalidIPv4Exception();

        if (port < 0 || port > 65635)
            throw new InvalidIPv4Exception();
        else
            this.port = port;
    }

    public AddressV4(String ipPort) throws InvalidIPv4Exception {
        String[] parsed = ipPort.split(":");
        AddressV4 tmp = new AddressV4(parsed[0], Integer.parseInt(parsed[1]));
        this.part1 = tmp.part1;
        this.part2 = tmp.part2;
        this.part3 = tmp.part3;
        this.part4 = tmp.part4;
        this.port = tmp.port;
    }

    @Override
    public String toString() {
        return String.valueOf(part1) + '.' +
                part2 + '.' +
                part3 + '.' +
                part4 + ':' +
                port;
    }

    public String getIP() {
        return part1 + "." + part2 + "." + part3 + "." + part4;
    }

    public int getPort() {
        return port;
    }

    public class InvalidIPv4Exception extends Throwable {

    }
}