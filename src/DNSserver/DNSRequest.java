package DNSserver;

import common.Domain;
import org.json.JSONObject;

public class DNSRequest {
    public enum RequestType {
        Iterative, Recursive;
    }

    private Domain domain;
    private RequestType requestType;

    DNSRequest(String domain, RequestType requestType) {
        this.domain = new Domain(domain);
        this.requestType = requestType;
    }

    DNSRequest(JSONObject dnsRequestJSON) {
        domain = new Domain(dnsRequestJSON.get("domain").toString());

        String tmpRequestType = dnsRequestJSON.get("requestType").toString();
        switch (tmpRequestType) {
            case "Iterative":
                requestType = RequestType.Iterative;
                break;
            case "Recursive":
                requestType = RequestType.Recursive;
                break;
            default:
                // TODO: throw an Exception! (first define it!)
        }
    }

    JSONObject getJSONObjectFormat() {
        JSONObject dnsRequest = new JSONObject();
        dnsRequest.put("domain", domain.toString());
        dnsRequest.put("requestType", requestType);
        return dnsRequest;
    }

    String getDomain() {
        return domain.toString();
    }

    RequestType getRequestType() {
        return requestType;
    }
}
