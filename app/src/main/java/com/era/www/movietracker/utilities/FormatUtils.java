package com.era.www.movietracker.utilities;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by Mohamed on 4/24/2018.
 */

public class FormatUtils {

    public static String formatMovieRevenue(int movieRevenue) {
        BigDecimal v = BigDecimal.valueOf(movieRevenue);
        DecimalFormat df = new DecimalFormat("###,###,###.00");
        //DecimalFormat numberFormat = new DecimalFormat("$##.##M");
        return df.format(v);
    }
}
