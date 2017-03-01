package DNSserver;

import common.IPv4;
import org.json.JSONObject;

public class DNSAnswer {
    public DNSAnswer(IPv4 ip, DNSAnswer.Type answerType, int ttl) {
        this.ip = ip;
        this.answerType = answerType;
        this.ttl = ttl;
    }

    public DNSAnswer(JSONObject jsonAnswer) throws InvalidDNSjsonAnswerException {
        try {
            ip = new IPv4(jsonAnswer.get("IPv4").toString());
        } catch (IPv4.InvalidIPv4Exception e) {
            throw new InvalidDNSjsonAnswerException();
        }

        String tmpAnswerType = jsonAnswer.get("Type").toString();
        switch (tmpAnswerType) {
            case "A":
                answerType = Type.A;
                break;
            case "NS":
                answerType = Type.NS;
                break;
            default:
                throw new InvalidDNSjsonAnswerException();
        }

        ttl = Integer.parseInt(jsonAnswer.get("ttl").toString());
    }

    public enum Type {
        A, NS
    }
    private IPv4 ip;
    private Type answerType;
    private int ttl;

    public Type getAnswerType() {
        return answerType;
    }
    public IPv4 getIp() {
        return ip;
    }
    public int getTtl() {
        return ttl;
    }

    JSONObject getJSONObjectFormat() {
        JSONObject dnsAnswer = new JSONObject();
        dnsAnswer.put("ip", ip.toString());
        dnsAnswer.put("answerType", answerType);
        dnsAnswer.put("ttl", ttl);
        return dnsAnswer;
    }

    public class InvalidDNSjsonAnswerException extends Throwable {
    }
}
