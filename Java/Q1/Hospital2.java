import java.util.Iterator;

public class Hospital2 extends HospitalBase {

    private PatientBase[] appointmentSlots;

    private String startTime = "08:00";
    private String endTime = "17:59";
    private String breakStart = "11:59";
    private String breakEnd = "13:00";

    private int numAppointments;

    public Hospital2() {
        /* Add your code here! */
        this.appointmentSlots = new PatientBase[1]; // Start with array of size 1
        this.numAppointments = 0;
    }

    @Override
    public boolean addPatient(PatientBase patient) {
        /* Add your code here! */
        // Check valid time
        if (!Patient.validTime(startTime, endTime, breakStart, breakEnd, patient.getTime())) {
            return false;
        }

        // if array is not large enough grow array with doubling strategy
        if (this.numAppointments == this.appointmentSlots.length) {
            growArrayDoubleStrategy();
        }

        // Insert patient at next available appointment slot
        this.appointmentSlots[this.numAppointments++] = patient;

        // if the first patient was added then the list is already sorted can return
        if (this.numAppointments == 1) {
            return true;
        }

        // start at right most patient in array and swap with patient to its left if its greater
        // than the patient we are inserting
        for (int i = this.numAppointments - 2; i >= 0; i--) {
            int newPatientIndex = i + 1;

            // if patient to left of new patient is less than or equal to the current patient break
            if (this.appointmentSlots[i].compareTo(patient) <= 0) {
                break;
            }

            // Swap the current patient index with the patient to the left
            swapPatients(i, newPatientIndex, this.appointmentSlots);
        }
        return true;
    }

    /** Increase array size by doubling strategy to achieve O(1) amortised array growth */
    public void growArrayDoubleStrategy() {
        int oldAppointmentsSize = this.appointmentSlots.length;
        PatientBase[] newArray = new PatientBase[this.appointmentSlots.length * 2];

        // Copy old array values into temp new array
        for (int i = 0; i < oldAppointmentsSize; i++) {
            newArray[i] = this.appointmentSlots[i];
        }
        this.appointmentSlots = newArray;
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
                return appointmentSlots[currIndex++];
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
        var p1 = new Patient("Max", "11:00");
        var p2 = new Patient("Alex", "13:15");
        var p3 = new Patient("George", "14:00");
        var p4 = new Patient("Damo", "17:00");
        var p5 = new Patient("Jamie", "08:00");
        var p6 = new Patient("Emily", "08:01");
        var p7 = new Patient("Bort", "08:00");
        var p8 = new Patient("LateMan", "18:00");

        // iterate before adding a patient
        for (var patient : hospital) {
            System.out.println(patient);
        }

        // iterate after adding one patient
        System.out.println(hospital.addPatient(p1));
        System.out.println("one patient added to hospital");
        for (var patient: hospital) {
            System.out.println(patient);
        }
        System.out.println();

        hospital.addPatient(p2);
        System.out.println(String.format("appointment slots = %d",
                hospital.appointmentSlots.length));
        for (var patient: hospital) {
            System.out.println(patient);
        }
        System.out.println();

        hospital.addPatient(p3);
        System.out.println(String.format("appointment slots = %d", hospital.appointmentSlots.length));
        for (var patient: hospital) {
            System.out.println(patient);
        }
        System.out.println();

        hospital.addPatient(p4);
        System.out.println(String.format("appointment slots = %d", hospital.appointmentSlots.length));
        for (var patient: hospital) {
            System.out.println(patient);
        }
        System.out.println();

        hospital.addPatient(p5);
        System.out.println(String.format("appointment slots = %d", hospital.appointmentSlots.length));
        for (var patient: hospital) {
            System.out.println(patient);
        }
        System.out.println();


        for (var patient : hospital) {
            System.out.println(patient);
        }
        System.out.println();

        hospital.addPatient(p6);
        System.out.println(String.format("appointment slots = %d", hospital.appointmentSlots.length));
        hospital.addPatient(p7);
        System.out.println(String.format("appointment slots = %d", hospital.appointmentSlots.length));
        hospital.addPatient(p8);
        System.out.println(String.format("appointment slots = %d", hospital.appointmentSlots.length));
        System.out.println(String.format("num appointments = %d", hospital.numAppointments));

        for (var patient : hospital) {
            System.out.println(patient);
        }
        System.out.println();

        // add invalid patients
        System.out.println("Adding various invalid patients");
        System.out.println(hospital.addPatient(new Patient("Hackerman", "07:59")));
        System.out.println(hospital.addPatient(new Patient("Hackerman", "11:59")));
        System.out.println(hospital.addPatient(new Patient("Hackerman", "11:59")));
        System.out.println(hospital.addPatient(new Patient("Hackerman", "12:00")));
        System.out.println(hospital.addPatient(new Patient("Hackerman", "12:59")));
        System.out.println(hospital.addPatient(new Patient("Hackerman", "13:00")));
        System.out.println(hospital.addPatient(new Patient("Hackerman", "17:59")));
        System.out.println(hospital.addPatient(new Patient("Hackerman", "18:00")));
        System.out.println(hospital.addPatient(new Patient("Hackerman", "18:01")));

        for (var patient : hospital) {
            System.out.println(patient);
        }
        System.out.println();
    }
}
