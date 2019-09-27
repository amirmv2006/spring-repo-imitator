package ir.amv.snippets.springrepoimitator;

import ir.amv.snippets.springrepoimitator.mine.MyAnnot;

@MyAnnot(beanName = "myClass")
public interface IMyClass {
    void sayHi(String name);
    void sayBye();
}
