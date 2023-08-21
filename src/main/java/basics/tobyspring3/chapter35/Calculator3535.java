package basics.tobyspring3.chapter35;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator3535 {

    public int calcSum(String path) throws IOException {
        LineCallback3535 callback = new LineCallback3535<Integer>() {
            @Override
            public Integer doSomethingWithLine(Integer result, String line) throws IOException {
                result = result + Integer.valueOf(line);
                return result;
            }
        };
        int result = this.fileReadTemplate(path, 0, callback);
        return result;
    }


    public int calcMinus(String path) throws IOException {
        LineCallback3535 callback = new LineCallback3535<Integer>() {
            @Override
            public Integer doSomethingWithLine(Integer result, String line) throws IOException {
                result = result - Integer.valueOf(line);
                return result;
            }
        };
        Integer result = this.fileReadTemplate(path, 0, callback);
        return result;
    }

    public int calcMult(String path) throws IOException {
        LineCallback3535 callback = new LineCallback3535<Integer>() {
            @Override
            public Integer doSomethingWithLine(Integer result, String line) throws IOException {
                result = result * Integer.valueOf(line);
                return result;
            }
        };
        int result = this.fileReadTemplate(path, 1, callback);
        return result;
    }


    public double calcDiv(String path) throws IOException {
        LineCallback3535<Double> callback = new LineCallback3535<Double>() {
            @Override
            public Double doSomethingWithLine(Double result, String line) throws IOException {
                result = result / Double.valueOf(line);
                return result;
            }
        };
        double result = this.fileReadTemplate(path, 1.0, callback);
        return result;
    }


    public <T> T fileReadTemplate(String path, T initVal, LineCallback3535<T> callback) throws IOException {


        BufferedReader br = null;
        try {
            FileReader fr = new FileReader(path);
            br = new BufferedReader(fr);
            T result = initVal;
            String line = br.readLine();
            while (line != null) {
                result = callback.doSomethingWithLine(result, line);
                line = br.readLine();
            }
            return result;
        } catch (IOException e) {
            System.out.println(e);
            throw  e;
        } finally {
            if(br != null) {
                br.close();
            }
        }
    }
}


interface LineCallback3535<T> {
    public abstract T doSomethingWithLine(T result, String line) throws IOException;
}


//p. 257 - chapter3.5
//지금까지 int 만 썼었지.
//이제 double 을 쓰는 calcDiv() 를 하나 더 만들어 봤어.
//그럼 interface  랑 콜백 함수들 정의랑 하나 더 만들어야 하나?
//int 로 선언하는 거, double 로 선언하는 거?
//그러면 long 나오면?
//User 나오면?
//Book 나오면?
//새로운 데이터 타입마다 하나씩 새로 만들어야 하냐?
//그렇지 않다.
//타입도 그때 그때 내가 원하는 대로 지정해줄 수 있도록 했어.
//파라미터처럼 쓸 수 있지.
//interface 선언할 때 클래스 이름 옆에 <T> 써주기.
//interface MyInterface<MyType> { ~~~~ }
//그러고 MyInterface { ~~ } 안에서 정의하는 부분에는 T를 그냥 일반 자료형처럼 갖다 쓰면 됨.
//이제 이 인터페이스로 callback 오브젝트 만들 때는 어떻게 만드냐?
//MyInterface<MyType> callback = new MyInterface<MyType>() { @Overried ~~~ }
//그냥 우리 ArrayList 나 HashMap 같은 거 객체 만들 때 타입 지정해주는 거랑 똑같음.
//ArrayList<Integer> array = new ArrayList<>();
//이렇게 생성자 클래스 이름 옆에 <>로 타입 지정해주잖아.
//그러면 이제 구현체에서 template 이나 일반 클래스에서 가져다 쓸 때는 어떻게?
//메소드 선언할 때 메소드 접근제어자랑 메소드 return type 이랑 사이에 <T> 써주기.
//public <MyType> MyType myMethod(MyType param) { ~~~ }
//참고로 MyType 은 헷갈리지 말라고 일부러 길게 쓴 거고.
//보통 T / U / V 이런 식으로 타입을 지정함.
//이렇게까지 해서 템플릿/콜백을 더 깊이 이해하고 연습해 봤어.
//이제 다시 jdbc 기술로 넘어가보자.
//UserDao361.class 로 가기.

