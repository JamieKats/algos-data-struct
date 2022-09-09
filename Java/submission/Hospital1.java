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
 * Can we use.
 * Integer.parseInt, array.length, string.equals()?
 * */
public class Hospital1 extends HospitalBase {

    /** Array of all available timeslots */
    private PatientBase[] appointments;

    /** Start time of hospital */
    private String startTime = "08:00";
    /** 20 min time slots therefore  */
    private String endTime = "17:40";
    private String breakStart = "11:40";
    private String breakEnd = "13:00";
    private int numPossibleTimeSlots;

    private int appointmentLength = 20;

    public Hospital1() {
        /* Add your code here! */
        this.numPossibleTimeSlots = numTimeSlots();
//        this.numPossibleTimeSlots = 3;
        this.appointments = new Patient[numPossibleTimeSlots];
//        System.out.println(this.appointments.length);
    }

    @Override
    public boolean addPatient(PatientBase patient) {
        /* Add your code here! */
        // Check time given is valid
        if (!Patient.validTime(startTime, endTime, breakStart, breakEnd, patient.getTime())) {
            return false;
        }

//        System.out.println(patient.getName() + " goes to " + timeToIndex(patient.getTime()));

        // Check if there exists a Paitent already at the index for the time specified
        if (appointments[timeToIndex(patient.getTime())] != null) {
            return false;
        }
        // Patient doesnt exist in the db

        appointments[timeToIndex(patient.getTime())] = patient;
        return true;
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
                    if (appointments[i] != null) {
                        currentIndex = i;
                        return true;
                    }
                }
                return false;
            }

            @Override
            public PatientBase next() {
//                int i;
//                int nextVal;
                for (int i = currentIndex; i < numPossibleTimeSlots; i++) {
                    if (appointments[i] != null) {
//                        nextVal = i;
                        currentIndex = i + 1;
                        return appointments[i];
                    }
                }
//                currentIndex = nextVal;
//                return appointments[i];
                return null;
            }
        };
    }

    /* Add any extra functions below */

//    public void getPaitent0() {
//        System.out.println(appointments[0]);
//    }

    /** Calculates the number of 20 min time slots available accounting for the lunch break. */
    public int numTimeSlots() {
        return 1 + (toMinuteOfDay(endTime) - toMinuteOfDay(startTime)) / appointmentLength;
    }

    /** Converts a time string to an index */
    public int timeToIndex(String time) {
        return (toMinuteOfDay(time) - toMinuteOfDay(startTime)) / appointmentLength;
    }

    public int toMinuteOfDay(String time) {
        int hours = Integer.parseInt(time.split(":")[0]);
        int mins = Integer.parseInt(time.split(":")[1]);
        return 60 * hours + mins;
    }



//    public static void main(String[] args) {
//        /*
//         * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//         * REMOVE THE MAIN METHOD BEFORE SUBMITTING TO THE AUTOGRADER
//         * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//         * The following main method is provided for simple debugging only
//         */
//        var hospital = new Hospital1();
////        System.out.println("num time slots: " + hospital.numTimeSlots());
////        System.out.println(hospital.appointments[0] == null);
////        var time = "18:00";
////        System.out.println(time + "=" + hospital.timeToIndex(time));
//        var p1 = new Patient("Max", "08:00");
//        var p2 = new Patient("Alex", "13:00");
//        var p3 = new Patient("George", "17:40");
//        hospital.addPatient(p1);
//        hospital.addPatient(p2);
//        hospital.addPatient(p3);
//        var patients = new Patient[] { p1, p2, p3 };
//        int i = 0;
//        String time1 = "08:00";
//        String time2 = "08:01";
//        System.out.println(String.format("%s : %s = %d", time1, time2, Patient.compareTimes(time1
//                , time2)));
////        System.out.println(hospital.appointments.length);
////        for (var patient : patients) {
////            System.out.println(patient);
////        }
//        for (var patient : hospital) {
//            System.out.println(patient);
//            if (!Objects.equals(patient, patients[i++])) {
//                System.err.println("Wrong patient encountered, check your implementation!");
//            }
//        }
//    }
}
