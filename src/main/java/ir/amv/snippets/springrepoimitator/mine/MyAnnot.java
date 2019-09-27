package ir.amv.snippets.springrepoimitator.mine;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface MyAnnot {
    String value();
}
