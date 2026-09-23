import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        WashService[] washes = readWashes("washes.txt");

        for (WashService wash : washes) {
            System.out.println(wash.summary());
        }
    }

    private static WashService[] readWashes(String fileName) {
        try (Scanner scanner = new Scanner(new File(fileName))) {
            int total = scanner.nextInt();
            WashService[] washes = new WashService[total];

            for (int i = 0; i < total; i++) {
                String type = scanner.next();
                String id = scanner.next();
                int days = scanner.nextInt();
                int units = scanner.nextInt();

                if (type.equals("MOTORCYCLE")) {
                    washes[i] = new MotorcycleWash(id, days, units);
                } else if (type.equals("CAR")) {
                    washes[i] = new CarWash(id, days, units);
                } else {
                    throw new IllegalArgumentException("Unknown wash type: " + type);
                }
            }

            return washes;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not find " + fileName, e);
        }
    }
}
