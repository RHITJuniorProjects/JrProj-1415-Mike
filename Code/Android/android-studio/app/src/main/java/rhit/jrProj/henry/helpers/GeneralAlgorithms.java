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
    private final static String[] months = {"Jan.", "Feb.", "Mar.", "Apr.",
            "May", "Jun.", "Jul.", "Aug.", "Sep.", "Oct.", "Nov.", "Dec."};

    public GeneralAlgorithms() {
        // TODO Auto-generated constructor stub
    }

    /**
     * compares two strings together ignoring case and putting 10 after 9
     * instead of after 1
     *
     * @param s1
     * @param s2
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

    public static int compareToByDate(String s1, String s2, boolean newestFirst) {
        if (s1 == s2)
            return 0;
        else if (s1.equals("No Due Date") || s1.equals(""))
            return -1;
        else if (s2.equals("No Due Date") || s2.equals(""))
            return 1;
        else {
            int result = 0;
            int year1 = new Integer(s1.substring(0, 4)).intValue();
            int year2 = new Integer(s2.substring(0, 4)).intValue();
            int month1 = new Integer(s1.substring(5, 7)).intValue();
            int month2 = new Integer(s2.substring(5, 7)).intValue();
            int day1 = new Integer(s1.substring(8)).intValue();
            int day2 = new Integer(s2.substring(8)).intValue();
            if (year1 != year2) {
                result = (year1 < year2) ? -1 : 1;
            } else {
                if (month1 != month2) {
                    result = (month1 < month2) ? -1 : 1;
                } else {
                    if (day1 != day2) {
                        result = (day1 < day2) ? -1 : 1;
                    } else {
                        result = 0;
                    }
                }
            }
            if (newestFirst) {
                return -1 * result;
            } else {
                return result;
            }
        }
    }

    /**
     * Formats Due date to dd/mm/yyyy
     *
     * @return formatted due date as String
     */
    public static String getDueDateFormatted(String s1) {
        if (s1.equals("No Due Date") || s1.equals(""))
            return s1;
        int yearend1 = s1.indexOf("/");
        if (yearend1 == -1) {
            yearend1 = s1.indexOf("-");
        }
        int monthend1 = s1.lastIndexOf("/");
        if (monthend1 == -1) {
            monthend1 = s1.lastIndexOf("-");
        }

        String year1 = s1.substring(0, yearend1);

        int month1 = new Integer(s1.substring(yearend1 + 1, monthend1))
                .intValue();

        String day1 = s1.substring(monthend1 + 1);
        String month = months[month1 - 1];
        return day1 + " " + month + " " + year1;

    }

    public static HashMap<String, Double> getRatio(Milestone m) {
        HashMap<String, List<Double>> map = new HashMap<String, List<Double>>(); //username, list of ratios  (one for each task)
        HashMap<String, Double> map2 = new HashMap<String, Double>(); //username, ratio (for all tasks)
        Random rand = new Random();
        for (Task t : m.getTasks()) {
            double actual = t.getHoursSpent();
//			actual=rand.nextInt(9)+1;
            double estimate = t.getOriginalHoursEstimate();
//			estimate=rand.nextInt(9)+1;
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
