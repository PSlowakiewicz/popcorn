import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class Connect {
    Starter starter;

    public Connect (Starter starter){
        this.starter = starter;
    }

    public String getTableName (){
        int nr;
        System.out.print("Wybierz tabelę: \n 1) Klienta \n 2) Sprawy \n 3) Rozprawy \n");
        nr = Starter.getNumber();
        while (nr > 3){
            System.out.println("Proszę podać poprawny numer: ");
            nr = Starter.getNumber();
        }
        if (nr == 1){return  "klienci";}
        if (nr == 2){return  "sprawy";}
        if (nr == 3){return  "rozprawy";}
        return null;
    };

    public ArrayList<String> getInfo(String tableName){
        ArrayList<String> arrName = new ArrayList<>();
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection con = DriverManager.getConnection(starter.getUrl());
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('dbo." + tableName + "') ");
            while(rs.next()) {
                arrName.add(rs.getString("name"));
            }
            statement.close();
            rs.close();
            con.close();
        }  catch (Exception var4) {
            var4.printStackTrace();
        }
        return arrName;
    }

    public ArrayList<String> getRow(){
        ArrayList<String> arrRow = new ArrayList<>();
        String name;
        String surname;
        //StringBuilder finPrint = new StringBuilder();
        System.out.print("Podaj imię: ");
        Scanner scanner = new Scanner(System.in);
        name = scanner.next();
        System.out.print("Podaj nazwisko: ");
        surname = scanner.next();

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection con = DriverManager.getConnection(starter.getUrl());
            PreparedStatement statement1 = con.prepareStatement("Select * from klienci where imie = ? and nazwisko = ?");
            statement1.setString(1, name);
            statement1.setString(2, surname);
            ResultSet rs = statement1.executeQuery();
            rs.next();
            for (int i = 1; i <= 8; i++) {
                arrRow.add(rs.getString(i));
            }
            statement1.close();
            rs.close();
            con.close();
        }  catch (Exception var4) {
            var4.printStackTrace();
        }
        return arrRow;
    }

    public ArrayList<String> getVar(ArrayList<String> arrName){
        ArrayList<String> arrVar = new ArrayList<>();
        arrName.remove(0);
        Scanner scanner = new Scanner(System.in);
        for (String name : arrName){
            System.out.print("Podaj " + name.toUpperCase() + ": ");
            String word = scanner.nextLine();
            if (word.equals(""))
                arrVar.add("null");
            else{
                arrVar.add(word);
            }
        }
        return arrVar;
    }
}
