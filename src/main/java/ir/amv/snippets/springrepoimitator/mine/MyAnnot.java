package ir.amv.snippets.springrepoimitator.mine;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface MyAnnot {
    String beanName();
}
