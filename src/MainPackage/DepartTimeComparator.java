package MainPackage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Rachel on 3/27/2017.
 */
public class DepartTimeComparator implements Comparator<ArrayList<Flight>> {
    public int compare(ArrayList<Flight> a, ArrayList<Flight> b){
        LocalTime atime = LocalTime.parse(String.format("%02d",a.get(0).getDepart_hours())+":"+String.format("%02d",a.get(0).getDepart_minutes()));
        LocalTime btime = LocalTime.parse(String.format("%02d",b.get(0).getDepart_hours())+":"+String.format("%02d",b.get(0).getDepart_minutes()));
        if(a.get(0).getDepart_AMPM().equals("PM")&&a.get(0).getDepart_hours()<12){
            atime=LocalTime.parse(String.format("%02d",a.get(0).getDepart_hours()+12)+":"+String.format("%02d",a.get(0).getDepart_minutes()));
        }if(b.get(0).getDepart_AMPM().equals("PM")&&b.get(0).getDepart_hours()<12){
            btime=LocalTime.parse(String.format("%02d",b.get(0).getDepart_hours()+12)+":"+String.format("%02d",(b.get(0).getDepart_minutes())));
        }
        return atime.compareTo(btime);
    }
}
