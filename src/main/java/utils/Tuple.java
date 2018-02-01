package utils;

public class Tuple<A,B> {

    private A a;
    private B b;

    public Tuple(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public A getLeft() {
        return a;
    }

    public B getRight() {
        return b;
    }
}
