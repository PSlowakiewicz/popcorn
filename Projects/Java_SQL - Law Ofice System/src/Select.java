import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Dictionary;
import java.util.HashMap;

public class Select extends Connect{
    private String url;

    public Select(Starter starter) {
        super(starter);
    }

    public StringBuilder getSelect(){
        String tableName = getTableName();
        HashMap<String, Integer> tNr = new HashMap<>();
        tNr.put("klienci", 8);
        tNr.put("sprawy", 10);
        tNr.put("rozprawy", 5);
        StringBuilder var10000 = new StringBuilder();
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection con = DriverManager.getConnection(starter.getUrl());
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("select * from " + tableName);
            while(rs.next()) {
                var10000.append("(");
                for (int i = 2; i <= tNr.get(tableName); i++) {
                    var10000.append(rs.getString(i)).append("  ");
                }
                var10000.append("),\n");
            }

            statement.close();
            rs.close();
            con.close();
        } catch (Exception var4) {
            var4.printStackTrace();
        }
        return var10000;
    }
}
