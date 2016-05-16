package com.teamproject.functions;

/**
 * Created by 008M on 2016-05-12.
 */
public class LineIntersection {

    public static double przynaleznosc(double xx, double xy, double yx, double yy, double zx, double zy)    {
        double det; //wyznacznik macierzy

        det = xx*yy + yx*zy + zx*xy - zx*yy - xx*zy - yx*xy;
        if (det!=0)
            return 0 ;
        else {
            if ((Math.min(xx, yx) <= zx) && (zx <= Math.max(xx, yx)) &&
                    (Math.min(xy, yy) <= zy) && (zy <= Math.max(xy, yy)))
                return 1;
            else
                return 0;
        }
    }

    //  wyznacznik macierzy
    public static double det_matrix(double xx, double xy, double yx, double yy, double zx, double zy)
    {
        return (xx*yy + yx*zy + zx*xy - zx*yy - xx*zy - yx*xy);
    }
}
