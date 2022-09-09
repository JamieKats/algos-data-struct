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

        // Insert patient at end of last inserted patient
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

    /** All values between and including leftIndex and rightIndex are used for sorting */
    public void mergeSort(PatientBase[] patients, int leftIndex, int rightIndex) {
//        System.out.println((rightIndex-leftIndex));
//        System.out.println("mergerSort called.....");
//        System.out.println(String.format("leftind = %s, rightind = %s", leftIndex, rightIndex));
//
        int size = rightIndex - leftIndex + 1;
//        System.out.println(String.format("patients size = %s", size));
//        for (int i = 0; i < size; i++) {
//            System.out.println(patients[i + leftIndex]);
//        }

        if (size < 2) {
            return;
        }




        if (leftIndex < rightIndex) {
            // TODO put size calc here
            int middle = size / 2;
//            System.out.println(String.format("middle = %s", middle));
//            System.out.println(String.format("rightIndex = %s", rightIndex));
            mergeSort(patients, leftIndex, middle - 1);
            mergeSort(patients, leftIndex + middle, rightIndex);
            merge(patients, leftIndex, leftIndex + middle, rightIndex);
        }
    }

    /** left, middle and right index are the indexes of the values that need to be swapped */
    public void merge(PatientBase[] patients, int leftIndex, int middleIndex, int rightIndex) {
//        System.out.println(String.format("merge called........"));
//        System.out.println(String.format("leftind = %s  middle = %s  rightInd = %s", leftIndex,
//                middleIndex, rightIndex));
//        for (int i = leftIndex; i < leftIndex - rightIndex + 1; i++) {
//            System.out.println(patients[i]);
//        }
        int leftSize = middleIndex - leftIndex; // size of first half of A
        int rightSize = rightIndex - middleIndex + 1; // size of second half of A
//        System.out.println(String.format("leftsize = %s", leftSize));
//        System.out.println(String.format("rightsize = %s", rightSize));
//        System.out.println("before left slice");
        PatientBase[] leftPatients = slicePatients(patients, leftIndex, middleIndex);
//        for (int i = 0; i < leftPatients.length; i++) {
//            System.out.println(leftPatients[i]);
//        }
//        System.out.println("before right slice");
        PatientBase[] rightPatients = slicePatients(patients, middleIndex, rightIndex + 1);
//        for (int i = 0; i < rightPatients.length; i++) {
//            System.out.println(rightPatients[i]);
//        }
//        System.out.println("");
//        System.out.println(leftSize);
//        System.out.println(rightSize);
//        System.out.println(leftPatients.length == leftSize);
//        System.out.println(rightPatients.length == rightSize);
        int i = 0;
        int j = 0;
        int k = leftIndex;
        while (i < leftSize && j < rightSize) {
            if (leftPatients[i] == null) {
                i = leftSize;
                break;
            } else if (rightPatients[j] == null) {
                j = rightSize;
                break;
            }

//            System.out.println("rightsize = " + rightSize);
//            System.out.println("leftsize = " + leftSize);
//            System.out.println("left patient = " + leftPatients[i]);
//            System.out.println("right patient = " + rightPatients[i]);
            if (rightPatients[i] != null && leftPatients[i].compareTo(rightPatients[j]) <= 0) {
                patients[k++] = leftPatients[i++];
            } else {
                patients[k++] = rightPatients[j++];
            }
        }
        while (i < leftSize) { // Copy rest of left array
            patients[k++] = leftPatients[i++];
        }
        while (j < rightSize) { // Copy rest of right array
            patients[k++] = rightPatients[j++];
        }
    }

    public PatientBase[] slicePatients(PatientBase[] patients, int leftIndex, int rightIndex) {
//        System.out.println(String.format("patients = "));
//        for (int i = leftIndex; i < rightIndex; i++) {
//            System.out.println(patients[i]);
//        }
//        System.out.println("");
//        System.out.println(String.format("leftIndex: %s, rightIndex: %s", leftIndex, rightIndex));
        int sliceSize = rightIndex - leftIndex;
//        System.out.println(String.format("sliceSize = %s", sliceSize));
        PatientBase[] patientsCopy = new PatientBase[sliceSize];
        for (int i = 0; i < sliceSize; i++) {
//            System.out.println(String.format("Left index %s", leftIndex));
//            System.out.println(String.format("patients.length %s", patients.length));
//            System.out.println(String.format("i + left ind %s", i + leftIndex));
            patientsCopy[i] = patients[i + leftIndex];
        }

        for (int i = 0; i < sliceSize; i++) {
//            System.out.println(patientsCopy[i]);
        }


        return patientsCopy;
    }

    @Override
    public Iterator<PatientBase> iterator() {
        /* Add your code here! */
        return new Iterator<PatientBase>() {

            int currIndex = 0;
            boolean isSorted = false;
            @Override
            public boolean hasNext() {
                if (!isSorted) {
                    mergeSort(appointments,0, numAppointments);
                    isSorted = true;
                }
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

//    public static void main(String[] args) {
//        /*
//         * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//         * REMOVE THE MAIN METHOD BEFORE SUBMITTING TO THE AUTOGRADER
//         * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//         * The following main method is provided for simple debugging only
//         */
////        String test = "true\ntrue\ntrue\n(Max, 11:00)\n(Alex, 13:15)\n(George, 14:00)\n";
////        String error = "true\ntrue\ntrue\njava.lang.NullPointerException\n\tat Patient.compareTo" +
////                "(Patient.java:10)\n\tat Patient.compareTo(Patient.java:1)\n\tat Hospital3.merge" +
////                "(Hospital3.java:130)\n\tat Hospital3.mergeSort(Hospital3.java:95)\n\tat " +
////                "Hospital3.mergeSort(Hospital3.java:94)\n\tat Hospital3$1.hasNext(Hospital3" +
////                ".java:179)\n\tat Q1Runner.main(Q1Runner.java:108)\n";
////        System.out.println(test);
////        System.out.println(error);
////        System.out.println("AFTER ERROR STUFFD.............................");
//
//
//        var hospital = new Hospital3();
//        var p1 = new Patient("Max", "17:00");
//        var p2 = new Patient("Alex", "13:15");
//        var p3 = new Patient("George", "14:00");
////        var p4 = new Patient("Geo", "11:40");
////        var p5 = new Patient("Jamie", "11:00");
////        var p6 = new Patient("Richard", "09:30");
////        var p7 = new Patient("Matt", "08:00");
////        var p6 = new Patient("John", "08:00");
////        var p1 = new Patient("Max", "13:10");
////        var p2 = new Patient("Alex", "17:00");
////        var p3 = new Patient("George", "17:20");
////        var p4 = new Patient("Geo", "11:40");
////        var p5 = new Patient("Jamie", "14:00");
////        var p6 = new Patient("Bort", "14:20");
//        hospital.addPatient(p1);
//        hospital.addPatient(p2);
//        hospital.addPatient(p3);
////        hospital.addPatient(p4);
////        hospital.addPatient(p5);
////        hospital.addPatient(p6);
////        hospital.addPatient(p7);
////        hospital.mergeSort(hospital.appointments, 0, hospital.numAppointments - 1);
////        System.out.println("/////RESULT OF MERGESORT/////");
//
////        System.out.println(hospital.numAppointments);
////        hospital.merge(hospital.appointments, 0, hospital.numAppointments / 2,
////                hospital.numAppointments - 1);
////        for (int i = 0; i < hospital.numAppointments; i++) {
////            System.out.println(hospital.appointments[i]);
////        }
//
//        // Test slice function
////        PatientBase[] test = hospital.slicePatients(hospital.appointments, 4,
////                hospital.numAppointments);
////        for (int i = 0; i < test.length; i++) {
////            System.out.println(test[i]);
////        }
//
//
////        var patients = new Patient[] {p1, p2, p3};
//        var patients = new Patient[] {p2, p3, p1};
//        int i = 0;
//        for (var patient : hospital) {
//            System.out.println(patient);
//            assert Objects.equals(patient, patients[i++]);
//        }
//    }
}

/** Private class to define the key value pair used in bucket sort */
//class BucketSortTuple {
//    int key;
//    PatientBase patient;
//
//    public BucketSortTuple(int key, PatientBase patient) {
//        this.key = key;
//        this.patient = patient;
//    }
//
//    public int getKey() { return key; }
//
//    public PatientBase getPatient() { return patient; }
//}