package Sample;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Rachel on 2/1/2017.
 */

@WebServlet("/HelloWorld")
public class HelloWorld extends HttpServlet{
    public static String passengers = "";
    public static String getMessage() {
        return "Hello, world";
    }

    public static String getPassengers(){
        return passengers;
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        if (request.getParameter("show") != null) {
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/airlinereservations", "root", "tomsham1995");
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
