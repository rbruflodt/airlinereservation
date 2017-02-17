package MainPackage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Rachel on 2/16/2017.
 */
@WebServlet("/manageraccountsservlet")
public class ManagerAccountsServlet  extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        HttpSession session = request.getSession(true);
            if(request.getParameter("newmanager")!=null){
                session.setAttribute("enteredinfo",new User());
                session.setAttribute("newmanager",true);
                response.sendRedirect("/newaccount.jsp");
            }

    }

    public static User[] getManagers(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://aa3zjrg5cjqq3u.c9taiotksa6k.us-east-1.rds.amazonaws.com:3306/ebdb", "team10", "team1010");
            Statement stmt = con.createStatement();
            String search = "select * from users where is_manager='1'";
            ResultSet rs = stmt.executeQuery(search);
            if(!rs.next()){
                return new User[0];
            }
            else{
                ArrayList<User> managers = new ArrayList<>();
                do{
                    managers.add(new User(rs.getString("first_name"), rs.getString("last_name"), rs.getLong("phone_number"), rs.getString("email"), rs.getString("password"),rs.getBoolean("is_manager"),rs.getBoolean("is_admin")));
                }while(rs.next());
                return managers.toArray(new User[managers.size()]);
            }
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return new User[0];
    }
}
