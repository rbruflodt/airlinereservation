package MainPackage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.*;

/**
 * Created by Rachel on 2/11/2017.
 */

@WebServlet("/newaccountservlet")
public class NewAccountServlet extends HttpServlet{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        HttpSession session = request.getSession(true);
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://aa3zjrg5cjqq3u.c9taiotksa6k.us-east-1.rds.amazonaws.com:3306/ebdb", "team10", "team1010");
            String query = " insert into users (first_name, last_name, phone_number, email, password)"
                    + " values (?, ?, ?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1,request.getParameter("first name"));
            stmt.setString(2,request.getParameter("last name"));
            stmt.setLong(3,Long.valueOf(request.getParameter("phone number")));
            stmt.setString(4,request.getParameter("email"));
            stmt.setString(5,request.getParameter("password"));
            stmt.execute();
            response.sendRedirect("/newaccount.jsp");
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
}
