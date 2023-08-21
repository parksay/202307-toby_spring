package basics.tobyspring3.chapter35;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator3533 {


    public int calcSum(String path) throws IOException {
        BufferedReaderCallBack callBack = new BufferedReaderCallBack() {
            @Override
            public Integer doSomethingWithReader(BufferedReader br) throws IOException {
                Integer sum = 0;
                String line = br.readLine();
                while (line != null) {
                    sum = sum + Integer.valueOf(line);
                    line = br.readLine();
                }
                return sum;
            }
        };
        int result = this.fileReadTemplate(path, callBack);
        return result;
    }

    public int calcMult(String path) throws IOException {
        BufferedReaderCallBack callback = new BufferedReaderCallBack() {
            @Override
            public Integer doSomethingWithReader(BufferedReader br) throws IOException {
                Integer sum = 1;
                String line = br.readLine();
                while (line != null) {
                    sum = sum * Integer.valueOf(line);
                    line = br.readLine();
                }
                return sum;
            }
        };
        int result = this.fileReadTemplate(path, callback);
        return result;
    }

    public int calcMinus(String path) throws IOException {
        BufferedReaderCallBack callback = new BufferedReaderCallBack() {
            @Override
            public Integer doSomethingWithReader(BufferedReader br) throws IOException {
                Integer result = 0;
                String line = br.readLine();
                while (line != null) {
                    result = result - Integer.valueOf(line);
                    line = br.readLine();
                }
                return result;
            }
        };
        Integer result = this.fileReadTemplate(path, callback);
        return result;
    }


    public int fileReadTemplate(String path, BufferedReaderCallBack callback) throws IOException {
        BufferedReader br = null;
        try {
            FileReader fr = new FileReader(path);
            br = new BufferedReader(fr);
            Integer result = callback.doSomethingWithReader(br);
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

interface BufferedReaderCallBack {
    public abstract Integer doSomethingWithReader(BufferedReader br) throws IOException;
}



//try/catch/finally 는 클래스 전체에 걸쳐서 여러 메소드 안에 반복되는 코드였어.
//그래서 그냥 클래스 안에 공통 메소드로 뽑아 버렸지.
//그 함수 이름이 fileReadTemplate() 이고.
//대시 반복되는 try/catch/finally 틀 안에서도 메소드마다 바꿔서 써야 하는 내용이 있었어.
//그건 어떡하냐?
//각 메소드에서 템플릿을 부를 때 자기가 바꿔서 쓸 내용은 직접 만들어서 콜백으로 던져주기로 함.
//이때 콜백 함수를 전해주는 게 자바스크립트처럼 함수 자체를 던져줄 수는 없어.
//객체를 던져주고 그 안에 있는 함수를 실행시켜주는 식으로 해야 해.
//객체지향 언어니까.
//그렇다고 아무 거나 던져줄 수는 없겠지.
//그 안에 어떤 함수가 있는지 알고, 함수 이름에 오타가 있으면 어쩌려고.
//그래서 최소한 이 틀에 맞춰서 객체를 만들어서 던쳐줄 테니까 너도 이 틀에 맞춰서 받아서 실행하라는 약속이 될 만한 게 필요해.
//그게 바로 인터페이스.
//던져주는 애는 오브젝트를 만들 때 이 인터페이스를 implements 해서 구현하고, 받아서 쓰는 애는 이 인터페이스 안에 있는 함수를 꺼내서 쓰면 돼.
//그러려고 만든 인터페이스 이름이 BufferedReaderCallBack 이고 그 안에 함수 이름이 doSomethingWithReader() 임.
//이 인터페이스를 implements 한 오브젝트를 만들 때는 어떻게 하느냐?
//원래는 파일을 하나 따로 만들어서 이 인터페이스를 implements 한 class 를 새로 만들어야 하는 게 보통이지.
//근데 그렇게 하려면 지금 calcSum() 에서 클래스 하나, calcMult() 에서 클래스 하나, calcMinus() 에서 하나...
//기능을 하나 추가할 때마다 그에 맞는 콜백 오브젝트를 만들어줘야 하고, 그러려면 오브젝트를 만들 클래스 파일도 계속해서 하나씩 늘어나.
//나중에 애플리케이션이 커지고 기능이 많아지면 클래스도 엄청 많아지겠지.
//그래서 그렇게 따로 만들지는 않고 호출하는 메소드 안에서 잠깐 만들었다가 지워.
//만약에 여기 저기서 쓰는 기능이라면 클래스로 만들어서 다같이 쓰는 게 맞겠지.
//하지만 이거는 다른 데서 쓸 일도 없고 한 번 만들어서 한 번 쓰고 버릴 오브젝트야.
//그래서 그냥 메소드 안에서 클래스를 바로 정의해서 바로 쓰고 그 자리에서 바로 버림.
//그게 위에서 익명 내부 클래스 방식으로 선언한 클래스임.

// 근데 또 보면 메소드에서 콜백함수로 만든 doSomethingWithReader() 내용을 보면 다들 비슷해.
//Integer result = 0;
//String line = br.readLine();
//    while (line != null) {
//        result = result - Integer.valueOf(line);
//        line = br.readLine();
//    }
//return result;
//초기화하고, while 문으로 라인 읽고, 최종값 return 하고.
// 이런 반복되는 부분까지 template 에 포함시켜버릴 수는 없을까? 
// 더 간단해지도록
// 그렇게 만든 게 Calculator3534.class