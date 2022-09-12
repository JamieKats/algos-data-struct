import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class Hospital3 extends HospitalBase {

    private Node head;

    private Node tail;

    private int numAppointments;

    private String startTime = "08:00";
    private String endTime = "17:59";
    private String breakStart = "11:59";
    private String breakEnd = "13:00";

    public Hospital3() {
        /* Add your code here! */
        this.head = null;
        this.tail = null;
        this.numAppointments = 0;
    }

    @Override
    public boolean addPatient(PatientBase patient) {
        /* Add your code here! */
        // Check time is valid
        if (!Patient.validTime(startTime, endTime, breakStart, breakEnd, patient.getTime())) {
            return false;
        }

        // create node
        Node newPatientNode = new Node(patient);

        if (this.numAppointments == 0) { // adding first patientNode
            this.head = newPatientNode;
            this.tail = newPatientNode;
        } else {
            this.tail.setNext(newPatientNode);
            Node oldTail = this.tail;
            this.tail = newPatientNode;
            newPatientNode.setPrevious(oldTail);
        }
        this.numAppointments++;
        return true;
    }

//    /** Swap the patients at index i and p */
//    public void swapPatients(int i, int p, PatientBase[] appointments) {
//        PatientBase tmp = appointments[i];
//        appointments[i] = appointments[p];
//        appointments[p] = tmp;
//    }

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
        for (int i = leftIndex; i < rightIndex; i++) {
            System.out.println(patients[i]);
        }
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

            Node currentNode = getHead();
            @Override
            public boolean hasNext() {
                if (!isSorted) {
//                    mergeSort(appointments,0, numAppointments);
                    isSorted = true;
                }
                if (this.currentNode != null) {
                    return true;
                }
                return false;
            }

            @Override
            public PatientBase next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                Node nextNode = this.currentNode;
                this.currentNode = this.currentNode.getNext();
                return nextNode.getPatient();
            }
        };
    }

    /* Add any extra functions below */

    public Node getHead() { return this.head; }

    public static void main(String[] args) {
        /*
         * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
         * REMOVE THE MAIN METHOD BEFORE SUBMITTING TO THE AUTOGRADER
         * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
         * The following main method is provided for simple debugging only
         */
//        String test = "true\ntrue\ntrue\n(Max, 11:00)\n(Alex, 13:15)\n(George, 14:00)\n";
//        String error = "true\ntrue\ntrue\njava.lang.NullPointerException\n\tat Patient.compareTo" +
//                "(Patient.java:10)\n\tat Patient.compareTo(Patient.java:1)\n\tat Hospital3.merge" +
//                "(Hospital3.java:130)\n\tat Hospital3.mergeSort(Hospital3.java:95)\n\tat " +
//                "Hospital3.mergeSort(Hospital3.java:94)\n\tat Hospital3$1.hasNext(Hospital3" +
//                ".java:179)\n\tat Q1Runner.main(Q1Runner.java:108)\n";
//        System.out.println(test);
//        System.out.println(error);
//        System.out.println("AFTER ERROR STUFFD.............................");


        var hospital = new Hospital3();
        var p1 = new Patient("Max", "11:00");
        var p2 = new Patient("Alex", "13:15");
        var p3 = new Patient("George", "14:00");
        var p4 = new Patient("Geo", "11:40");
        var p5 = new Patient("Jamie", "11:00");
        var p6 = new Patient("Richard", "09:30");
        var p7 = new Patient("Matt", "08:00");
//        var p6 = new Patient("John", "08:00");
//        var p1 = new Patient("Max", "13:10");
//        var p2 = new Patient("Alex", "17:00");
//        var p3 = new Patient("George", "17:20");
//        var p4 = new Patient("Geo", "11:40");
//        var p5 = new Patient("Jamie", "14:00");
//        var p6 = new Patient("Bort", "14:20");

        hospital.addPatient(p1);
        hospital.addPatient(p2);
        hospital.addPatient(p3);
        hospital.addPatient(p4);
        hospital.addPatient(p5);
        hospital.addPatient(p6);
        hospital.addPatient(p7);
//        hospital.mergeSort(hospital.appointments, 0, hospital.numAppointments - 1);
//        System.out.println("/////RESULT OF MERGESORT/////");

//        System.out.println(hospital.numAppointments);
//        hospital.merge(hospital.appointments, 0, hospital.numAppointments / 2,
//                hospital.numAppointments - 1);
//        for (int i = 0; i < hospital.numAppointments; i++) {
//            System.out.println(hospital.appointments[i]);
//        }

        // Test slice function
//        PatientBase[] test = hospital.slicePatients(hospital.appointments, 4,
//                hospital.numAppointments);
//        for (int i = 0; i < test.length; i++) {
//            System.out.println(test[i]);
//        }


//        var patients = new Patient[] {p1, p2, p3};
//        var patients = new Patient[] {p2, p3, p1};
        int i = 0;
//        for (int j = 0; j <  hospital.appointments.length; j++) {
//            System.out.println(hospital.appointments[j]);
//        }
        for (var patient : hospital) {
            System.out.println(patient);
//            assert Objects.equals(patient, patients[i++]);
        }
        System.out.println("Linked list printed above...");
//        System.out.println(hospital.getHead().getPatient());
//        System.out.println(hospital.getHead().getNext().getPatient());
//        System.out.println(hospital.getHead().getNext().getNext().getPatient());
//        System.out.println(hospital.getHead().getNext().getNext().getNext());
    }
}

class Node {
    PatientBase patient;

    Node previous;

    Node next;

    public Node(PatientBase patient) {
        this.patient = patient;
    }

    public PatientBase getPatient() { return patient; }

    public Node getPrevious() { return previous; }

    public Node getNext() { return next; }

    public void setPrevious(Node node) { this.previous = node; }

    public void setNext(Node node) { this.next = node; }
}

//class appointmentsList {
//    Node head;
//
//    Node root;
//
//    appointmentsList() {
//        this.head = null;
//        this.root = null;
//    }
//
//
//
//}
