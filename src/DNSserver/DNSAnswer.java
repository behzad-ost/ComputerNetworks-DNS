package DNSserver;

import common.AddressV4;
import org.json.JSONObject;

public class DNSAnswer {
    public DNSAnswer(AddressV4 ip, DNSRepository.DNSRecord.Type answerType, int ttl, StatusCode statusCode) {
        this.ip = ip;
        this.answerType = answerType;
        this.ttl = ttl;
        this.statusCode = statusCode;
    }

    public enum StatusCode {
        success, notInDatabase
    }

    public DNSAnswer(JSONObject jsonAnswer) throws InvalidDNSjsonAnswerException {
        try {
            ip = new AddressV4(jsonAnswer.get("AddressV4").toString());
        } catch (AddressV4.InvalidIPv4Exception e) {
            throw new InvalidDNSjsonAnswerException();
        }

        String tmpAnswerType = jsonAnswer.get("Type").toString();
        switch (tmpAnswerType) {
            case "A":
                answerType = DNSRepository.DNSRecord.Type.A;
                break;
            case "NS":
                answerType = DNSRepository.DNSRecord.Type.NS;
                break;
            default:
                throw new InvalidDNSjsonAnswerException();
        }

        ttl = Integer.parseInt(jsonAnswer.get("ttl").toString());
    }

    private AddressV4 ip;
    private DNSRepository.DNSRecord.Type answerType;
    private int ttl;
    private StatusCode statusCode;

    public DNSRepository.DNSRecord.Type getAnswerType() {
        return answerType;
    }
    public AddressV4 getIp() {
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
