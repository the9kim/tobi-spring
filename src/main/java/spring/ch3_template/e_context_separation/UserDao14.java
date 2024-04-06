package spring.ch3_template.e_context_separation;


import org.springframework.dao.EmptyResultDataAccessException;
import spring.ch1_object_and_dependency.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao14 {

    private DataSource dataSource;

    public void add(final User user) throws SQLException {

        // Local Class
        class StatementStrategy14Add implements StatementStrategy14 {
            @Override
            public PreparedStatement makePreparedStatement(Connection conn) throws SQLException {
                String sql = "INSERT INTO users(id, name, password) VALUES(?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, user.getId());
                ps.setString(2, user.getName());
                ps.setString(3, user.getPassword());
                return ps;
            }
        }

        StatementStrategy14 psStrategy = new StatementStrategy14Add();
        jdbcContextWithStatementStrategy(psStrategy);
    }

    public User get(String id) throws SQLException {

        class StatementStrategy14Get implements StatementStrategy14 {
            @Override
            public PreparedStatement makePreparedStatement(Connection conn) throws SQLException {
                String sql = "SELECT * FROM users WHERE id = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, id);
                return ps;
            }
        }

        class ResultSetStrategyGet14 implements ResultSetStrategy14 {
            @Override
            public Object makeObject(ResultSet rs) throws SQLException {
                User user = null;

                if (rs.next()) {
                    user = new User();
                    user.setId(rs.getString("id"));
                    user.setName(rs.getString("name"));
                    user.setPassword(rs.getString("password"));
                }
                if (user == null) {
                    throw new EmptyResultDataAccessException(1);
                }
                return user;
            }
        }

        StatementStrategy14Get psStrategy = new StatementStrategy14Get();
        ResultSetStrategyGet14 rsStrategy = new ResultSetStrategyGet14();
        return (User) jdbcContextWithStatementStrategy(psStrategy, rsStrategy);
    }

    public void deleteAll() throws SQLException {

        class StatementStrategy14DeleteAll implements StatementStrategy14 {
            @Override
            public PreparedStatement makePreparedStatement(Connection conn) throws SQLException {
                String sql = "DELETE FROM users";
                return conn.prepareStatement(sql);
            }
        }

        StatementStrategy14DeleteAll psStrategy = new StatementStrategy14DeleteAll();
        jdbcContextWithStatementStrategy(psStrategy);
    }

    public int getCount() throws SQLException {

        class StatementStrategy14GetCount implements StatementStrategy14 {
            @Override
            public PreparedStatement makePreparedStatement(Connection conn) throws SQLException {
                String sql = "SELECT COUNT(users.id) AS count FROM users";
                return conn.prepareStatement(sql);
            }
        }

        class ResultSetStrategyGetCount14 implements ResultSetStrategy14 {
            @Override
            public Object makeObject(ResultSet resultSet) throws SQLException {
                resultSet.next();
                return resultSet.getInt("count");
            }
        }

        StatementStrategy14GetCount psStrategy = new StatementStrategy14GetCount();
        ResultSetStrategyGetCount14 rsStrategy = new ResultSetStrategyGetCount14();
        return (int) jdbcContextWithStatementStrategy(psStrategy, rsStrategy);
    }

    public void jdbcContextWithStatementStrategy(StatementStrategy14 strategy) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = dataSource.getConnection();

            ps = strategy.makePreparedStatement(conn);

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

    public Object jdbcContextWithStatementStrategy(StatementStrategy14 PsStrategy, ResultSetStrategy14 rsStrategy) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();

            ps = PsStrategy.makePreparedStatement(conn);

            rs = ps.executeQuery();

            Object object = rsStrategy.makeObject(rs);

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

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
