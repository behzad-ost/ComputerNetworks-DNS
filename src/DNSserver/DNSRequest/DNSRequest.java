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
}