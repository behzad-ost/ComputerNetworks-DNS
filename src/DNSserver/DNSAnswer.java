package DNSserver;

import org.json.JSONObject;

public class DNSAnswer {
    public DNSAnswer(String address, DNSRepository.DNSRecord.Type answerType, int ttl, StatusCode statusCode) {
        this.address = address;
        this.answerType = answerType;
        this.ttl = ttl;
        this.statusCode = statusCode;
    }

    public enum StatusCode {
        SUCCESS, NOT_IN_DATABASE
    }

    public DNSAnswer(JSONObject jsonAnswer) throws InvalidDNSJsonAnswerException {
        address = jsonAnswer.get("address").toString();

        String tmpAnswerType = jsonAnswer.get("answerType").toString();
        switch (tmpAnswerType) {
            case "A":
                answerType = DNSRepository.DNSRecord.Type.A;
                break;
            case "NS":
                answerType = DNSRepository.DNSRecord.Type.NS;
                break;
            default:
                throw new InvalidDNSJsonAnswerException();
        }

        ttl = Integer.parseInt(jsonAnswer.get("ttl").toString());

        String tmpStatusCode = jsonAnswer.get("statusCode").toString();
        switch (tmpStatusCode) {
            case "SUCCESS":
                statusCode = StatusCode.SUCCESS;
                break;
            case "NOT_IN_DATABASE":
                statusCode = StatusCode.NOT_IN_DATABASE;
                break;

            default:

        }
    }

    private String address;
    private DNSRepository.DNSRecord.Type answerType;
    private int ttl;
    private StatusCode statusCode;

    public String getAddress() {
        return address;
    }
    public DNSRepository.DNSRecord.Type getAnswerType() {
        return answerType;
    }
    public int getTtl() {
        return ttl;
    }
    public StatusCode getStatusCode() {
        return statusCode;
    }

    public JSONObject getJSONObjectFormat() {
        JSONObject dnsAnswer = new JSONObject();
        dnsAnswer.put("address", address);
        dnsAnswer.put("answerType", answerType);
        dnsAnswer.put("ttl", ttl);
        dnsAnswer.put("statusCode", statusCode);
        return dnsAnswer;
    }

    @Override
    public String toString() {
        return getJSONObjectFormat().toString();
    }

    public class InvalidDNSJsonAnswerException extends Throwable {
    }
}
