package spring.ch7_sql_separation.f_interface_separation;

public interface SqlRegistry {

    void registerSql(String key, String sql);

    String findSql(String key) throws SqlNotFoundException;


    public class SqlNotFoundException extends RuntimeException {

        public SqlNotFoundException(String message) {
            super(message);
        }

        public SqlNotFoundException(Throwable cause) {
            super(cause);
        }
    }
}
