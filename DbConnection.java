import java.sql.*;

public class DbConnection {
    private Connection conn=null;
    private Statement stmt = null;
    private ResultSet resultSet = null;

    public DbConnection() throws SQLException
    {
        this.conn= DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/chatapp?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root","");
                                                         //TO FIX THE DATABASE AND SEVER TIME ISSUE.

    }
    public boolean pushRecords(String username, String email, String gender, char[] password) {
        try {

            String query = "INSERT INTO users (username, email, gender, password)" +
            "VALUES ('" + username + "','" + email + "','" + gender + "','" + password +"')";
            stmt = this.conn.createStatement();
            int a  = stmt.executeUpdate(query);
            if(a>0)
            {
                System.out.println("Stored data successfully");
                return true;
            }
            else{
                System.out.println("error in storing data");
                return false;
            }
        }catch(SQLException e)
        {
            e.printStackTrace();

            System.out.println(e.getMessage());
            return false;
        }
    }

}
