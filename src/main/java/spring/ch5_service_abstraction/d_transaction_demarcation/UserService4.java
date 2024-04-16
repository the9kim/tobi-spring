package spring.ch5_service_abstraction.d_transaction_demarcation;

import spring.ch5_service_abstraction.Level;
import spring.ch5_service_abstraction.User2;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserService4 {

    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

    UserDao19 userDao19;
    DataSource dataSource;


    public void upgradeLevels() throws SQLException {
        Connection conn = null;

        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);

            List<User2> users = userDao19.getAll(conn);

            for (User2 user : users) {
                if (canUpgradeLevel(user)) {
                    upgradeLevel(user, conn);
                }
            }
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {

                }
            }
        }
    }

    protected void upgradeLevel(User2 user, Connection conn) throws SQLException {
        user.upgradeLevel();
        userDao19.update(user, conn);
    }

    public boolean canUpgradeLevel(User2 user) {
        Level currentLevel = user.getLevel();
        switch(currentLevel) {
            case BASIC: return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
            case SILVER: return (user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD);
            case GOLD: return false;
            default:
                throw new IllegalArgumentException("Unknown Level: " + currentLevel);
        }
    }

    public void add(User2 user) throws SQLException {
        Connection conn = null;

        try {
            conn = dataSource.getConnection();
            if (user.getLevel() == null) {
                user.setLevel(Level.BASIC);
                userDao19.add(user, conn);
            } else {
                userDao19.add(user, conn);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public void setUserDao19(UserDao19 userDao) {
        this.userDao19 = userDao;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
