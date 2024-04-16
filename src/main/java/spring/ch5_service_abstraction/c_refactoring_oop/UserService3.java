package spring.ch5_service_abstraction.c_refactoring_oop;

import spring.ch5_service_abstraction.Level;
import spring.ch5_service_abstraction.User2;
import spring.ch5_service_abstraction.a_level_management_function.UserDao2;

import java.util.List;

public class UserService3 {

    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;
    UserDao2 userDao2;

    public void upgradeLevels() {
        List<User2> users = userDao2.getAll();

        for (User2 user : users) {
            if (canUpgradeLevel(user)) {
                upgradeLevel(user);
            }
        }
    }

    private void upgradeLevel(User2 user) {
        user.upgradeLevel();
        userDao2.update(user);
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
}
