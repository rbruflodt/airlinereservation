package MainPackage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;


/**
 * Created by Rachel on 2/11/2017.
 */
@WebServlet("/signin")
public class SignInServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        HttpSession session = request.getSession(true);
        if (request.getParameter("newaccount") != null) {
            session.setAttribute("enteredinfo",new User("","",null,request.getParameter("email"),"",false,false));
            response.sendRedirect("newaccount.jsp");
        }
        else if(request.getParameter("signout")!=null){
            session.removeAttribute("currentuser");
            session.removeAttribute("currenttab");
           response.sendRedirect("/index.jsp");
        }
        else if(request.getParameter("newpassword")!=null){
            response.sendRedirect("/newpassword.jsp");
        }
        else if(request.getParameter("verifyaccount")!=null){
            response.sendRedirect("/verifyaccount.jsp");
        }
        else{
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://aa3zjrg5cjqq3u.c9taiotksa6k.us-east-1.rds.amazonaws.com:3306/ebdb", "team10", "team1010");
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                messageDigest.update(request.getParameter("password").getBytes());
                Statement stmt = con.createStatement();
                String search = "select * from users where email='"
                        + request.getParameter("email")
                        + "' AND HEX(password)='"
                        + DatatypeConverter.printHexBinary(messageDigest.digest())
                        + "'";
                ResultSet rs = stmt.executeQuery(search);
                if (!rs.next()) {
                    session.setAttribute("loginmessage", "Invalid email or password.");
                    response.sendRedirect("/index.jsp");
                } else if(rs.getInt("verification")!=-1){
                    session.setAttribute("code",String.valueOf(rs.getInt("verification")));
                    session.setAttribute("enteredinfo",new User(rs.getString("first_name"), rs.getString("last_name"), rs.getLong("phone_number"), rs.getString("email"), rs.getString("password"),rs.getBoolean("is_manager"),rs.getBoolean("is_admin")));
                    session.setAttribute("verificationoption","verify");
                    response.sendRedirect("/index.jsp");
                }
                    else{
                    session.setAttribute("currentuser", new User(rs.getString("first_name"), rs.getString("last_name"), rs.getLong("phone_number"), rs.getString("email"), rs.getString("password"),rs.getBoolean("is_manager"),rs.getBoolean("is_admin")));
                    session.removeAttribute("loginmessage");
                    response.sendRedirect("/index.jsp");
                }
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch(NoSuchAlgorithmException e){
                e.printStackTrace();
            }
        }
    }
}
