package MainPackage;

import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Rachel on 3/27/2017.
 */
public class PriceComparator implements Comparator<Flight> {
    public int compare(Flight a, Flight b){
        if(Collections.min(AdminFlightServlet.getPrices(a))< Collections.min(AdminFlightServlet.getPrices(b))){
            return -1;
        }else{
            return 1;
        }
    }
}
