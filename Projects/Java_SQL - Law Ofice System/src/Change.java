import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Scanner;

public class Change extends Connect{
    ArrayList<String> arrChanges = new ArrayList<>();
    public Change(Starter starter) {
        super(starter);
    }

    public void change() {
        String tableName = "klienci";
        ArrayList<String> arrName = getInfo(tableName);
        ArrayList<String> arrChange = new ArrayList<>();
        arrName.remove(0);
        ArrayList<String> arrRow = getRow();
        String nr = arrRow.get(0);
        arrRow.remove(0);
        Scanner scanner = new Scanner(System.in);
        for (int i = 1; arrName.size() >= i; i++){
            System.out.print("\n" + arrName.get(i-1).toUpperCase() + " obecnie: " + arrRow.get(i-1) + "   Podaj zmianę: ");
            arrChange.add(scanner.nextLine());
        }
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection con = DriverManager.getConnection(starter.getUrl());
            PreparedStatement statement = con.prepareStatement("Update klienci set imie=?,nazwisko=?,pesel=?,adres=?,miasto=?,kodPocztowy=?,nrTelefonu=? where idk = ?");
            //statement.setObject(1, tableName);
            for (int i = 1; arrName.size() >= i; i++){
                String vchan;
                if (arrChange.get(i-1).equals("")){
                    vchan = "null";
                }else {vchan = arrChange.get(i-1); }
                System.out.println(vchan);
                if (vchan.equals("null")){
                    statement.setNull(i, Types.NULL);
                    continue;
                }

                statement.setString(i, vchan);
            }
            statement.setString(8, nr);
            statement.execute();
        }
         catch (Exception e) {
            e.printStackTrace();
        }
    }
}
