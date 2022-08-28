public class Patient extends PatientBase {

    public Patient(String name, String time) {
        super(name, time);
    }

    @Override
    public int compareTo(PatientBase o) {
        /* Add your code here! */
        return compareTimes(this.getTime(), o.getTime());
    }

    /* Add any extra functions below */

    /** Loop through the time string and return -1 if time1 < time2 */
    static int compareTimes(String time1, String time2) {
        for (int i = 0; i < 5; i++) {
            if (time1.charAt(i) < time2.charAt(i)) {
                return -1;
            } else if (time1.charAt(i) != time2.charAt(i)) {
                return 1;
            }
        }
        return 0;
    }

    /** Returns true if the given time is within the hospital start and end times and doesnt fall */
    static boolean validTime(String startTime, String endTime, String breakStart,
                             String breakEnd, String time) {

        if ((compareTimes(time, startTime) >= 0 && compareTimes(time, endTime) <= 0)
                && !(compareTimes(time, breakStart) >= 0 && compareTimes(time, breakEnd) < 0)) {
            return true;
        }
        return false;
    }
}
