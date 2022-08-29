import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class Hospital2 extends HospitalBase {

    private PatientBase[] appointments;

    private String startTime = "08:00";
    private String endTime = "17:59";
    private String breakStart = "11:59";
    private String breakEnd = "13:00";

    private final int initialArraySize = 2;

    private int growthMultiplier;

    private int appointmentsSize;

    private int numAppointments;

    public Hospital2() {
        /* Add your code here! */
        this.appointments = new PatientBase[initialArraySize];
        this.appointmentsSize = initialArraySize;
        this.numAppointments = 0;
        this.growthMultiplier = 0;
    }

    @Override
    public boolean addPatient(PatientBase patient) {
        /* Add your code here! */
        // Add patient in O(n) by scanning array and inserting patient in order of time

        // Scan array in order and find last instance of the time before where patient is inserted
        // insert the new value at the last index of the array then keep swapping values to the left
        // until you find a value to the left that is <= the one you inserted.
        // Step 1 Add patient to end of array, assume array is correct size

        if (!Patient.validTime(startTime, endTime, breakStart, breakEnd, patient.getTime())) {
            return false;
        }

        // Is array large enough?
        // Doubling strategy array growth
        if (this.numAppointments == this.appointments.length) { // Need to expand array
           growArrayDoubleStrategy();
        }

        // Insert patient at end of array
        this.appointments[this.numAppointments++] = patient;
        System.out.println("Patient added to list");


        if (this.numAppointments == 1) { // First patient has been added
            return true;
        }

//        int newPatientIndex = numAppointments - 1; // New patient at last index of appointments list

        for (int i = this.numAppointments - 2; i >= 0; i--) {
            int newPatientIndex = i + 1;
            if (Patient.compareTimes(this.appointments[i].getTime(), patient.getTime()) <= 0) {
                break; // Patient is at correct position
            }
            // Swap the current patient index with the patient to the left
            swapPatients(i, newPatientIndex, this.appointments);
//            newPatientIndex--;
//            System.out.println("NEW REARRANGE");

        }
//        for (var patient1 : this.appointments) {
//            if (patient1 == null) {
//                break;
//            }
//            System.out.println(patient1);
//        }

        return true;
    }

    /** Increase array size by doubling strategy to achieve O(1) amortised array growth */
    public void growArrayDoubleStrategy() {
        int oldAppointmentsSize = this.appointmentsSize;
        this.appointmentsSize += Math.pow(2, this.growthMultiplier++);
        PatientBase[] newArray = new PatientBase[this.appointmentsSize];

        // Copy old array values into temp new array
        for (int i = 0; i < oldAppointmentsSize; i++) {
            newArray[i] = this.appointments[i];
        }
        this.appointments = newArray;
    }

    /** Swap the patients at index i and p */
    public void swapPatients(int i, int p, PatientBase[] appointments) {
        PatientBase tmp = appointments[i];
        appointments[i] = appointments[p];
        appointments[p] = tmp;
    }

    @Override
    public Iterator<PatientBase> iterator() {
        /* Add your code here! */
        return new Iterator<PatientBase>() {

            int currIndex = 0;
            @Override
            public boolean hasNext() {
                if (currIndex < numAppointments) {
                    return true;
                }
                return false;
            }

            @Override
            public PatientBase next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                return appointments[currIndex++];
            }
        };
    }

    /* Add any extra functions below */

    public static void main(String[] args) {
        /*
         * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
         * REMOVE THE MAIN METHOD BEFORE SUBMITTING TO THE AUTOGRADER
         * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
         * The following main method is provided for simple debugging only
         */
        var hospital = new Hospital2();
//        System.out.println(String.format("2^3 = %d", ));
        var p1 = new Patient("Max", "11:00");
        var p2 = new Patient("Alex", "13:15");
        var p3 = new Patient("George", "14:00");
        var p4 = new Patient("Damo", "17:00");
        var p5 = new Patient("Jamie", "08:00");
        var p6 = new Patient("Emily", "08:01");
        var p7 = new Patient("Bort", "08:00");
        var p8 = new Patient("LateMan", "18:00");
        hospital.addPatient(p1);
        for (var patient: hospital) {
            System.out.println(patient);
        }
        hospital.addPatient(p2);
        System.out.println(String.format("appointment size = %d", hospital.appointmentsSize));
        hospital.addPatient(p3);
        System.out.println(String.format("appointment size = %d", hospital.appointmentsSize));
        hospital.addPatient(p4);
        System.out.println(String.format("appointment size = %d", hospital.appointmentsSize));
        hospital.addPatient(p5);
        System.out.println(String.format("appointment size = %d", hospital.appointmentsSize));
        hospital.addPatient(p6);
        System.out.println(String.format("appointment size = %d", hospital.appointmentsSize));
        hospital.addPatient(p7);
        System.out.println(String.format("appointment size = %d", hospital.appointmentsSize));
        hospital.addPatient(p8);
        System.out.println(String.format("appointment size = %d", hospital.appointmentsSize));
        System.out.println(String.format("num appointments = %d", hospital.numAppointments));
        var patients = new Patient[] {p1, p2, p3};
        int i = 0;

//        for (var patient : hospital) {
//            System.out.println(patient);
////            assert Objects.equals(patient, patients[i++]);
//
////            if (!Objects.equals(patient, patients[i++])) {
////                System.err.println("Wrong patient encountered, check your implementation!");
////            }
//        }

        Iterator<PatientBase> it = hospital.iterator();
        while (it.hasNext()) {
            PatientBase patient = it.next();
            System.out.println(patient);
        }
    }
}
