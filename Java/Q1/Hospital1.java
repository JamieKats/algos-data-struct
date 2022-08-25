import java.util.Iterator;
import java.util.Objects;

/**
 * Qn 1
 * Option 1 is initialise array to hold all possible time slots then start adding in the new times
 * from the start of the array, inserting the new time into the correct location in the array.
 * This results in O(n) for insertion but O(1) for iterator.
 * Option 2 is initialise array to hold all possible time slots then add in time slots into their
 * correct indexes, resulting in a sparse array. This results in O(1) insertion and O(n) iterator
 *
 *
 * */
public class Hospital1 extends HospitalBase {

    /** Array of all available timeslots */
    private Patient[] appointments;

    /** Start time of hospital */
    private String startTime = "08:00";
    /** 20 min time slots therefore  */
    private String endTime = "18:00";
    private String breakStart = "12:00";
    private String breakEnd = "13:00";
    private int numPossibleTimeSlots;

    public Hospital1() {
        /* Add your code here! */
        this.numPossibleTimeSlots = numTimeSlots();
        this.appointments = new Patient[numPossibleTimeSlots];

    }

    @Override
    public boolean addPatient(PatientBase patient) {
        /* Add your code here! */
        if (!validTime(patient.getTime())) {
            return false;
        }
        return false;
    }

    // https://www.delftstack.com/howto/java/custom-iterator-java/
    @Override
    public Iterator<PatientBase> iterator() {
        /* Add your code here! */

        return new Iterator<PatientBase>() {
            int currentIndex = 0;
            @Override
            public boolean hasNext() {
                for (int i = currentIndex; i < numPossibleTimeSlots; i++) {
                    if (appointments[i] instanceof Patient) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public PatientBase next() {
                int i;
//                int nextVal;
                for (i = currentIndex; i < numPossibleTimeSlots; i++) {
                    if (appointments[i] instanceof Patient) {
//                        nextVal = i;
                        currentIndex = i;
                        break;
                    }
                }
//                currentIndex = nextVal;
                return appointments[i];
            }
        };
    }

    /* Add any extra functions below */

    public void getPaitent0() {
        System.out.println(appointments[0]);
    }

    /** Calculates the number of 20 min time slots available accounting for the lunch break. */
    public int numTimeSlots() {
        return (toMinuteOfDay(endTime) - toMinuteOfDay(startTime)) / 20;
    }

    /** Returns true if the given time is within the hospital start and end times and doesnt fall */
    public boolean validTime(String time) {

        if ((time.compareTo(startTime) >= 0 && time.compareTo(endTime) <= 0)
            && !(time.compareTo(breakStart) >= 0 && time.compareTo(breakEnd) < 0)) {
            return true;
        }
        return false;
    }

    public int toMinuteOfDay(String time) {
        int hours = Integer.parseInt(time.split(":")[0]);
        int mins = Integer.parseInt(time.split(":")[1]);
        return 60 * hours + mins;
    }



    public static void main(String[] args) {
        /*
         * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
         * REMOVE THE MAIN METHOD BEFORE SUBMITTING TO THE AUTOGRADER
         * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
         * The following main method is provided for simple debugging only
         */
        var hospital = new Hospital1();
        System.out.println(hospital.numTimeSlots());
        var p1 = new Patient("Max", "11:00");
        var p2 = new Patient("Alex", "13:00");
        var p3 = new Patient("George", "14:00");
        hospital.addPatient(p1);
        hospital.addPatient(p2);
        hospital.addPatient(p3);
        var patients = new Patient[] { p1, p2, p3 };
        int i = 0;
        for (var patient : hospital) {
            if (!Objects.equals(patient, patients[i++])) {
                System.err.println("Wrong patient encountered, check your implementation!");
            }
        }
    }
}
