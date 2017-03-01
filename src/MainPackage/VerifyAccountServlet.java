package MainPackage;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

/**
 * Created by Rachel on 2/11/2017.
 */
@WebServlet("/verifyaccountservlet")
public class VerifyAccountServlet extends HttpServlet{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        HttpSession session = request.getSession(true);
        try {
            if(request.getParameter("submit")!=null) {
                if (request.getParameter("codeinput").equals(session.getAttribute("code"))) {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con = DriverManager.getConnection("jdbc:mysql://aa3zjrg5cjqq3u.c9taiotksa6k.us-east-1.rds.amazonaws.com:3306/ebdb", "team10", "team1010");
                    PreparedStatement stmt = con.prepareStatement("update users set verification = ? where email = ?");
                    stmt.setInt(1,-1);
                    stmt.setString(2,((User) session.getAttribute("enteredinfo")).getEmail());
                    stmt.executeUpdate();
                    session.setAttribute("currentuser", session.getAttribute("enteredinfo"));
                    session.removeAttribute("enteredinfo");
                    con.close();
                    response.sendRedirect("/index.jsp");
                } else {
                    session.setAttribute("codeerrormessage", "Incorrect code.");
                    response.sendRedirect("/verifyaccount.jsp");
                }
            }
            else{
                String message = "Please enter this code on the verification page: ";
                Message msg = NewAccountServlet.getEmailMessage(((User) session.getAttribute("enteredinfo")).getEmail());
                Random r = new Random();
                String code = String.valueOf(r.nextInt(89999)+10000);
                msg.setText(message+code);
                session.setAttribute("code",code);
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://aa3zjrg5cjqq3u.c9taiotksa6k.us-east-1.rds.amazonaws.com:3306/ebdb", "team10", "team1010");
                PreparedStatement stmt = con.prepareStatement("update users set verification = ? where email = ?");
                stmt.setInt(1,Integer.valueOf(code));
                stmt.setString(2,((User) session.getAttribute("enteredinfo")).getEmail());
                stmt.executeUpdate();
                // sends the e-mail
                Transport.send(msg);
                response.sendRedirect("/verifyaccount.jsp");
            }
        }catch(MessagingException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void createNewAccount(HttpSession session, boolean is_manager,int code){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://aa3zjrg5cjqq3u.c9taiotksa6k.us-east-1.rds.amazonaws.com:3306/ebdb", "team10", "team1010");
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            String query = " insert into users (first_name, last_name, phone_number, email, password, is_admin, is_manager, verification)"
                    + " values (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(query);
            User enteredInfo = (User) session.getAttribute("enteredinfo");
            messageDigest.update(enteredInfo.getPassword().getBytes());
            stmt.setString(1, enteredInfo.getFirstName());
            stmt.setString(2, enteredInfo.getLastName());
            stmt.setLong(3, enteredInfo.getPhoneNumber());
            stmt.setString(4, enteredInfo.getEmail());
            stmt.setBytes(5, messageDigest.digest());
            stmt.setBoolean(6, false);
            stmt.setBoolean(7, is_manager);
            stmt.setInt(8,code);
            stmt.execute();
            session.removeAttribute("code");
            session.removeAttribute("verifymessage");
            con.close();
        }catch(SQLException e){
            e.printStackTrace();
        }        catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
