package spring.ch5_service_abstraction.b_level_management_function;

import spring.ch5_service_abstraction.Level;
import spring.ch5_service_abstraction.User2;
import spring.ch5_service_abstraction.a_level_management_function.UserDao2;

import java.util.List;

public class UserService2 {

    UserDao2 userDao2;

    public void upgradeLevels() {
        List<User2> users = userDao2.getAll();

        Boolean changed = null;
        for (User2 user : users) {
            if (user.getLevel() == Level.BASIC && user.getLogin() >= 50) {
                user.setLevel(Level.SILVER);
                changed = true;
            } else if (user.getLevel() == Level.SILVER && user.getRecommend() >= 30) {
                user.setLevel(Level.GOLD);
                changed = true;
            } else {
                changed = false;
            }
            if (changed == true) {
                userDao2.update(user);
            }
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
