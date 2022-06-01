package com.yzc.bilibili;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class BaseTestRule implements TestRule {

    @Override
    public Statement apply(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                // 想要在测试方法运行之前做一些事情，就在base.evaluate()之前做
                String className = description.getClassName();
                String methodName = description.getMethodName();
                System.out.println("\t[Start == > " + className + "$" + methodName + "]");

                base.evaluate();  //这其实就是运行测试方法

                //想要在测试方法运行之后做一些事情，就在base.evaluate()之后做
                System.out.println("\t[End   == > " + className + "$" + methodName + "]");
                System.out.println();
            }
        };
    }
}
