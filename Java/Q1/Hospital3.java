import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class Hospital3 extends HospitalBase {

    private PatientBase[] appointments;

    private String startTime = "08:00";
    private String endTime = "17:59";
    private String breakStart = "11:59";
    private String breakEnd = "13:00";

    private final int initialArraySize = 1;

    private int growthMultiplier;

    private int appointmentsSize;

    private int numAppointments;

    public Hospital3() {
        /* Add your code here! */
        this.appointments = new PatientBase[initialArraySize];
        this.appointmentsSize = initialArraySize;
        this.numAppointments = 0;
        this.growthMultiplier = 0;
    }

    @Override
    public boolean addPatient(PatientBase patient) {
        /* Add your code here! */
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

        return true;
    }

    /** Increase array size by doubling strategy to achieve O(1) amortised array growth */
    public void growArrayDoubleStrategy() {
        int oldAppointmentsSize = this.appointmentsSize;
        this.appointmentsSize = this.appointmentsSize * 2;
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

    public void decimalRadixSort(PatientBase[] appointments) {
//        PatientBase[] appointments = this.appointments;
        // Create key value list (0, x)
        BucketSortTuple[] bucketSortTuples = new BucketSortTuple[appointments.length];
        for (int i = 0; i < appointments.length; i++) {
            bucketSortTuples[i] = new BucketSortTuple(0, this.appointments[i]);
        }
        // for i<-0 to b-1 (each dimension of input list values)
        for (int i = 4; i >= 0; i--) {
            if (i == 2) { // Skip doing radix sort on the ":" index
                continue;
            }
            // replace key k of item (k, x) with bit xi of x
            for (int j = 0; j < this.numAppointments; j++) {
//                System.out.println(bucketSortTuples[j].getPatient());
                int timeChar =
                        Integer.parseInt(bucketSortTuples[j].getPatient().getTime().charAt(i));
//                System.out.println(timeChar);
                bucketSortTuples[j].key = timeChar;
                System.out.println(String.format("%s : %s", bucketSortTuples[j].getKey(),
                        bucketSortTuples[j].getPatient().getName()));
            }
            //Bucket sort
        }


    }

//    public void bucketSort()

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
        var hospital = new Hospital3();
        var p1 = new Patient("Max", "11:00");
        var p2 = new Patient("Alex", "13:15");
        var p3 = new Patient("George", "14:00");
        hospital.addPatient(p1);
        hospital.addPatient(p2);
        hospital.addPatient(p3);
        hospital.decimalRadixSort(hospital.appointments);
        var patients = new Patient[] {p1, p2, p3};
        int i = 0;
        for (var patient : hospital) {
            assert Objects.equals(patient, patients[i++]);
        }
    }
}

/** Private class to define the key value pair used in bucket sort */
class BucketSortTuple {
    int key;
    PatientBase patient;

    public BucketSortTuple(int key, PatientBase patient) {
        this.key = key;
        this.patient = patient;
    }

    public int getKey() { return key; }

    public PatientBase getPatient() { return patient; }
}