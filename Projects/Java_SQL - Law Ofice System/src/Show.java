import java.sql.*;
import java.util.Scanner;

public class Show extends Connect{
    public Show(Starter starter) {
        super(starter);
    }


    public void shower(){
        String name;
        String surname;
        StringBuilder finPrint = new StringBuilder();
        System.out.print("Podaj imię: ");
        Scanner scanner = new Scanner(System.in);
        name = scanner.next();
        System.out.print("Podaj nazwisko: ");
        surname = scanner.next();
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection con = DriverManager.getConnection(starter.getUrl());
            PreparedStatement statement = con.prepareStatement("Select * from klienci where imie = ? and nazwisko = ?");
            PreparedStatement statement2 = con.prepareStatement("Select * from sprawy where idk = ?");
            PreparedStatement statement3 = con.prepareStatement("Select * from rozprawy where ids = ?");

            statement.setString(1, name);
            statement.setString(2, surname);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                finPrint.append(rs.getString("idk")).append(": ").append(rs.getString("imie")).append(" ").append(rs.getString("nazwisko")).append("\n");
                statement2.setString(1, rs.getString("idk"));
                ResultSet rs2 = statement2.executeQuery();
                while (rs2.next()){
                    finPrint.append("   ").append(rs2.getString("sygnatura")).append(" ");
                    if (!rs2.getBoolean("pelnomoctnictwo")){
                        finPrint.append("Brak pełnomocnictwa  ");
                    }
                    long diff = rs2.getLong("cena") - rs2.getLong("zaplacone");
                    if (diff > 0){
                        finPrint.append("Do zapłacenia: ").append(diff);
                    }else {finPrint.append("Zapłacone");}
                    finPrint.append("\n");

                    statement3.setString(1, rs2.getString("ids"));
                    ResultSet rs3 = statement3.executeQuery();
                    while (rs3.next()){
                        finPrint.append("        ").append(rs2.getString("sad")).append("  ").append(rs3.getString("dataRozprawy")).append("  ").append(rs3.getString("wynik")).append("\n");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(finPrint);
    }
}
