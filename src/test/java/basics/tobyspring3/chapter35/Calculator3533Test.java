package basics.tobyspring3.chapter35;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;

public class Calculator3533Test {

    Calculator3533 calculator;
    String path;

    @BeforeEach
    public void setUp() {
        Calculator3533 calculator = new Calculator3533();
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



//테스트 코드가 아주 보기 좋고 깔끔해졌지.
//메소드마다 테스트 하고 싶은 그 특정 관심 있는 메소드가 또렷이 드러나게끔 됐어.
//그거 말고는 군더더기 코드가 싹 사라졌지.
//어디로 갔냐하면 공통 메소드인 @BeforeEach setUp() 메소드로 다 들어가 있어.
//필요한 건 다 클래스 변수에서 받아다가 쓰고 있지.
//앞으로 더 기능이 추가돼서 테스트해야 하는 메소드가 늘어나거나 해도 걱정 없지. 추가하기 편하지.
