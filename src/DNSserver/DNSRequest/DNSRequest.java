package DNSserver.DNSRequest;

import common.Domain;
import org.json.JSONObject;

abstract public class DNSRequest {
    public enum RequestHandleType {
        Iterative, Recursive;
    }

    public enum RequestType {
        LookUp, AddNewDomain, UpdateCurrentDomain
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
            case "Iterative":
                return RequestHandleType.Iterative;
            case "Recursive":
                return RequestHandleType.Recursive;
            default:
                // TODO: throw Exception!
                return null;
        }
    }

    public static RequestType getRequestType(String dnsRequest) {
        JSONObject requestInJSON = new JSONObject(dnsRequest);

        String requestType = requestInJSON.get("requestType").toString();
        switch (requestType) {
            case "LookUp":
                return RequestType.LookUp;
            case "AddNewDomain":
                return RequestType.AddNewDomain;
            case "UpdateCurrentDomain":
                return RequestType.UpdateCurrentDomain;
            default:
                // TODO: throw Exception!
                return null;
        }
    }
}