package spring.ch7_sql_separation.c_sql_service;

public interface SqlService {

    String getSql(String key);

    public class SqlRetrievalFailureException extends RuntimeException {
        public SqlRetrievalFailureException(String message) {
            super(message);
        }

        public SqlRetrievalFailureException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
