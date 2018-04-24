package com.era.www.movietracker.utilities;

import java.text.DecimalFormat;

/**
 * Created by Mohamed on 4/24/2018.
 */

public class FormatUtils {

    public static String formatMovieRevenue(int movieRevenue) {
        double v = movieRevenue / 1000000.0;
        DecimalFormat numberFormat = new DecimalFormat("$##.##M");
        return numberFormat.format(v);
    }
}
