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
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;


/**
 * Created by Rachel on 4/1/2017.
 */
@WebServlet("/bookflightsservlet")
public class BookFlightsServlet extends HttpServlet{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        HttpSession session = request.getSession();
        if(request.getParameter("creditcardtype")==null||request.getParameter("creditcardnumber")==null||
                request.getParameter("creditcardexpiration")==null||request.getParameter("creditcardsecurity")==null){
            session.setAttribute("bookflighterror","All fields required.");
            response.sendRedirect("/bookflights.jsp");
        }
        else if(request.getParameter("creditcardnumber").length()!=10||!Character.isAlphabetic(request.getParameter("creditcardnumber").charAt(0))){
            session.setAttribute("bookflighterror","Credit card number must be 10 digits with an alphabetic character as the first digit.");
            response.sendRedirect("/bookflights.jsp");
        }
        else if(!Float.valueOf(request.getParameter("priceconfirmation")).equals(Float.valueOf((String)session.getAttribute("totalprice")))){
            session.setAttribute("bookflighterror","Price confirmation does not match.");
            response.sendRedirect("/bookflights.jsp");
        }
        else{
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://aa3zjrg5cjqq3u.c9taiotksa6k.us-east-1.rds.amazonaws.com:3306/ebdb", "team10", "team1010");
                Statement stmt = con.createStatement();
                String search = "select* from tickets";
                ArrayList<String> ticketnumbers = new ArrayList<>();
                ArrayList<String> passengerstrings = new ArrayList<>();
                ArrayList<String> flightstrings = new ArrayList<>();

                for(int i = 0; i < Integer.valueOf((String)session.getAttribute("numpassengers"));i++){
                    ArrayList<Flight> flights = (ArrayList<Flight>) session.getAttribute("flightstobook");
                    int num = 100000;
                    search = "select * from tickets";
                    ResultSet rs = stmt.executeQuery(search);
                    if(rs.next()) {
                        rs.last();
                        num = rs.getInt("ticket_number") + 1;
                    }
                    ticketnumbers.add(""+num);
                    passengerstrings.add(""+num);
                    passengerstrings.add(request.getParameter("lastname"+i));
                    passengerstrings.add(request.getParameter("firstname"+i));
                    passengerstrings.add(request.getParameter("gender"+i));
                    passengerstrings.add(request.getParameter("dob"+i));
                    passengerstrings.add(request.getParameter("id"+i));
                    for(int j = 0; j < flights.size(); j++) {
                        Flight f = flights.get(j);

                        String arrday;
                        if(f.isSame_day()){
                            arrday = LocalDate.parse(f.getDepart_date()).toString();
                        }else {
                            arrday=LocalDate.parse(f.getDepart_date()).plusDays(1).toString();
                        }
                        if(i==0){
                            flightstrings.add(f.getDepart_city());
                            flightstrings.add(f.getArrive_city());
                            flightstrings.add(f.getDepart_date()+" "+f.getDepart_hours()+":"+String.format("%02d",f.getDepart_minutes())+" "+f.getDepart_AMPM()+" "+f.getDepart_timezone());
                            flightstrings.add(arrday+" "+f.getArrive_hours()+":"+String.format("%02d",f.getArrive_minutes())+" "+f.getArrive_AMPM()+" "+f.getArrive_timezone());
                            flightstrings.add("$"+String.format("%.2f",Integer.valueOf((String)session.getAttribute("numpassengers"))*Double.valueOf(((ArrayList<Float>) session.getAttribute("bookedprices")).get(j))));
                        }
                        search = "insert into tickets (ticket_number,gender,id,checked_in,dob,first_name,last_name," +
                                "email,depart_date,flight_id,class,arrive_date) values(" + num + ",'" + request.getParameter("gender" + i) + "'," +
                                "'" + request.getParameter("id" + i) + "',0,'" + request.getParameter("dob" + i) + "'," +
                                "'" + request.getParameter("firstname" + i) + "','" + request.getParameter("lastname" + i) + "'," +
                                "'" + ((User) session.getAttribute("currentuser")).getEmail() + "','" + f.getDepart_date() + "','" + f.getFlight_id() + "'," +
                                "'" + ((ArrayList<String>) session.getAttribute("bookedclasses")).get(j) + "','" + arrday + "')";
                        stmt.execute(search);

                    }
                }
                Message message = NewAccountServlet.getEmailMessage(((User) session.getAttribute("currentuser")).getEmail());
                message.setSubject("Iowa Air Flight Confirmation");
                BodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setText("This is to confirm your flight reservation on Iowa Air.");
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);
                messageBodyPart=new MimeBodyPart();
                for(String t : ticketnumbers) {
                    ReceiptMaker.makeReceipt(session, "receipt"+t, passengerstrings, flightstrings);
                }
                ServletContext sc = session.getServletContext();

                DataSource source = new FileDataSource(sc.getResource("/WEB-INF/receipts/").getPath()+"receipt"+ticketnumbers.get(0)+".pdf");
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName("receipt.pdf");
                multipart.addBodyPart(messageBodyPart);
                message.setContent(multipart);
                Transport.send(message);
            }catch(ClassNotFoundException e){
                e.printStackTrace();
            }catch(SQLException e){
                e.printStackTrace();
            }catch(MessagingException e){
                e.printStackTrace();
            }
            session.setAttribute("flightsearcherror", "A confirmation was sent to your email address.");
            response.sendRedirect("/index.jsp");
        }
    }
}
