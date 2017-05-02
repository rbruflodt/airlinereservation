package MainPackage;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Rachel on 4/8/2017.
 */
public class TicketDepartTimeComparator implements Comparator<ArrayList<String>> {
    public int compare(ArrayList<String> a, ArrayList<String> b){
        LocalDateTime atime;

            atime=LocalDateTime.parse(a.get(3) + "T" + a.get(4).substring(0, a.get(4).length() - 9));
        LocalDateTime btime;

            btime=LocalDateTime.parse(b.get(3)+"T"+b.get(4).substring(0,b.get(4).length()-9));

        if(a.get(4).substring(a.get(4).length()-8,a.get(4).length()-5).equals("PM")&&Integer.valueOf(a.get(4).substring(a.get(4).length()-8,a.get(4).length()-6))<12){
            atime=LocalDateTime.parse(a.get(3)+"T"+String.valueOf(Integer.valueOf(a.get(4).substring(a.get(4).length()-8,a.get(4).length()-6))+12)+a.get(4).substring(2,a.get(4).length()-9));
        }if(b.get(4).substring(b.get(4).length()-8,b.get(4).length()-5).equals("PM")&&Integer.valueOf(b.get(4).substring(b.get(4).length()-8,b.get(4).length()-6))<12){
            btime=LocalDateTime.parse(b.get(3)+"T"+String.valueOf(Integer.valueOf(b.get(4).substring(b.get(4).length()-8,b.get(4).length()-6))+12)+b.get(4).substring(2,b.get(4).length()-9));
        }
        return atime.compareTo(btime);
    }
}
