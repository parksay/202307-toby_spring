package basics.tobyspring3.chapter35;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;

public class Calculator3531Test {


    @Test
    public void calcSumTest() throws IOException {
        Calculator3531 calculator = new Calculator3531();
        Class c = getClass();
        URL url = c.getResource("/numbers.txt");
        if(url == null) {
            System.out.println("url is null");
            return;
        }
        String path = url.getPath();
        int sum = calculator.calcSum(path);
        System.out.println("sum = " + sum);
    }
}
