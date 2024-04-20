package spring.ch6_aop.j_pointcut_expression;

import org.junit.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PointCutExpressionTest {

    @Test
    public void checkMethodSignature() throws NoSuchMethodException {
        System.out.println(String.class.getMethod("length"));
        System.out.println(Target.class.getMethod("minus", int.class, int.class));
    }

    static interface TargetInterface {
        void hello();

        void hello(String a);

        int minus(int a, int b) throws RuntimeException;

        int plus(int a, int b);
    }

    static class Target implements TargetInterface {
        @Override
        public void hello() {
        }

        @Override
        public void hello(String a) {
        }

        @Override
        public int minus(int a, int b) throws RuntimeException {
            return 0;
        }

        @Override
        public int plus(int a, int b) {
            return 0;
        }

        public void method() {
        }
    }

    static class Bean {
        public void method() throws RuntimeException {
        }
    }

    @Test
    public void methodSignaturePointcut() throws NoSuchMethodException {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(public int spring.ch6_aop.j_pointcut_expression.PointCutExpressionTest$Target.minus(int,int) throws java.lang.RuntimeException)");


        // Target.minus()
        assertThat(pointcut.getClassFilter().matches(Target.class)
                        && pointcut.getMethodMatcher().matches(Target.class.getMethod("minus", int.class, int.class), null)
                , is(true));

        // Target.plus
        assertThat(pointcut.getMethodMatcher().matches(Target.class.getMethod("plus", int.class, int.class), null), is(false));


        // Bean.method()
        assertThat(pointcut.getClassFilter().matches(Bean.class), is(false));
    }

}
