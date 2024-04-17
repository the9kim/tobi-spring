package spring.ch6_aop.g_factory_bean;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import spring.ch6_aop.f_dynamic_proxy.TransactionHandler;

import java.lang.reflect.Proxy;

public class TxProxyFactoryBean implements FactoryBean<Object> {

    private Object target;
    private PlatformTransactionManager transactionManager;
    private String pattern;
    private Class<?> serviceInterface;


    @Override
    public Object getObject() {
        TransactionHandler transactionHandler = new TransactionHandler();
        transactionHandler.setTarget(this.target);
        transactionHandler.setTransactionManager(this.transactionManager);
        transactionHandler.setPattern(this.pattern);

        return Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{serviceInterface},
                transactionHandler
        );
    }

    @Override
    public Class<?> getObjectType() {
        return serviceInterface;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public void setServiceInterface(Class<?> serviceInterface) {
        this.serviceInterface = serviceInterface;
    }
}
