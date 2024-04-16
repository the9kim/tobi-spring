package spring.ch5_service_abstraction.h_mail_service;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import spring.ch5_service_abstraction.Level;

import java.sql.SQLException;
import java.util.List;


public class UserService8 {

    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

    private UserDao3 userDao3;
    private PlatformTransactionManager transactionManager;
    private MailSender mailSender;

    public void upgradeLevels() throws SQLException {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            List<User3> users = userDao3.getAll();
            for (User3 user : users) {
                if (canUpgradeLevel(user)) {
                    upgradeLevel(user);
                }
            }
            this.transactionManager.commit(status);
        } catch (SQLException e) {
            this.transactionManager.rollback(status);
            throw e;
        }
    }

    protected void upgradeLevel(User3 user) throws SQLException {
        user.upgradeLevel();
        userDao3.update(user);
        sendUpgradeEMail(user);
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

    /**
     * Using Javax mail API directly
     */
//    private void sendUpgradeEMail(User3 user) {
//        Properties props = new Properties();
//        props.put("mail.smtp.host", "mail.ksug.org");
//        Session s = Session.getInstance(props, null);
//
//        MimeMessage message = new MimeMessage(s);
//        try {
//            message.setFrom(new InternetAddress("user1@ksug.org"));
//            message.addRecipient(Message.RecipientType.TO,
//                    new InternetAddress(user.getEmail()));
//            message.setSubject("Upgrade 안내");
//            message.setText("사용자님의 등급이 " + user.getLevel().name() + "로 업그레이드되었습니다.");
//
//            Transport.send(message);
//        } catch (AddressException e) {
//            throw new RuntimeException(e);
//        } catch (MessagingException e) {
//            throw new RuntimeException(e);
//        }
//    }
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

    public void setUserDao3(UserDao3 userDao3) {
        this.userDao3 = userDao3;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }
}
