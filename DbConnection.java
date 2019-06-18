import java.sql.*;

public class DbConnection {
    private String userEmail;
    private String userPassword;
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet resultSet = null;

    public DbConnection() throws SQLException {
        this.conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/chatapp?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "");
        //TO FIX THE DATABASE AND SEVER TIME ISSUE.

    }

    public boolean pushRecords(String username, String email, String gender, String password) {
        try {

            String query = "INSERT INTO users (username, email, gender, password)" +
                    "VALUES ('" + username + "','" + email + "','" + gender + "','" + password + "')";
            stmt = this.conn.createStatement();
            int a = stmt.executeUpdate(query);
            if (a > 0) {
                System.out.println("Stored data successfully");
                return true;
            } else {
                System.out.println("Error in storing data");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();

            System.out.println(e.getMessage());
            return false;
        }
    }
    public boolean checkEmailDetail(String regEmail)
    {
        try{
            String query = "Select * from users where users.email='"+regEmail+"'";
            stmt = conn.createStatement();
            resultSet = stmt.executeQuery(query);
            if(resultSet.next())
            {
                return true;
            }
            else{
                return false;
            }
        }catch(SQLException e)
        {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean checkLoginDetails(String userEmail, String userPassword) {
        try {
            String query = "Select * from users where users.email='"+userEmail+ "' and users.password='"+userPassword+"'";
//            String query = "Select * from users";
            stmt = conn.createStatement();
            resultSet = stmt.executeQuery(query);
            if(resultSet.next())
            {
                System.out.println("Login Record Found");
                return true;
            }else{
                System.out.println("Login Record Not Found");
                return false;
            }
//            while(resultSet.next())
//            {
//                if(resultSet.getString("email").equals(userEmail) && resultSet.getString("password").equals(userPassword))
//                {
//                    System.out.println("found");
//                    return true;
//                }else {
//                    System.out.println("not found");
//                    return  false;
//                }
//                }
        } catch (SQLException e){
                System.out.println(e.getMessage());
                return false;
            }
    }
}


