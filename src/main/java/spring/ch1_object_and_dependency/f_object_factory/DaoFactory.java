package spring.ch1_object_and_dependency.f_object_factory;

public class DaoFactory {

    public UserDao6 userDao6() {
        return new UserDao6(connectionMaker());
    }

    public ConnectionMaker connectionMaker() {
        return new DConnectionMaker();
    }
}
