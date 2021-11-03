import java.util.Scanner;

public class Starter {
    private final String url;

    public Starter(String url)
    {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Integer.parseInt(strNum);

        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static int getNumber() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Podaj numer: ");
        String move = scanner.next();
        while (!isNumeric(move.toString())){
            System.out.println("Proszę wybrać jedną z powyższych opcji : ");
            move = scanner.nextLine();
        }
        return Integer.parseInt(move.toString());
    }
}
