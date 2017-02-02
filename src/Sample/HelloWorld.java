package Sample;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;

/**
 * Created by Rachel on 2/1/2017.
 */

@WebServlet("/HelloWorld")
public class HelloWorld extends HttpServlet{
    public static String passengers = "";
    Connection conn;
    public static String getMessage() {
        return "Hello, world";
    }

    public static String getPassengers(){
        return passengers;
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        if (request.getParameter("show") != null) {
            try {
                    if(conn == null) {
                        if (System.getProperty("RDS_HOSTNAME") != null) {
                            Class.forName("com.mysql.jdbc.Driver");
                            String dbName = System.getProperty("RDS_DB_NAME");
                            String userName = System.getProperty("RDS_USERNAME");
                            String password = System.getProperty("RDS_PASSWORD");
                            String hostname = System.getProperty("RDS_HOSTNAME");
                            String port = System.getProperty("RDS_PORT");
                            String jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password=" + password;
                            conn = DriverManager.getConnection(jdbcUrl);
                        }
                        else {
                            Class.forName("com.mysql.jdbc.Driver").newInstance();
                            conn = DriverManager.getConnection("jdbc:mysql://aa3zjrg5cjqq3u.c9taiotksa6k.us-east-1.rds.amazonaws.com:3306/ebdb", "team10", "team1010");
                        }
                    }
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM passengers");
                String s = "<html><ul style=\"list-style-type:circle\">";
                while (rs.next()) {
                    s+="<li>";
                    s += rs.getString("passenger_id");
                    s += " ";
                    s += rs.getString("name");
                    s += "</li>";
                }
                s+="</ul><html>";
                passengers = s;
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        } else {
            passengers = "";
        }
        try {
            response.sendRedirect("/index.jsp");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
