package MainPackage;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Rachel on 3/27/2017.
 */
public class ArriveTimeComparator implements Comparator<ArrayList<Flight>> {
    public int compare(ArrayList<Flight> a, ArrayList<Flight> b){
        if(a.get(0).isSame_day()&&a.get(a.size()-1).isSame_day()&&(!b.get(b.size()-1).isSame_day()||!b.get(0).isSame_day())){
            return -1;
        }else if(b.get(0).isSame_day()&&b.get(b.size()-1).isSame_day()&&(!a.get(a.size()-1).isSame_day()||!a.get(0).isSame_day())){
            return 1;
        }
        else {
            LocalTime atime = LocalTime.parse(String.format("%02d", a.get(a.size()-1).getArrive_hours()) + ":" + String.format("%02d", a.get(a.size()-1).getArrive_minutes()));
            LocalTime btime = LocalTime.parse(String.format("%02d", b.get(b.size()-1).getArrive_hours()) + ":" + String.format("%02d", b.get(b.size()-1).getArrive_minutes()));
            if (a.get(a.size()-1).getArrive_AMPM().equals("PM")&&a.get(a.size()-1).getArrive_hours()<12) {
                atime = LocalTime.parse(String.format("%02d", a.get(a.size()-1).getArrive_hours() + 12) + ":" + String.format("%02d", a.get(a.size()-1).getArrive_minutes()));
            }
            if (b.get(b.size()-1).getArrive_AMPM().equals("PM")&&b.get(b.size()-1).getArrive_hours()<12) {
                btime = LocalTime.parse(String.format("%02d", b.get(b.size()-1).getArrive_hours() + 12) + ":" + String.format("%02d", (b.get(b.size()-1).getArrive_minutes())));
            }
            return atime.compareTo(btime);
        }
    }
}
