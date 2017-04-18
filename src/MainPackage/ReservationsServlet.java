package MainPackage;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.MalformedURLException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Created by Rachel on 4/8/2017.
 */
@WebServlet("/reservationsservlet")
public class ReservationsServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        HttpSession session = request.getSession();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://aa3zjrg5cjqq3u.c9taiotksa6k.us-east-1.rds.amazonaws.com:3306/ebdb", "team10", "team1010");
            Statement stmt = con.createStatement();
            String search = "select * from tickets where email='" + ((User) session.getAttribute("currentuser")).getEmail() + "'";
            ResultSet rs = stmt.executeQuery(search);
            while(rs.next()) {
                /*if (request.getParameter("emailreceipt"+rs.getInt("ticket_number"))!=null){
                    Message message = NewAccountServlet.getEmailMessage(((User) session.getAttribute("currentuser")).getEmail());
                    message.setSubject("Iowa Air Flight Confirmation");
                    BodyPart messageBodyPart = new MimeBodyPart();
                    messageBodyPart.setText("This is to confirm your flight reservation on Iowa Air.");
                    Multipart multipart = new MimeMultipart();
                    multipart.addBodyPart(messageBodyPart);
                    messageBodyPart=new MimeBodyPart();
                    ServletContext sc = session.getServletContext();

                    DataSource source = new FileDataSource(sc.getResource("/WEB-INF/receipts/").getPath()+"receipt"+rs.getString("ticket_number")+".pdf");
                    messageBodyPart.setDataHandler(new DataHandler(source));
                    messageBodyPart.setFileName("receipt.pdf");
                    multipart.addBodyPart(messageBodyPart);
                    message.setContent(multipart);
                    Transport.send(message);
                    session.setAttribute("reservationmessage","The receipt was sent to your email address.");
                }else */if(request.getParameter("cancelticket"+rs.getString("ticket_number"))!=null){
                    search = "delete from tickets where (ticket_number='"+rs.getString("ticket_number")+"' and depart_date='"+rs.getString("depart_date")+"')";
                    stmt.execute(search);
                }
            }
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }catch(SQLException e){
            e.printStackTrace();
        }/*catch(MessagingException e){
            e.printStackTrace();
        }*/
        response.sendRedirect("/index.jsp");
    }

    public static ArrayList<ArrayList<String>> getTickets(HttpSession session){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://aa3zjrg5cjqq3u.c9taiotksa6k.us-east-1.rds.amazonaws.com:3306/ebdb", "team10", "team1010");
            Statement stmt = con.createStatement();
            String search = "select * from tickets where email='"+((User)session.getAttribute("currentuser")).getEmail()+"'";
            ResultSet rs = stmt.executeQuery(search);
            ArrayList<ArrayList<String>> tickets=new ArrayList<>();
            while (rs.next()){
                ArrayList<String> temptickets = new ArrayList<>();
                search="select * from flights where flight_id='"+rs.getString("flight_id")+"'";
                Statement stmt2 = con.createStatement();
                ResultSet rs2 = stmt2.executeQuery(search);
                rs2.next();
                temptickets.add(String.valueOf(rs.getInt("ticket_number")));
                temptickets.add(rs.getString("first_name")+" "+rs.getString("last_name"));
                temptickets.add(rs2.getString("depart_city"));
                temptickets.add(rs.getString("depart_date"));
                temptickets.add(String.format("%02d",rs2.getInt("depart_hours"))+":"+String.format("%02d",rs2.getInt("depart_minutes"))+" "+rs2.getString("depart_AMPM")+" ("+rs2.getString("depart_timezone")+")");
                temptickets.add(rs2.getString("arrive_city"));
                temptickets.add(rs.getString("arrive_date"));
                temptickets.add(String.format("%02d",rs2.getInt("arrive_hours"))+":"+String.format("%02d",rs2.getInt("arrive_minutes"))+" "+rs2.getString("arrive_AMPM")+" ("+rs2.getString("arrive_timezone")+")");
                tickets.add(temptickets);
            }
            tickets.sort(new TicketDepartTimeComparator());
            con.close();
            return tickets;
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public static String getReceiptURL(HttpSession session){
        ServletContext sc = session.getServletContext();
        try {
            return sc.getResource("/WEB-INF/receipts/").getPath();
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        return null;
    }

    public static boolean within24Hours(String date){
        LocalDate now = LocalDate.now();
        if(now.isEqual(LocalDate.parse(date))){
            return true;
        }
        now = now.plusDays(1);
        if(now.isEqual(LocalDate.parse(date))){
            return true;
        }
        return false;
    }

    public static boolean isPassed(String date){
        LocalDate now = LocalDate.now();
        if(now.isAfter(LocalDate.parse(date))){
            return true;
        }
        return false;
    }
}