package MainPackage;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Rachel on 4/29/2017.
 */
public class BoardingPassMaker {
    public static void makeBoardingPass(HttpSession session, String filename, ArrayList<String> passengerstrings, ArrayList<String> flightstrings){
        Document document = new Document();
        ServletContext sc = session.getServletContext();
        Font titleFont = new Font(Font.FontFamily.COURIER,20,Font.BOLD, new BaseColor(44,113,201));
        Font headerfont = new Font(Font.FontFamily.COURIER,16,Font.BOLD, new BaseColor(44,113,201));
        Font tableheaderfont = new Font(Font.FontFamily.COURIER,12,Font.BOLD, BaseColor.BLACK);
        try {
            Files.deleteIfExists(Paths.get(sc.getRealPath("/WEB-INF/receipts/"+filename+".pdf")));
            Files.createFile(Paths.get(sc.getRealPath("/WEB-INF/receipts/"+filename+".pdf")));
            PdfWriter.getInstance(document, new FileOutputStream((sc.getResource("/WEB-INF/receipts/").getPath()+filename+".pdf")));
            document.open();
            document.addTitle("Iowa Air Boarding Pass");
            document.add(new Paragraph("Iowa Air Boarding Pass",titleFont));
            Paragraph date = new Paragraph();
            date.add("" + new Date());
            document.add(date);
            Jpeg planelogo = new Jpeg(sc.getResource("/WEB-INF/Images/plane.jpg"));
            planelogo.scaleToFit(400,600);
            planelogo.setAlignment(Element.ALIGN_CENTER);
            document.add(planelogo);
            document.add(new Paragraph(""));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Passenger",headerfont));
            document.add(new Paragraph(" "));
            PdfPTable passengertable = new PdfPTable(5);
            PdfPCell cell = new PdfPCell(new Phrase("Ticket #",tableheaderfont));
            cell.setBorderColor(BaseColor.WHITE);
            passengertable.addCell(cell);
            cell = new PdfPCell(new Phrase("Last Name",tableheaderfont));
            cell.setBorderColor(BaseColor.WHITE);
            passengertable.addCell(cell);
            cell = new PdfPCell(new Phrase("First Name",tableheaderfont));
            cell.setBorderColor(BaseColor.WHITE);
            passengertable.addCell(cell);
            cell = new PdfPCell(new Phrase("Seat",tableheaderfont));
            cell.setBorderColor(BaseColor.WHITE);
            passengertable.addCell(cell);
            cell = new PdfPCell(new Phrase("Bags",tableheaderfont));
            cell.setBorderColor(BaseColor.WHITE);
            passengertable.addCell(cell);
            passengertable.setHeaderRows(1);
            int count = 0;
            while(count<passengerstrings.size()) {
                cell = new PdfPCell(new Phrase(" "));
                cell.setBorderColor(BaseColor.WHITE);
                passengertable.addCell(cell);
                cell = new PdfPCell(new Phrase(" "));
                cell.setBorderColor(BaseColor.WHITE);
                passengertable.addCell(cell);
                cell = new PdfPCell(new Phrase(" "));
                cell.setBorderColor(BaseColor.WHITE);
                passengertable.addCell(cell);
                cell = new PdfPCell(new Phrase(" "));
                cell.setBorderColor(BaseColor.WHITE);
                passengertable.addCell(cell);
                cell = new PdfPCell(new Phrase(" "));
                cell.setBorderColor(BaseColor.WHITE);
                passengertable.addCell(cell);


                cell = new PdfPCell(new Phrase(passengerstrings.get(count++)));
                cell.setBorderColor(BaseColor.WHITE);
                passengertable.addCell(cell);
                cell = new PdfPCell(new Phrase(passengerstrings.get(count++)));
                cell.setBorderColor(BaseColor.WHITE);
                passengertable.addCell(cell);
                cell = new PdfPCell(new Phrase(passengerstrings.get(count++)));
                cell.setBorderColor(BaseColor.WHITE);
                passengertable.addCell(cell);
                cell = new PdfPCell(new Phrase(passengerstrings.get(count++)));
                cell.setBorderColor(BaseColor.WHITE);
                passengertable.addCell(cell);
                cell = new PdfPCell(new Phrase(passengerstrings.get(count++)));
                cell.setBorderColor(BaseColor.WHITE);
                passengertable.addCell(cell);

            }
            passengertable.setHorizontalAlignment(Element.ALIGN_LEFT);
            float[] widths = {100,100,100,70,100};
            passengertable.setTotalWidth(widths);
            passengertable.setLockedWidth(true);
            Paragraph tablepar = new Paragraph();
            tablepar.setAlignment(Element.ALIGN_LEFT);
            tablepar.add(passengertable);
            document.add(tablepar);

            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Flights",headerfont));
            document.add(new Paragraph(" "));
            PdfPTable flighttable = new PdfPTable(4);
            cell = new PdfPCell(new Phrase("From",tableheaderfont));
            cell.setBorderColor(BaseColor.WHITE);
            flighttable.addCell(cell);
            cell = new PdfPCell(new Phrase("To",tableheaderfont));
            cell.setBorderColor(BaseColor.WHITE);
            flighttable.addCell(cell);
            cell = new PdfPCell(new Phrase("Depart",tableheaderfont));
            cell.setBorderColor(BaseColor.WHITE);
            flighttable.addCell(cell);
            cell = new PdfPCell(new Phrase("Arrive",tableheaderfont));
            cell.setBorderColor(BaseColor.WHITE);
            flighttable.addCell(cell);
            flighttable.setHeaderRows(1);
            count = 0;
            while(count<flightstrings.size()) {
                cell = new PdfPCell(new Phrase(" "));
                cell.setBorderColor(BaseColor.WHITE);
                flighttable.addCell(cell);
                cell = new PdfPCell(new Phrase(" "));
                cell.setBorderColor(BaseColor.WHITE);
                flighttable.addCell(cell);
                cell = new PdfPCell(new Phrase(" "));
                cell.setBorderColor(BaseColor.WHITE);
                flighttable.addCell(cell);
                cell = new PdfPCell(new Phrase(" "));
                cell.setBorderColor(BaseColor.WHITE);
                flighttable.addCell(cell);



                cell = new PdfPCell(new Phrase(flightstrings.get(count++)));
                cell.setBorderColor(BaseColor.WHITE);
                flighttable.addCell(cell);
                cell = new PdfPCell(new Phrase(flightstrings.get(count++)));
                cell.setBorderColor(BaseColor.WHITE);
                flighttable.addCell(cell);
                cell = new PdfPCell(new Phrase(flightstrings.get(count++)));
                cell.setBorderColor(BaseColor.WHITE);
                flighttable.addCell(cell);
                cell = new PdfPCell(new Phrase(flightstrings.get(count++)));
                cell.setBorderColor(BaseColor.WHITE);
                flighttable.addCell(cell);


            }

            flighttable.setHorizontalAlignment(Element.ALIGN_LEFT);
            float[] widths2={90,90,140,140};
            flighttable.setTotalWidth(widths2);
            flighttable.setLockedWidth(true);
            Paragraph tablepar2 = new Paragraph();
            tablepar2.setAlignment(Element.ALIGN_LEFT);
            tablepar2.add(flighttable);
            document.add(tablepar2);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
