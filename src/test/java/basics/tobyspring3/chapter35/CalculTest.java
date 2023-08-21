package basics.tobyspring3.chapter35;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;

public class CalculTest {


    @Test
    public void calcSumTest() throws IOException {
        Calculator353 calculator = new Calculator353();
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
