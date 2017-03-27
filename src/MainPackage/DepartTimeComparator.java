package MainPackage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;

/**
 * Created by Rachel on 3/27/2017.
 */
public class DepartTimeComparator implements Comparator<Flight> {
    public int compare(Flight a, Flight b){
        LocalTime atime = LocalTime.parse(String.format("%02d",a.getDepart_hours())+":"+String.format("%02d",a.getDepart_minutes()));
        LocalTime btime = LocalTime.parse(String.format("%02d",b.getDepart_hours())+":"+String.format("%02d",b.getDepart_minutes()));
        if(a.getDepart_AMPM().equals("PM")){
            atime=LocalTime.parse(String.format("%02d",a.getDepart_hours()+12)+":"+String.format("%02d",a.getDepart_minutes()));
        }if(b.getDepart_AMPM().equals("PM")){
            btime=LocalTime.parse(String.format("%02d",b.getDepart_hours()+12)+":"+String.format("%02d",(b.getDepart_minutes())));
        }
        return atime.compareTo(btime);
    }
}
