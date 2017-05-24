package de.sp.tools.objects;

/**
 * Created by martin on 24.05.2017.
 */
public class CompareTool {

    /**
     * usage:
     * compare(surname1, surname2, firstname1, firstname2, ...)
     *
     * compares with several hierarchies.
     * null < any other object
     *
     * @param o
     * @return
     */
    public static int compare(Comparable... o){

        for(int i = 0; i < o.length/2; i++){
            Comparable o1 = o[i*2];
            Comparable o2 = o[i*2+1];

            if(o1 == null && o2 != null){
                return -1;
            }

            if(o1 != null && o2 == null){
                return 1;
            }

            if(o1 == null){
                continue;
            }

            int v = o1.compareTo(o2);

            if(v != 0){
                return v;
            }

        }

        return 0;
    }




}
