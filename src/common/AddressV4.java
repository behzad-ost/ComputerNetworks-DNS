package common;

public class AddressV4 {
    private int part1;
    private int part2;
    private int part3;
    private int part4;
    private int port;

    public AddressV4(String ip, int port) throws InvalidIPv4Exception {
        String[] parsed = ip.split(".");
        part1 = Integer.parseInt(parsed[0]);
        if (part1 < 0 || part1 > 255)
            throw new InvalidIPv4Exception();

        part2 = Integer.parseInt(parsed[0]);
        if (part2 < 0 || part2 > 255)
            throw new InvalidIPv4Exception();

        part3 = Integer.parseInt(parsed[0]);
        if (part3 < 0 || part3 > 255)
            throw new InvalidIPv4Exception();

        part4 = Integer.parseInt(parsed[0]);
        if (part4 < 0 || part4 > 255)
            throw new InvalidIPv4Exception();

        if (port < 0 || port > 65635)
            throw new InvalidIPv4Exception();
        else
            this.port = port;
    }

    public AddressV4(String ipPort) throws InvalidIPv4Exception {
        String[] parsed = ipPort.split(":");
        new AddressV4(parsed[0], Integer.parseInt(parsed[1]));
    }

    @Override
    public String toString() {
        return String.valueOf(part1) + '.' +
                part2 + '.' +
                part3 + '.' +
                part4 + ':' +
                port;
    }

    public class InvalidIPv4Exception extends Throwable {

    }
}