import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Insert extends Connect{
    Starter starter;

    public Insert (Starter starter){
        super(starter);
        this.starter = starter;
    }


    public void insert() {
        String tableName = getTableName();
        ArrayList<String> arrName = getInfo(tableName);
        ArrayList<String> arrVar = getVar(arrName);
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection con = DriverManager.getConnection(starter.getUrl());
            String preSt = null;
            if (tableName.equals("klienci")) {
                preSt = "insert into kancelaria.dbo.klienci" +
                        "(imie, nazwisko, pesel, adres, miasto, kodPocztowy, nrTelefonu)" +
                        "values(?, ?, ?, ?, ?, ?, ?)";
            }
            if (tableName.equals("sprawy")){
                preSt = "insert into kancelaria.dbo.sprawy" +
                        "(sygnatura, pelnomoctnictwo, idk, sad, wydzial, strona, cena, zaplacone, zakonczone)" +
                        "values(?,?,?,?,?,?,?,?,?)";
            }
            if (tableName.equals("rozprawy")){
                preSt = "insert into kancelaria.dbo.rozprawy" +
                        "(ids, dataRozprawy, nrSali, wynik)" +
                        "values(?,?,?,?)";
            }
            PreparedStatement statement = con.prepareStatement(preSt);
            for (int i = 1; i <= arrVar.size(); i++){
                if (arrVar.get(i-1).equals("null")){
                    statement.setNull(i, Types.NULL);
                    continue;
                }
                statement.setString(i, arrVar.get(i-1));
            }
            boolean isInsert = statement.execute();
            if (!isInsert) {
                System.out.println("Dodawanie udane");
            }else {
                System.out.println("Dodawanie nie powiodło się");
            }
            statement.close();
            con.close();

        } catch (Exception var4) {
            var4.printStackTrace();
        }
    }
}
