package spring.ch5_service_abstraction.d_transaction_demarcation;

import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcContext18 {

    public void workWithStrategy(StatementStrategy18 psStrategy) throws SQLException {
        PreparedStatement ps = null;

        try {
            ps = psStrategy.makePreparedStatement();

            ps.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public <T> T workWithStrategies(StatementStrategy18 psStrategy, ResultSetStrategy18<T> rsStrategy) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = psStrategy.makePreparedStatement();

            rs = ps.executeQuery();

            T object = rsStrategy.makeObject(rs);

            return object;

        } catch (SQLException | EmptyResultDataAccessException e) {
            throw e;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public <T> T workWithStrategiesWithList(StatementStrategy18 psStrategy, ResultSetStrategy18<T> rsStrategy) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = psStrategy.makePreparedStatement();

            rs = ps.executeQuery();

//            List<T> objects = new ArrayList<>();
//
//            while (rs.next()) {
//                objects.add(rsStrategy.makeObject(rs));
//            }
            T objects = rsStrategy.makeObject(rs);
            return objects;

        } catch (SQLException | EmptyResultDataAccessException e) {
            throw e;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public void update(Connection conn, final String sql, Object... args) throws SQLException {
        workWithStrategy(
                new StatementStrategy18() {
                    @Override
                    public PreparedStatement makePreparedStatement() throws SQLException {
                        PreparedStatement ps = conn.prepareStatement(sql);

                        for (int i = 0; i < args.length; i++) {
                            ps.setObject(i + 1, args[i]);
                        }
                        return ps;
                    }
                }
        );
    }

    public <T> T query(Connection conn, final String sql, ResultSetStrategy18<T> rsStrategy, Object[] args) throws SQLException {
        return workWithStrategiesWithList(
                new StatementStrategy18() {
                    @Override
                    public PreparedStatement makePreparedStatement() throws SQLException {
                        PreparedStatement ps = conn.prepareStatement(sql);

                        for (int i = 0; i < args.length; i++) {
                            ps.setObject(i + 1, args[i]);
                        }
                        return ps;
                    }
                },
                rsStrategy
        );
    }
}
