import java.util.Random;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // Scanner scanner = new Scanner();
        Random random = new Random();

        boolean isHead = random.nextBoolean();
        
        if (isHead) {
            System.out.println("HEAD");
        } else {
            System.out.println("TAIL");
        }

        // scanner.close();
    }
}