package MainPackage;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Properties;
import java.util.Date;
import java.util.Random;
import javax.mail.MessagingException;

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
            String errorMessage="";
            String firstName = request.getParameter("first name");
            String lastName = request.getParameter("last name");
            String phoneString = request.getParameter("phone number");
            Long phoneNumber;
            if(phoneString.equals("")){
                phoneNumber = null;
            }
            else{
                phoneNumber = Long.valueOf(phoneString);
            }
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String confirmpassword = request.getParameter("confirmpassword");
            if(firstName.equals("")||lastName.equals("")||phoneString.equals("")||email.equals("")||password.equals("")){
                errorMessage+="All fields required.<html><br></html>";
            }
            else {
                if (!(Long.compare(phoneNumber, Long.valueOf("9999999999")) > 0 || Long.compare(phoneNumber, Long.valueOf(1000000000)) >= 0)) {
                    errorMessage += "Invalid phone number.<html><br></html>";
                }
                if (!email.contains("@") || !email.contains(".")) {
                    errorMessage += "Invalid email address.<html><br></html>";
                }
                else{
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery("select * from users where email='"
                            + request.getParameter("email")
                            +"'");
                    if(rs.next()){
                        errorMessage += "An account already exists with that email address.<html><br></html>";
                    }
                }
                if (password.toLowerCase().equals(password) || password.length() < 8) {
                    errorMessage += "Invalid password.<html><br></html>";
                }
                if (!confirmpassword.equals(password)) {
                    errorMessage += "Password fields do not match.<html><br></html>";
                }
            }
            User info = (User) session.getAttribute("enteredinfo");
            info.setEmail(email);
            info.setFirstName(firstName);
            info.setLastName(lastName);
            info.setPhoneNumber(phoneNumber);
            if(!errorMessage.equals("")){
                session.setAttribute("errormessage",errorMessage);
                response.sendRedirect("/newaccount.jsp");
            }
            else {
                info.setPassword(password);
                Message msg = getEmailMessage(email);
                if(session.getAttribute("newmanager")==null) {
                    String message = "Please enter this code on the verification page: ";
                    Random r = new Random();
                    String code = String.valueOf(r.nextInt(99999));
                    session.setAttribute("code",code);
                    msg.setText(message+code);
                    // sends the e-mail
                    Transport.send(msg);
                    VerifyAccountServlet.createNewAccount(session,false,Integer.valueOf(code));
                    response.sendRedirect("/verifyaccount.jsp");
                }
                else{
                    VerifyAccountServlet.createNewAccount(request.getSession(),true,-1);
                    String message = "An Iowa Air manager's account has been created for you.\nYour account password is: "+password+"\nVisit the website to login and change your password.";
                    msg.setText(message);
                    // sends the e-mail
                    Transport.send(msg);
                    session.removeAttribute("newmanager");
                    session.removeAttribute("enteredinfo");
                    session.setAttribute("newpasswordmessage","Manager account created successfully.");
                    response.sendRedirect("/index.jsp");
                }



            }
            con.close();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        catch(javax.mail.MessagingException e){
            e.printStackTrace();
        }
    }

    public static Message getEmailMessage(String email){
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("IowaAirTeam10@gmail.com", "team1010");
            }
        };

        Session s = Session.getInstance(properties, auth);

        // creates a new e-mail message
        Message msg = new MimeMessage(s);

        try {
            msg.setFrom(new InternetAddress("IowaAirTeam10@gmail.com"));
            InternetAddress[] toAddresses = {new InternetAddress(email)};
            msg.setRecipients(Message.RecipientType.TO, toAddresses);
            msg.setSubject("Iowa Air Account Verification");
            msg.setSentDate(new Date());
        }catch(MessagingException e){
            e.printStackTrace();
        }
        return msg;
    }
}
