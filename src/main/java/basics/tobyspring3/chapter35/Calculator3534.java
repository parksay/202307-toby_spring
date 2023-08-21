package basics.tobyspring3.chapter35;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator3534 {


    public int calcSum(String path) throws IOException {
        LineCallback callback = new LineCallback() {
            @Override
            public Integer doSomethingWithLine(Integer result, String line) throws IOException {
                result = result + Integer.valueOf(line);
                return result;
            }
        };
        int result = this.fileReadTemplate(path, 0, callback);
        return result;
    }

    public int calcMult(String path) throws IOException {
        LineCallback callback = new LineCallback() {
            @Override
            public Integer doSomethingWithLine(Integer result, String line) throws IOException {
                result = result * Integer.valueOf(line);
                return result;
            }
        };
        int result = this.fileReadTemplate(path, 1, callback);
        return result;
    }

    public int calcMinus(String path) throws IOException {
        LineCallback callback = new LineCallback() {
            @Override
            public Integer doSomethingWithLine(Integer result, String line) throws IOException {
                result = result - Integer.valueOf(line);
                return result;
            }
        };
        Integer result = this.fileReadTemplate(path, 0, callback);
        return result;
    }


    public int fileReadTemplate(String path, int initVal, LineCallback callback) throws IOException {


        BufferedReader br = null;
        try {
            FileReader fr = new FileReader(path);
            br = new BufferedReader(fr);
            Integer result = initVal;
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


interface LineCallback {
    public abstract Integer doSomethingWithLine(Integer result, String line) throws IOException;
}


//p. 254 - chapter3.5
//이전에 보면 calcSum() / calcMult() / calcMinus() 등에서 만든 콜백 함수가 아래 틀로 비슷했지.
//Integer result = 0;
//String line = br.readLine();
//    while (line != null) {
//        result = result - Integer.valueOf(line);
//        line = br.readLine();
//    }
//return result;


//반복되고 비슷한 틀을 그냥 template 에 모두 때려박았어.
//그러고 실제로 바뀌는 내용 - while 문 안의 코드만 calback 으로 던져주도록 구족를 바꿨어.
//이전보다 훨씬 관심 있는 코드가 눈에 편하게 보이지.
//한 가지만 더 해보자면, return 데이터 타입도 좀 메소드마다 바뀔 수 있게 해줄 수는 없을까.
//예를 들어 calcDiv() 라는 기능을 추가하고 싶어.
//이 기능은 나눗셈 해주는 기능이야.
//근데 나누기는 int 데이터 타입으로는 안 될 거 아냐.
//double 이라든가 float 이나 필요할 거 아냐..
//그때 그때 data type 도 내가 지정할 수 있으면 좋겠어.
//그 기능이 Java5 부터 나왔다더군 generics 가.
//그거 적용해서 한번 해보자.
// 그렇게 해서 만든 게 Calculator3535.class