import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String moze = " 1) Wyświetl \n 2) Dodawanie \n 3) Zmień dane klienta \n 4) Raport o kliencie \n 5) Usuń klienta \n 0) Wyłącz aplikację \n";
        int nmove = 0;
        System.out.print("Witaj w DuraLEX. Możliwości: \n" + moze);
        System.out.println("Podaj kod URL połączenia siędo bazy:");
        Scanner scanner = new Scanner(System.in);
        String url = scanner.next();
        Starter starter = new Starter(url);

        nmove = Starter.getNumber();

        while (nmove != 0){
            switch (nmove) {
                case 1 -> {
                    Select select = new Select(starter);
                    System.out.println(select.getSelect().toString());
                }
                case 2 -> {
                    Insert insert = new Insert(starter);
                    insert.insert();
                }
                case 3 -> {
                    Change change = new Change(starter);
                    change.change();
                }
                case 4 -> {
                    Show show = new Show(starter);
                    show.shower();
                }
                case 5 -> {
                    Delate delate = new Delate(starter);
                    delate.delateClient();
                }
            }
            System.out.println(moze);
            nmove = Starter.getNumber();
        }
    }


}