package basics.tobyspring3.chapter35;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator3532 {


    public int calcSum(String path) throws IOException {
        BufferedReader br = null;
        try {
            FileReader fr = new FileReader(path);
            br = new BufferedReader(fr);
            Integer result = 0;
            String line = br.readLine();
            while (line != null) {
                result = result + Integer.valueOf(line);
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

    public int calcMult(String path) throws IOException {
        BufferedReader br = null;
        try {
            FileReader fr = new FileReader(path);
            br = new BufferedReader(fr);
            Integer result = 1;
            String line = br.readLine();
            while (line != null) {
                result = result * Integer.valueOf(line);
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

    public int calcMinus(String path) throws IOException {
        BufferedReader br = null;
        try {
            FileReader fr = new FileReader(path);
            br = new BufferedReader(fr);
            Integer result = 0;
            String line = br.readLine();
            while (line != null) {
                result = result - Integer.valueOf(line);
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


//이렇게 보니까 어때.
//try/catch/finally 라는 틀은 거의 비슷하지.
//틀 안에서도 catch 랑 finally 내용은 아예 똑같아.
//좀 다른 건 try 안에 있는 부분만 달라.
//그러면 전체적인 틀을 공통적으로 유지하면서 안에 내용물만 조금씩 바꿔가면서 재사용할 수는 없을까?
//그게 바로 템플릿/콜백 패턴이지.
//try/catch/finally 부분은 아래 공통 메소드로 빼 놓고, 실제 calcSum() / calcDiv() / calcMult() 등에서는 try 부분 구현해서 넣어주로독 수정하기.
