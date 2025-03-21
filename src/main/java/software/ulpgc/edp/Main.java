package software.ulpgc.edp;

import java.util.Random;

public class Main {
    public static Random rand = new Random();

    public static void main(String[] args) {
        Dict<String, Integer> dict = new Dictionary<>();
        for (int i = 1; i <= 200 ; i++) {
            dict.add("P" + i, rand.nextInt(100));
        }
        int count = 0;
        for (Dict.Entry<String, Integer> key : dict.items()) {
            System.out.println(key);
            count++;
        }
        System.out.println(count);
    }
}