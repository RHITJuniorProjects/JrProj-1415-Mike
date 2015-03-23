package rhit.jrProj.henry.firebase;

/**
 * Created by daveyle on 3/23/2015.
 */
public class DueDate {
    int day;
    int month;
    int year;
    private final static String[] months = {"Jan.", "Feb.", "Mar.", "Apr.",
            "May", "Jun.", "Jul.", "Aug.", "Sep.", "Oct.", "Nov.", "Dec."};
    static String nullDate = "No Due Date";

    public DueDate(String s1) {
        if (s1.equals(nullDate) || s1.equals("")) {
            day = -1;
            month = -1;
            year = -1;
        } else {
            int index1=s1.indexOf("-");
            int index2=s1.indexOf("-", index1+1);
            year = new Integer(s1.substring(0, index1)).intValue();
            month = new Integer(s1.substring(index1+1, index2)).intValue();
            day = new Integer(s1.substring(index2+1)).intValue();
        }
    }

    public DueDate(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public DueDate() {
        day = -1;
        month = -1;
        year = -1;
    }

    public int getDay() {
        return this.day;
    }

    public int getMonth() {
        return this.month;
    }

    public String getMonthName() {
        return months[month - 1];
    }

    public int getYear() {
        return this.year;
    }

    public String toString() {
        if (!hasNoDueDate()) {
            return year + "/" + month + "/" + day;
        } else {
            return nullDate;
        }
    }

    public String toStringFormatted() {
        if (!hasNoDueDate()) {

            return day + " " + getMonthName() + " " + year;
        } else {
            return nullDate;
        }

    }

    public boolean hasNoDueDate() {
        return (year == -1 && month == -1 && day == -1);
    }

    public int compareTo(DueDate d2, boolean newestFirst) {
        if (hasNoDueDate() && d2.hasNoDueDate()) {
            return 0;
        } else if (hasNoDueDate()) {
            return -1;
        } else if (d2.hasNoDueDate()) {
            return 1;
        } else {
            int result = 0;
            int year1 = getYear();
            int year2 = d2.getYear();
            int month1 = getMonth();
            int month2 = d2.getMonth();
            int day1 = getDay();
            int day2 = d2.getDay();
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


}