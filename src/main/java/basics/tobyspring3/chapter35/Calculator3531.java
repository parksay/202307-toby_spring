package basics.tobyspring3.chapter35;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator3531 {


    public int calcSum(String path) throws IOException {
        BufferedReader br = null;
        try {
            FileReader fr = new FileReader(path);
            br = new BufferedReader(fr);
            Integer sum = 0;
            String line = br.readLine();
            while (line != null) {
                sum = sum + Integer.valueOf(line);
                line = br.readLine();
            }
            return sum;
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

//
// 원래 아래처럼 있었거든
//public int calcSum(String path) throws IOException {
//    BufferedReader br = new BufferedReader(new FileReader(path));
//    Integer sum = 0;
//    String line = null;
//    while ((line = br.readLine()) != null) {
//        sum = sum + Integer.valueOf(line);
//    }
//    br.close();
//    return sum;
//}
//근데 파일도 DB 풀이랑 마찬가지로 열고 나서 꼭 닫아줘야 함.
//메소드 실행하다가 중간에 예외가 발생하면 파일을 닫지 않고 메소드를 빠져나와버리는 일이 발생할 수 있음.
//그래서 try/catch/finally 로 감싸서 항상 close() 되도록 해줬음.
//
//근데 이제 여기서 기능 추가 요청이 들어왔다 쳐보자.
//만약에 클라이언트가 기능 요건에 더하기만 말고 곱하기도 넣어달래.
//그리고 앞으로 빼기.나누기.루트 등등 계속 추가될 수 있대.
//이러면 전체적인 구조 수정이 들어가야겠지.
//반복되는 걸 뽑고 유지 보수 편하도록 객체지향 설계로 바꿔보자.

