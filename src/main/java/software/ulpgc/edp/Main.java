package software.ulpgc.edp;

import java.util.Random;

public class Main {
    public static Random rand = new Random();

    public static void main(String[] args) {
        Dict<String, Integer> dict = new Dictionary<>();
        for (int i = 0; i < 100 ; i++) {
            dict.add("P" + i, 100 + 1);
        }
        int nullCount = 0;
        for (Dict.Entry<String, Integer> key : dict.items()) {
            System.out.println(key);
            if(key == null) nullCount++;
        }
        System.out.println(nullCount);
    }
}