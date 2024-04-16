package spring.ch5_service_abstraction.h_mail_service;

import org.junit.Before;
import org.junit.Test;
import spring.ch5_service_abstraction.Level;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class User3Test {

    User3 user;

    @Before
    public void setUp() {
        user = new User3();
    }

    @Test
    public void upgradeLevel() {
        Level[] levels = Level.values();

        for (Level level : levels) {
            if (level.nextLevel() == null) {
                continue;
            }
            user.setLevel(level);
            user.upgradeLevel();
            assertThat(user.getLevel(), is(level.nextLevel()));
        }
    }

    @Test(expected = IllegalStateException.class)
    public void cannotUpgradeLevel() {
        Level[] levels = Level.values();

        for (Level level : levels) {
            if (level.nextLevel() != null) {
                continue;
            }
            user.setLevel(level);
            user.upgradeLevel();
        }
    }
}
