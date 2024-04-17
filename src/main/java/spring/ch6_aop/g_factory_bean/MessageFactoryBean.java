package spring.ch6_aop.g_factory_bean;

import org.springframework.beans.factory.FactoryBean;

public class MessageFactoryBean implements FactoryBean<Message> {

    String text;

    @Override
    public Message getObject() {
        return Message.of(text);
    }

    @Override
    public Class<? extends Message> getObjectType() {
        return Message.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    public void setText(String text) {
        this.text = text;
    }
}
