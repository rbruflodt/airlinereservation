package MainPackage;

import java.time.LocalTime;
import java.util.Comparator;

/**
 * Created by Rachel on 3/27/2017.
 */
public class ArriveTimeComparator implements Comparator<Flight> {
    public int compare(Flight a, Flight b){
        if(a.isSame_day()&&!b.isSame_day()){
            return -1;
        }else if(b.isSame_day()&&!a.isSame_day()){
            return 1;
        }
        else {
            LocalTime atime = LocalTime.parse(String.format("%02d", a.getArrive_hours()) + ":" + String.format("%02d", a.getArrive_minutes()));
            LocalTime btime = LocalTime.parse(String.format("%02d", b.getArrive_hours()) + ":" + String.format("%02d", b.getArrive_minutes()));
            if (a.getArrive_AMPM().equals("PM")) {
                atime = LocalTime.parse(String.format("%02d", a.getArrive_hours() + 12) + ":" + String.format("%02d", a.getArrive_minutes()));
            }
            if (b.getArrive_AMPM().equals("PM")) {
                btime = LocalTime.parse(String.format("%02d", b.getArrive_hours() + 12) + ":" + String.format("%02d", (b.getArrive_minutes())));
            }
            return atime.compareTo(btime);
        }
    }
}
