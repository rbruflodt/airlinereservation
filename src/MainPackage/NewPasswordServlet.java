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
 * Created by Rachel on 2/13/2017.
 */
@WebServlet("/newpasswordservlet")
public class NewPasswordServlet extends HttpServlet{
        public void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, java.io.IOException {
            HttpSession session = request.getSession(true);
            try {
                String errorMessage="";
                String password = request.getParameter("password");
                String confirmpassword = request.getParameter("confirmpassword");
                String oldpassword = request.getParameter("oldpassword");
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://aa3zjrg5cjqq3u.c9taiotksa6k.us-east-1.rds.amazonaws.com:3306/ebdb", "team10", "team1010");
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                messageDigest.update(oldpassword.getBytes());
                Statement statement = con.createStatement();
                String search = "select * from users where email='"
                        + ((User) session.getAttribute("currentuser")).getEmail()
                        + "' AND HEX(password)='"
                        + DatatypeConverter.printHexBinary(messageDigest.digest())
                        + "'";
                ResultSet rs = statement.executeQuery(search);
                if(!rs.next()){
                    errorMessage += "Incorrect old password.<html><br></html>";
                }
                if (password.toLowerCase().equals(password) || password.length() < 8) {
                    errorMessage += "Invalid password.<html><br></html>";
                }
                if (!confirmpassword.equals(password)) {
                    errorMessage += "Password fields do not match.<html><br></html>";
                }
                if(!errorMessage.equals("")){
                    session.setAttribute("errormessage",errorMessage);
                    con.close();
                    response.sendRedirect("/newpassword.jsp");
                }
                else{
                    messageDigest.update(password.getBytes());
                    PreparedStatement stmt = con.prepareStatement("update users set password = ? where email = ?");
                    stmt.setBytes(1,messageDigest.digest());
                    stmt.setString(2,((User) session.getAttribute("currentuser")).getEmail());
                    stmt.executeUpdate();
                    session.setAttribute("newpasswordmessage","Password changed.");
                    con.close();
                    response.sendRedirect("/index.jsp");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }catch(ClassNotFoundException e){
                e.printStackTrace();
            }catch(NoSuchAlgorithmException e){
                e.printStackTrace();
            }
        }
    }

