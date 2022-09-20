import java.util.Iterator;
import java.util.Objects;

public class Hospital1 extends HospitalBase {

    /** Array of all available timeslots */
    private PatientBase[] appointments;

    /** Hospital working times and break times with 20min appointment slots */
    private String startTime = "08:00";
    private String endTime = "17:40";
    private String breakStart = "11:40";
    private String breakEnd = "13:00";
    private int numPossibleTimeSlots;

    private int appointmentLength = 20;

    public Hospital1() {
        /* Add your code here! */
        this.numPossibleTimeSlots = numTimeSlots();
        this.appointments = new Patient[numPossibleTimeSlots];
    }

    @Override
    public boolean addPatient(PatientBase patient) {
        /* Add your code here! */
        // Check time given is within working hours of hospital
        if (!Patient.validTime(startTime, endTime, breakStart, breakEnd, patient.getTime())) {
            return false;
        }

        // Check time is a multiple of the appointment length
        if (!validAppointmentTime(patient.getTime())) {
            return false;
        }

        // Check if there exists a Patient already at the index for the time specified
        if (appointments[timeToIndex(patient.getTime())] != null) {
            return false;
        }
        // time slot is free
        appointments[timeToIndex(patient.getTime())] = patient;
        return true;
    }

    @Override
    public Iterator<PatientBase> iterator() {
        /* Add your code here! */

        return new Iterator<PatientBase>() {
            int currentIndex = 0;
            @Override
            public boolean hasNext() {
                for (int i = currentIndex; i < numPossibleTimeSlots; i++) {
                    if (appointments[i] != null) { // found next patient index
                        currentIndex = i;
                        return true;
                    }
                }
                // iterated through entire array and didnt find another patient
                return false;
            }

            @Override
            public PatientBase next() {
                return appointments[currentIndex++];
            }
        };
    }

    /* Add any extra functions below */


    /** Calculates the number of 20 min time slots available accounting for the lunch break. */
    public int numTimeSlots() {
        return 1 + (toMinuteOfDay(endTime) - toMinuteOfDay(startTime)) / appointmentLength;
    }

    /** Converts a time string to an index */
    public int timeToIndex(String time) {
        return (toMinuteOfDay(time) - toMinuteOfDay(startTime)) / appointmentLength;
    }

    /** Converts a time string to the minutes in a day */
    public int toMinuteOfDay(String time) {
        int hours = Integer.parseInt(time.split(":")[0]);
        int mins = Integer.parseInt(time.split(":")[1]);
        return 60 * hours + mins;
    }

    /** Check the requested time is a multiple of the appointment time */
    public boolean validAppointmentTime(String time) {
        return toMinuteOfDay(time) % appointmentLength == 0;
    }

    public static void main(String[] args) {
        /*
         * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
         * REMOVE THE MAIN METHOD BEFORE SUBMITTING TO THE AUTOGRADER
         * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
         * The following main method is provided for simple debugging only
         */
        var hospital = new Hospital1();

//        String testTime = "17:40";
//        System.out.println("Time " + testTime + " is within hospital hours: " +
//                Patient.validTime(hospital.startTime, hospital.endTime, hospital.breakStart,
//                        hospital.breakEnd, testTime));
//        System.out.println("Time " + testTime + " is multiple of appt time: " +
//                hospital.validAppointmentTime(testTime));



//        System.out.println("num time slots: " + hospital.numTimeSlots());
//        System.out.println(hospital.appointments[0] == null);
//        var time = "18:00";
//        System.out.println(time + "=" + hospital.timeToIndex(time));

        // iterate before adding a patient
        for (var patient : hospital) {
            System.out.println(patient);
        }

        var p1 = new Patient("Max", "08:00");
        var p2 = new Patient("Alex", "13:00");
        var p3 = new Patient("George", "17:40");
        var p4 = new Patient("Jamie", "11:20");
        var p5 = new Patient("John", "09:40");
        var p6 = new Patient("Eily", "13:40");

//        hospital.addPatient(p1);
//        hospital.addPatient(p2);
//        hospital.addPatient(p3);
//        hospital.addPatient(p4);
//        hospital.addPatient(p5);
//        hospital.addPatient(p6);

        System.out.println(hospital.addPatient(p1));
        System.out.println("One patient added to hospital");
        for (var patient : hospital) {
            System.out.println(patient);
        }
        System.out.println();

        System.out.println(hospital.addPatient(p2));
        System.out.println(hospital.addPatient(p3));
        System.out.println(hospital.addPatient(p4));
        System.out.println(hospital.addPatient(p5));
        System.out.println(hospital.addPatient(p6));

        var patients = new Patient[] { p1, p2, p3 };
        int i = 0;
//        String time1 = "08:00";
//        String time2 = "08:01";
//        System.out.println(String.format("%s : %s = %d", time1, time2, Patient.compareTimes(time1
//                , time2)));
//        System.out.println(hospital.appointments.length);
//        for (var patient : patients) {
//            System.out.println(patient);
//        }
        for (var patient : hospital) {
            System.out.println(patient);
//            if (!Objects.equals(patient, patients[i++])) {
//                System.err.println("Wrong patient encountered, check your implementation!");
//            }
        }
        System.out.println();

        // Add more patients
        var p7 = new Patient("Moe", "10:00");
        var p8 = new Patient("Matt", "11:40");
        var p9 = new Patient("Eugene", "17:20");
        hospital.addPatient(p7);
        hospital.addPatient(p8);
        hospital.addPatient(p9);

        for (var patient : hospital) {
            System.out.println(patient);
        }
        System.out.println();

        System.out.println(hospital.addPatient(new Patient("Hackerman", "10:01")));
        System.out.println(hospital.addPatient(new Patient("Hackerman", "10:00")));
        System.out.println(hospital.addPatient(new Patient("Hackerman", "07:40")));
        System.out.println(hospital.addPatient(new Patient("Hackerman", "11:40")));
        System.out.println(hospital.addPatient(new Patient("Hackerman", "12:00")));
        System.out.println(hospital.addPatient(new Patient("Hackerman", "13:00")));
        System.out.println(hospital.addPatient(new Patient("Hackerman", "17:40")));
        System.out.println(hospital.addPatient(new Patient("Hackerman", "17:41")));
        System.out.println(hospital.addPatient(new Patient("Hackerman", "18:00")));
        System.out.println(hospital.addPatient(new Patient("Hackerman", "18:20")));

        for (var patient : hospital) {
            System.out.println(patient);
        }
    }
}
