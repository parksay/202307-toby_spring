package basics.tobyspring3.chapter35;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator353 {


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
