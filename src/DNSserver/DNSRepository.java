package DNSserver;

import exceptions.DomainNotFoundInDBException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class DNSRepository {
    private List<DNSRecord> records;

    public static class DNSRecord {
        DNSRecord(String name, List<String> values, Type recordType) {
            this.name = name;
            this.values = values;
            this.recordType = recordType;
            this.roundRobbinIndex = 0;
        }

        String getName() {
            return name;
        }

        DNSRepositoryAnswer getAnswer() {
            String answer = values.get(roundRobbinIndex);
            roundRobbinIndex = (roundRobbinIndex +1) % values.size();
            return new DNSRepositoryAnswer(name, answer, recordType);
        }

        public enum Type {
            A, NS
        }
        String name;
        List<String> values;
        Type recordType;

        int roundRobbinIndex;
    }

    public DNSRepository() {
        records = new LinkedList<>();
    }

    public static class DNSRepositoryAnswer {
        public DNSRepositoryAnswer(String name, String value, DNSRecord.Type answerType) {
            this.name = name;
            this.value = value;
            this.answerType = answerType;
        }

        String name;
        String value;
        DNSRecord.Type answerType;

        public String getName() {
            return name;
        }
        public String getValue() {
            return value;
        }
        public DNSRecord.Type getAnswerType() {
            return answerType;
        }
    }

    public DNSRepositoryAnswer searchRepo(String name) throws DomainNotFoundInDBException {
        for (DNSRecord record: records) {
            if (record.getName().equals(name)) {
                return record.getAnswer();
            }
        }
        throw new DomainNotFoundInDBException();
    }

    public void addDataInRepo(String name, List<String> values, DNSRecord.Type type) {
        // TODO: first verify it exists or not!
        records.add(new DNSRecord(name, values, type));
    }
}
