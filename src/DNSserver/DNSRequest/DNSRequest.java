package DNSserver.DNSRequest;

import common.Domain;
import org.json.JSONObject;

abstract public class DNSRequest {
    public enum RequestHandleType {
        ITERATIVE, RECURSIVE
    }

    public enum RequestType {
        LOOKUP, ADD, UPDATE
    }

    Domain domain;
    RequestHandleType requestHandleType;
    RequestType requestType;

    abstract public JSONObject getJSONObjectFormat();

    String getDomain() {
        return domain.toString();
    }
    RequestHandleType getRequestHandleType() {
        return requestHandleType;
    }
    public RequestType getRequestType() {
        return requestType;
    }

    public static RequestHandleType getRequestHandleType(String dnsRequest) {
        JSONObject requestInJSON = new JSONObject(dnsRequest);

        String handleType = requestInJSON.get("requestHandleType").toString();
        switch (handleType) {
            case "ITERATIVE":
                return RequestHandleType.ITERATIVE;
            case "RECURSIVE":
                return RequestHandleType.RECURSIVE;
            default:
                // TODO: throw Exception!
                return null;
        }
    }

    public static RequestType getRequestType(String dnsRequest) {
        JSONObject requestInJSON = new JSONObject(dnsRequest);

        String requestType = requestInJSON.get("requestType").toString();
        switch (requestType) {
            case "LOOKUP":
                return RequestType.LOOKUP;
            case "ADD":
                return RequestType.ADD;
            case "UPDATE":
                return RequestType.UPDATE;
            default:
                // TODO: throw Exception!
                return null;
        }
    }
}