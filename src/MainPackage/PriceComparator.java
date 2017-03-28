package MainPackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Rachel on 3/27/2017.
 */
public class PriceComparator implements Comparator<ArrayList<Flight>> {
    public int compare(ArrayList<Flight> a, ArrayList<Flight> b){
        float asum = 0;
        float bsum = 0;
        for(Flight p : a){
            asum+=Collections.min(AdminFlightServlet.getPrices(p));
        }
        for(Flight p : b){
            bsum+=Collections.min(AdminFlightServlet.getPrices(p));
        }
        if(asum< bsum){
            return -1;
        }else{
            return 1;
        }
    }
}
