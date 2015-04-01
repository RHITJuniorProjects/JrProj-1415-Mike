package rhit.jrProj.henry.helpers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import rhit.jrProj.henry.firebase.Milestone;
import rhit.jrProj.henry.firebase.Task;

import android.util.Log;

import java.util.Random;

public class GeneralAlgorithms {


    public GeneralAlgorithms() {
        // TODO Auto-generated constructor stub
    }

    /**
     * compares two strings together ignoring case and putting 10 after 9
     * instead of after 1
     *
     * @return
     */
    private final static String getChunk(String s, int slength, int marker) {
        StringBuilder chunk = new StringBuilder();
        char c = s.charAt(marker);
        chunk.append(c);
        marker++;
        if (Character.isDigit(c)) {
            while (marker < slength) {
                c = s.charAt(marker);
                if (!Character.isDigit(c))
                    break;
                chunk.append(c);
                marker++;
            }
        } else {
            while (marker < slength) {
                c = s.charAt(marker);
                if (Character.isDigit(c))
                    break;
                chunk.append(c);
                marker++;
            }
        }
        return chunk.toString();
    }

    public static int compareToIgnoreCase(String s1, String s2) {
        System.out.println(s1 + "\t\t\t" + s2);
        if (s1 == null || s2 == null) {
            return 0;
        }
        int i = 0;
        int j = 0;
        int s1len = s1.length();
        int s2len = s2.length();
        while (i < s1len && j < s2len) {
            String c1 = getChunk(s1, s1len, i);
            i += c1.length();

            String c2 = getChunk(s2, s2len, j);
            j += c2.length();

            // If both chunks contain numeric characters, sort them numerically
            int result = 0;
            if (Character.isDigit(c1.charAt(0))
                    && Character.isDigit(c2.charAt(0))) {
                // Simple chunk comparison by length.
                int c1Length = c1.length();
                result = c1Length - c2.length();
                // If equal, the first different number counts
                if (result == 0) {
                    for (int k = 0; k < c1Length; k++) {
                        result = c1.charAt(k) - c2.charAt(k);
                        if (result != 0) {
                            return result;
                        }
                    }
                }
            } else {
                result = c1.compareToIgnoreCase(c2);
            }

            if (result != 0)
                return result;
        }
        return s1len - s2len;
    }


    public static HashMap<String, Double> getRatio(Milestone m) {
        HashMap<String, List<Double>> map = new HashMap<String, List<Double>>(); //username, list of ratios  (one for each task)
        HashMap<String, Double> map2 = new HashMap<String, Double>(); //username, ratio (for all tasks)
        Random rand = new Random();
        for (Task t : m.getTasks()) {
            double actual = t.getHoursSpent();
            double estimate = t.getOriginalHoursEstimate();
            double ratio = (actual - estimate) / estimate;
            if (map.containsKey(t.getAssignedUserName())) {
                map.get(t.getAssignedUserName()).add(ratio);
            } else {
                List<Double> ls = new ArrayList<Double>();
                ls.add(ratio);
                map.put(t.getAssignedUserName(), ls);
            }
        }
        for (String key : map.keySet()) {
            List<Double> d = map.get(key);
            double ratio = 0;
            for (int i = 0; i < d.size(); i++) {
                double ratio2 = d.get(i);
                ratio += ratio2;
            }
            ratio = ratio / d.size();
            ratio = Math.floor(ratio * 10000) / 10000;
            map2.put(key, ratio);

        }
        return map2;
    }

    public static Date EpochConvert(int x) {
        return new Date(x);
    }


}
