package ir.amv.snippets.springrepoimitator;

import ir.amv.snippets.springrepoimitator.mine.MyAnnot;

//@MyAnnot
public class MyClassImpl
        implements IMyClass {
    @Override
    public void sayHi(String name) {
        System.out.println("HiImpl");
    }

    @Override
    public void sayBye() {
        System.out.println("ByeImpl");
    }
}
