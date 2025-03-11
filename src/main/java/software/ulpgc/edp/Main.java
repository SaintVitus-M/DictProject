package software.ulpgc.edp;

public class Main {
    public static void main(String[] args) {
        Dictionary<String, Integer> d = new Dictionary<>();
        for (int i = 0; i < 500; i++) {
            d.put("P" + (i + 1), i);
        }
        int counter = 0;
        for (Dictionary.Entry<String, Integer> entry : d) {
            System.out.println(entry);
            counter++;
        }
        System.out.println(counter);
    }

}