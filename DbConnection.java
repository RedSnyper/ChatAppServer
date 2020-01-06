import java.sql.*;
import java.util.HashMap;

public class DbConnection {
    private String userEmail;
    private String userPassword;
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet resultSet = null;
    private HashMap<String,String> recordMap = new HashMap<>();

    public DbConnection() throws SQLException {
        this.conn = DriverManager.getConnection(
             "jdbc:mysql://localhost:3306/chatapp?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", ""); //setting up driver
        //"jdbc:mysql://localhost:3306/chatapp","root","");
        //TO FIX THE DATABASE AND SEVER TIME ISSUE.
        if(conn==null)
        {
            System.out.println("not connected");
        }

    }

    public boolean pushRecords(String username, String email, String gender, String password) // called to register data
    {
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
    public boolean checkEmailDetail(String regEmail) // called to see if the same email exists or not
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

    public boolean checkLoginDetails(String userEmail, String userPassword) // called to see if login detail exists or not
    {
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
        } catch (SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
    }
    public HashMap getUserName(String email) throws SQLException
    {

        String query = "Select username from users where users.email='"+ email +"'";
        stmt = conn.createStatement();
        resultSet = stmt.executeQuery(query);
        if(resultSet.next())
        {
            recordMap.put(resultSet.getString("email"),resultSet.getString("username"));
            return recordMap;
        }
        return null;
    }

    public String getUserEmail(String name) // to get the useremail of the receiver.
                                            // Did this to make it easy to program the serverConnect in ChatApp project for switching messages with unique email for all
    {
        try {
            String query = "Select email from users where users.username='"+ name +"'";
//            String query = "Select * from users";
            stmt = conn.createStatement();
            resultSet = stmt.executeQuery(query);
            if(resultSet.next())
            {

                return resultSet.getString("email");
            }else{
                System.out.println("Login Record Not Found");
                return null;
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());

        }
        return null;
    }
    public void changeOnlineStatus(String userEmail,int onlineStatus) throws SQLException // change the online status. Called when user logs into and when user quits the app in ChatApp project
    {
        String query =" Update users set login_status='"+onlineStatus+"' where users.email='"+ userEmail +"'";
        stmt = conn.createStatement();
        stmt.executeUpdate(query);
        if(stmt!=null) {
           // System.out.println("vayo");
        }else
        {
            System.out.println("Error in changeOnlineStatus");
        }

    }
    public HashMap getOnlineUsers() throws SQLException // gets all online user info and sends to server. This is called right after login is done in ChatApp project.
    {
        String query ="Select * from users where login_status=1";
        stmt = conn.createStatement();
        resultSet = stmt.executeQuery(query);
        while(resultSet.next())
        {
            recordMap.put(resultSet.getString("email"),resultSet.getString("username"));
        }
        return recordMap;
    }
}
