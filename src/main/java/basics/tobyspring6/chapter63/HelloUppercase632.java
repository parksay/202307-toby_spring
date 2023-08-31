package basics.tobyspring6.chapter63;

public class HelloUppercase632 implements Hello632 {
    private final Hello632 hello;

    public HelloUppercase632(Hello632 hello) {
        this.hello = hello;
    }
    public String sayHello(String name) {
        return hello.sayHello(name).toUpperCase();
    }

    public String sayHi(String name) {
        return hello.sayHi(name).toUpperCase();
    }

    public String sayThankYou(String name) {
        return hello.sayThankYou(name).toUpperCase();
    }

}
