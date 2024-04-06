package spring.ch3_template.h_generics;

import org.springframework.dao.EmptyResultDataAccessException;
import spring.ch3_template.g_embedded_callback.ResultSetStrategy16;
import spring.ch3_template.g_embedded_callback.StatementStrategy16;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcContext17 {

    DataSource dataSource;

    public void workWithStatementStrategy(StatementStrategy17 psStrategy) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = dataSource.getConnection();

            ps = psStrategy.makePreparedStatement(conn);

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
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }

    }

    public <T> T workWithStatementStrategy(StatementStrategy17 psStrategy, ResultSetStrategy17<T> rsStrategy) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();

            ps = psStrategy.makePreparedStatement(conn);

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
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public void executeSql(final String sql, Object... args) throws SQLException {
        workWithStatementStrategy(
                new StatementStrategy17() {
                    @Override
                    public PreparedStatement makePreparedStatement(Connection conn) throws SQLException {
                        PreparedStatement ps = conn.prepareStatement(sql);

                        for (int i = 0; i < args.length; i++) {
                            ps.setObject(i + 1, args[i]);
                        }
                        return ps;
                    }
                }
        );
    }

    public <T> T executeSqlWithRowMapping(final String sql, ResultSetStrategy17<T> rsStrategy, Object[] args) throws SQLException {
        return workWithStatementStrategy(
                new StatementStrategy17() {
                    @Override
                    public PreparedStatement makePreparedStatement(Connection conn) throws SQLException {
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

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
