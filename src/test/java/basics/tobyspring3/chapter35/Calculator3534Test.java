package basics.tobyspring3.chapter35;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;

public class Calculator3534Test {

    Calculator3534 calculator;
    String path;

    @BeforeEach
    public void setUp() {
        Calculator3534 calculator = new Calculator3534();
        this.calculator = calculator;
        Class c = getClass();
        URL url = c.getResource("/numbers.txt");
        if(url == null) {
            System.out.println("url is null");
            return;
        }
        this.path = url.getPath();
    }

    @Test
    public void calcSumTest() throws IOException {
        int sum = calculator.calcSum(path);
        System.out.println("sum = " + sum);
    }


    @Test
    public void calcMultTest() throws IOException {
        int sum = calculator.calcMult(path);
        System.out.println("sum = " + sum);
    }


    @Test
    public void calcMinusTest() throws IOException {
        int sum = calculator.calcMinus(path);
        System.out.println("sum = " + sum);
    }
}



