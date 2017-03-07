package common;

import java.util.LinkedList;
import java.util.List;

public class Domain {
    private String domain;
    public Domain(String domain) {
        this.domain = domain;
    }

    public static List<Domain> subDomains(String domain) {
        List<Domain> subDomains = new LinkedList<>();

        String[] parsed = domain.split("\\.");
        for (int i = 0; i < parsed.length; i++) {
            StringBuilder builder = new StringBuilder();
            for (int j = i; j < parsed.length; j++) {
                if (j == parsed.length-1)
                    builder.append(parsed[j]);
                else
                    builder.append(parsed[j]).append(".");
            }
            subDomains.add(new Domain(builder.toString()));
        }

        return subDomains;
    }

    @Override
    public String toString() {
        return domain;
    }
}
