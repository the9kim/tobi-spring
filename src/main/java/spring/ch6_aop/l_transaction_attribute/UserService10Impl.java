package spring.ch6_aop.l_transaction_attribute;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import spring.ch5_service_abstraction.Level;
import spring.ch5_service_abstraction.h_mail_service.User3;
import spring.ch5_service_abstraction.h_mail_service.UserDao3;
import spring.ch6_aop.a_extraction_of_transaction.UserService;

import java.sql.SQLException;
import java.util.List;


public class UserService10Impl implements UserService2 {

    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

    private UserDao3 userDao3;
    private MailSender mailSender;

    public void upgradeLevels() throws SQLException {
        List<User3> users = userDao3.getAll();
        for (User3 user : users) {
            if (canUpgradeLevel(user)) {
                upgradeLevel(user);
            }
        }
    }

    public boolean canUpgradeLevel(User3 user) {
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

    protected void upgradeLevel(User3 user) throws SQLException {
        user.upgradeLevel();
        userDao3.update(user);
        sendUpgradeEMail(user);
    }

    private void sendUpgradeEMail(User3 user) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom("useradmin@ksug.org");
        mailMessage.setSubject("Upgrade 안내");
        mailMessage.setText("사용자님의 등급이 " + user.getLevel().name() + "로 업그레이드되었습니다.");

        mailSender.send(mailMessage);
    }

    public void add(User3 user) {
        if (user.getLevel() == null) {
            user.setLevel(Level.BASIC);
            userDao3.add(user);
        } else {
            userDao3.add(user);
        }
    }

    public User3 get(String userId) {
        return userDao3.get(userId);
    }

    public List<User3> getAll() {
        return userDao3.getAll();
    }

    public int getCount() {
        return userDao3.getCount();
    }

    public void update(User3 user) {
        userDao3.update(user);
    }

    public void deleteAll() {
        userDao3.deleteAll();
    }

    public void setUserDao3(UserDao3 userDao3) {
        this.userDao3 = userDao3;
    }

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }
}
