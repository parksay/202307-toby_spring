package basics.tobyspring3.chapter35;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;

public class Calculator3532Test {


    @Test
    public void calcSumTest() throws IOException {
        Calculator3532 calculator = new Calculator3532();
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


    @Test
    public void calcMultTest() throws IOException {
        Calculator3532 calculator = new Calculator3532();
        Class c = getClass();
        URL url = c.getResource("/numbers.txt");
        if(url == null) {
            System.out.println("url is null");
            return;
        }
        String path = url.getPath();
        int sum = calculator.calcMult(path);
        System.out.println("sum = " + sum);
    }


    @Test
    public void calcMinusTest() throws IOException {
        Calculator3532 calculator = new Calculator3532();
        Class c = getClass();
        URL url = c.getResource("/numbers.txt");
        if(url == null) {
            System.out.println("url is null");
            return;
        }
        String path = url.getPath();
        int sum = calculator.calcMinus(path);
        System.out.println("sum = " + sum);
    }
}


//리팩토링을 해두어야 하는 대상은 애플리케이션 코드뿐만이 아니라고 했지.
//테스트 코드도 리팩토링을 해줘야 해.
//애플리케이션에 수정이 발생하면 테스트 코드도 따라서 수정해줘야 하니까.
//어차피 테스트도 개발을 진행하면서 계속 만들어야 하고 계속 실행해야 하고 계속 수정해야 해.
//애플리케이션을 운영하는 한에는 테스트 코드도 계속 함께 가져가야 하는 영역이야.
//내가 나중에 편하려면 계속 같이 수정해 주는 게 좋겠지.
//반복되는 부분, URL 이나 테스트 오브젝트나 등등은 클래스 변수로 뽑아서 공통으로 가져다 쓰기


