package spring.ch5_service_abstraction.e_transaction_synchronization;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import spring.ch5_service_abstraction.Level;
import spring.ch5_service_abstraction.User2;
import spring.ch5_service_abstraction.a_level_management_function.UserDao2;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserService5 {

    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

    UserDao2 userDao2;
    DataSource dataSource;

    public void upgradeLevels() throws SQLException {
        TransactionSynchronizationManager.initSynchronization();
        Connection conn = DataSourceUtils.getConnection(dataSource);
        conn.setAutoCommit(false);

        try {
            List<User2> users = userDao2.getAll();
            for (User2 user : users) {
                if (canUpgradeLevel(user)) {
                    upgradeLevel(user);
                }
            }
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            DataSourceUtils.releaseConnection(conn, dataSource);
            TransactionSynchronizationManager.unbindResource(this.dataSource);
            TransactionSynchronizationManager.clearSynchronization();
        }
    }

    protected void upgradeLevel(User2 user) throws SQLException {
        user.upgradeLevel();
        userDao2.update(user);
    }

    public boolean canUpgradeLevel(User2 user) {
        Level currentLevel = user.getLevel();
        switch (currentLevel) {
            case BASIC:
                return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
            case SILVER:
                return (user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD);
            case GOLD:
                return false;
            default:
                throw new IllegalArgumentException("Unknown Level: " + currentLevel);
        }
    }

    public void add(User2 user) {
        if (user.getLevel() == null) {
            user.setLevel(Level.BASIC);
            userDao2.add(user);
        } else {
            userDao2.add(user);
        }
    }

    public void setUserDao2(UserDao2 userDao2) {
        this.userDao2 = userDao2;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
