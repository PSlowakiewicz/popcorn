import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Scanner;

public class Delate extends Connect{

    public Delate(Starter starter) {
        super(starter);
    }

    public void delateClient(){
        String name;
        String surname;

        System.out.print("Podaj imię: ");
        Scanner scanner = new Scanner(System.in);
        name = scanner.next();
        System.out.print("Podaj nazwisko: ");
        surname = scanner.next();

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection con = DriverManager.getConnection(starter.getUrl());
            PreparedStatement statement_r = con.prepareStatement(
                    """
                            begin transaction
                            delete rozprawy
                            where idr in(
                            select r.idr from klienci k
                            join sprawy s on k.idk = s.idk
                            join rozprawy r on s.ids = r.ids
                            where k.imie = ? and k.nazwisko = ?);delete sprawy
                            where ids in(
                            select s.ids from klienci k
                            join sprawy s on k.idk = s.idk
                            where k.imie = ? and k.nazwisko = ?);delete klienci
                            where idk in(
                            select k.idk from klienci k
                            where k.imie = ? and k.nazwisko = ?);commit transaction""");
            statement_r.setString(1, name);
            statement_r.setString(3, name);
            statement_r.setString(5, name);
            statement_r.setString(2, surname);
            statement_r.setString(4, surname);
            statement_r.setString(6, surname);
            statement_r.execute();
            statement_r.close();
            con.close();


        }catch (Exception var4) {
            var4.printStackTrace();
        }
    };

}
