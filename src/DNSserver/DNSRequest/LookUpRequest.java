package DNSserver.DNSRequest;

import common.Domain;
import org.json.JSONObject;

public class LookUpRequest extends DNSRequest {
    public LookUpRequest(String domain, RequestHandleType requestHandleType) {
        requestType = RequestType.LOOKUP;
        this.domain = new Domain(domain);
        this.requestHandleType = requestHandleType;
    }

    public LookUpRequest(JSONObject lookUpRequest) {
        requestType = RequestType.LOOKUP;
        domain = new Domain(lookUpRequest.get("domain").toString());

        String tmpRequestHandleType = lookUpRequest.get("requestHandleType").toString();
        switch (tmpRequestHandleType) {
            case "ITERATIVE":
                requestHandleType = RequestHandleType.ITERATIVE;
                break;
            case "RECURSIVE":
                requestHandleType = RequestHandleType.RECURSIVE;
                break;
            default:
                // TODO: throw an Exception! (first define it!)
        }
    }

    public String getDomain() {
        return domain.toString();
    }

    @Override
    public JSONObject getJSONObjectFormat() {
        JSONObject dnsRequest = new JSONObject();
        dnsRequest.put("requestType", requestType);
        dnsRequest.put("domain", domain.toString());
        dnsRequest.put("requestHandleType", requestHandleType);
        return dnsRequest;
    }
}
