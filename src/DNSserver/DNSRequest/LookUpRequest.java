package DNSserver.DNSRequest;

import common.Domain;
import org.json.JSONObject;

public class LookUpRequest extends DNSRequest {
    public LookUpRequest(String domain, RequestHandleType requestHandleType) {
        requestType = RequestType.LookUp;
        this.domain = new Domain(domain);
        this.requestHandleType = requestHandleType;
    }

    public LookUpRequest(JSONObject lookUpRequest) {
        requestType = RequestType.LookUp;
        domain = new Domain(lookUpRequest.get("domain").toString());

        String tmpRequestHandleType = lookUpRequest.get("requestType").toString();
        switch (tmpRequestHandleType) {
            case "Iterative":
                requestHandleType = RequestHandleType.Iterative;
                break;
            case "Recursive":
                requestHandleType = RequestHandleType.Recursive;
                break;
            default:
                // TODO: throw an Exception! (first define it!)
        }
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
