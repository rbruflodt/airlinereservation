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
                    MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                    String query = " insert into users (first_name, last_name, phone_number, email, password, is_admin, is_manager)"
                            + " values (?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement stmt = con.prepareStatement(query);
                    User enteredInfo = (User) session.getAttribute("enteredinfo");
                    messageDigest.update(enteredInfo.getPassword().getBytes());
                    stmt.setString(1, enteredInfo.getFirstName());
                    stmt.setString(2, enteredInfo.getLastName());
                    stmt.setLong(3, enteredInfo.getPhoneNumber());
                    stmt.setString(4, enteredInfo.getEmail());
                    stmt.setBytes(5, messageDigest.digest());
                    stmt.setBoolean(6, false);
                    stmt.setBoolean(7, false);
                    stmt.execute();
                    session.removeAttribute("enteredinfo");
                    session.removeAttribute("code");
                    session.removeAttribute("verifymessage");
                    con.close();
                    session.setAttribute("currentuser",enteredInfo);
                    response.sendRedirect("/index.jsp");
                } else {
                    session.setAttribute("codeerrormessage", "Incorrect code.");
                    response.sendRedirect("/verifyaccount.jsp");
                }
            }
            else{
                String message = "Please enter this code on the verification page: ";
                Message msg = (Message) session.getAttribute("verifymessage");
                Random r = new Random();
                String code = String.valueOf(r.nextInt(99999));
                msg.setText(message+code);
                session.setAttribute("code",code);

                // sends the e-mail
                Transport.send(msg);
                response.sendRedirect("/verifyaccount.jsp");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }        catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }catch(MessagingException e){
            e.printStackTrace();
        }
    }
}
