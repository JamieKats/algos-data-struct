import java.util.Iterator;
import java.util.NoSuchElementException;

public class Hospital3 extends HospitalBase {

    DoublyLinkedList appointments;

    private String startTime = "08:00";
    private String endTime = "17:59";
    private String breakStart = "11:59";
    private String breakEnd = "13:00";

    public Hospital3() {
        /* Add your code here! */
        this.appointments = new DoublyLinkedList();
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

        // add node to end of appointments doubly linked list
        this.appointments.addNode(newPatientNode);
        return true;
    }

    @Override
    public Iterator<PatientBase> iterator() {
        /* Add your code here! */
        return new Iterator<PatientBase>() {

            boolean isSorted = false;

            DoublyLinkedList list = sort(appointments);

            Node currentNode = list.head;

            @Override
            public boolean hasNext() {
//                if (!isSorted) {
////                    System.out.println();
////                    System.out.println();
////                    System.out.println(getHead().getPatient());
////                    System.out.println(getTail().getPatient());
////                    System.out.println(numAppointments);
////                    System.out.println();
////                    System.out.println();
////                    head = mergeSort(getHead(), getTail(), numAppointments);
//                    head = sort(getHead(), getTail(), numAppointments);
//                    isSorted = true;
//                }

                if (this.currentNode != null) {
                    return true;
                }
                return false;
            }

            @Override
            public PatientBase next() {
                if (!this.hasNext()) {
                    System.out.println(currentNode.patient);
                    throw new NoSuchElementException();
                }
                Node nextNode = this.currentNode;
                this.currentNode = this.currentNode.next;
                return nextNode.getPatient();
            }
        };
    }

    /* Add any extra functions below */
    public DoublyLinkedList sort(DoublyLinkedList list) {
        DoublyLinkedList sortedList = mergeSort(list);
        this.appointments = sortedList;
        return sortedList;
    }

    /** All values between and including leftIndex and rightIndex are used for sorting */
    public DoublyLinkedList mergeSort(DoublyLinkedList list) {
        // list with one or no elements is already sorted
        if (list.size == 0 || list.size == 1) {
            return list;
        }

        Node middleNode = getMiddleNode(list.head, list.tail, list.size);
        int middleNodeNumber = getMiddleNodeNumber(list.size);

        DoublyLinkedList rightList = this.appointments.sliceList(middleNode.next, list.tail,
                list.size - middleNodeNumber);
        DoublyLinkedList leftList = this.appointments.sliceList(list.head, middleNode,
                middleNodeNumber);

        DoublyLinkedList leftSorted = mergeSort(leftList);
        DoublyLinkedList rightSorted = mergeSort(rightList);
        return merge(leftSorted, rightSorted);
    }

    /** left, middle and right index are the indexes of the values that need to be swapped */
    public DoublyLinkedList merge(DoublyLinkedList leftList, DoublyLinkedList rightList) {
        Node leftNode = leftList.head;
        Node rightNode = rightList.head;
        DoublyLinkedList mergedList = new DoublyLinkedList();

        while (leftNode != null && rightNode != null) {
            if (leftNode.getPatient().compareTo(rightNode.getPatient()) <= 0) {
                mergedList.addNode(leftNode);
                leftNode = leftNode.next;
            } else {
                mergedList.addNode(rightNode);
                rightNode = rightNode.next;
            }
        }
        while (leftNode != null) { // Copy rest of left array
            mergedList.addNode(leftNode);
            leftNode = leftNode.next;
        }
        while (rightNode != null) { // Copy rest of right array
            mergedList.addNode(rightNode);
            rightNode = rightNode.next;
        }
        return mergedList;
    }

    public Node getMiddleNode(Node head, Node tail, int numNodes) {
        if (numNodes == 1) {
            return head;
        }
        // calculate the middle node
        int middleNum = getMiddleNodeNumber(numNodes);

        Node middleNode = head;
        for (int i = 1; i < middleNum; i++) {
            middleNode = middleNode.next;
        }
        return middleNode;
    }

    public int getMiddleNodeNumber(int numNodes) {
        if (numNodes % 2 == 0) {
            return numNodes / 2;
        } else {
            return numNodes / 2;
        }
    }

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
        var p1 = new Patient("Max", "15:00");
        var p2 = new Patient("Alex", "13:15");
        var p3 = new Patient("George", "08:00");
        var p4 = new Patient("Geo", "11:40");
        var p5 = new Patient("Jamie", "11:00");
        var p6 = new Patient("Richard", "09:30");
//        var p7 = new Patient("Matt", "08:00");
//        var p6 = new Patient("John", "08:00");
//        var p1 = new Patient("Max", "08:10");
//        var p2 = new Patient("Alex", "09:00");
//        var p3 = new Patient("George", "10:20");
//        var p4 = new Patient("Geo", "11:00");
//        var p5 = new Patient("Jamie", "14:00");
//        var p6 = new Patient("Bort", "14:20");

        // iterate before adding a patient
        System.out.println("iterating with no patients");
        for (var patient : hospital) {
            System.out.println(patient);
        }
        System.out.println();

        hospital.addPatient(p1);
        System.out.println("iterating with one patient");
        for (var patient : hospital) {
            System.out.println(patient);
        }
        System.out.println();

        hospital.addPatient(p2);
        hospital.addPatient(p3);
        hospital.addPatient(p4);
        hospital.addPatient(p5);
        hospital.addPatient(p6);

        System.out.println("iterating over 6 patients");
        for (var patient : hospital) {
            System.out.println(patient);
        }
        System.out.println();

        System.out.println(hospital.appointments.head.previous);
        System.out.println("head = " + hospital.appointments.head.getPatient());
        System.out.println(hospital.appointments.head.next.getPatient());
        System.out.println(hospital.appointments.head.next.next.getPatient());
        System.out.println(hospital.appointments.head.next.next.next.getPatient());
        System.out.println(hospital.appointments.head.next.next.next.next.getPatient());
        System.out.println(hospital.appointments.head.next.next.next.next.next.getPatient());
        System.out.println(hospital.appointments.head.next.next.next.next.next.next);
        System.out.println();
        System.out.println("Num patients = " + hospital.appointments.size);
        System.out.println("Printing list in reverse order");
        System.out.println(hospital.appointments.head.next.next.next.next.next.next);
        System.out.println(hospital.appointments.head.next.next.next.next.next.getPatient());
        System.out.println(hospital.appointments.head.next.next.next.next.next.previous.getPatient());
        System.out.println(hospital.appointments.head.next.next.next.next.next.previous.previous.getPatient());
        System.out.println(hospital.appointments.head.next.next.next.next.next.previous.previous.previous.getPatient());
        System.out.println(hospital.appointments.head.next.next.next.next.next.previous.previous.previous.previous.getPatient());
        System.out.println(hospital.appointments.head.next.next.next.next.next.previous.previous.previous.previous.previous.getPatient());
        System.out.println(hospital.appointments.head.next.next.next.next.next.previous.previous.previous.previous.previous.previous);
        System.out.println();

        hospital.addPatient(new Patient("Sam", "08:20"));
        hospital.addPatient(new Patient("Matt", "08:19"));

//        System.out.println(hospital.appointments.head.previous);
//        System.out.println("head = " + hospital.appointments.head.getPatient());
//        System.out.println(hospital.appointments.head.next.getPatient());
//        System.out.println(hospital.appointments.head.next.next.getPatient());
//        System.out.println(hospital.appointments.head.next.next.next.getPatient());
//        System.out.println(hospital.appointments.head.next.next.next.next.getPatient());
//        System.out.println(hospital.appointments.head.next.next.next.next.next.getPatient());
//        System.out.println(hospital.appointments.head.next.next.next.next.next.next.getPatient());
//        System.out.println(hospital.appointments.head.next.next.next.next.next.next.next);

        System.out.println("Iterating over 8 patients");
        System.out.println("SIZE OF APPTS = " + hospital.appointments.size);
        for (var patient : hospital) {
            System.out.println(patient);
        }
        System.out.println();

        // add some patients at same time as existing
        hospital.addPatient(new Patient("Harry", "08:19"));
        hospital.addPatient(new Patient("Matt1", "08:19"));
        System.out.println(hospital.addPatient(new Patient("Eugene", "13:15")));
        System.out.println(hospital.addPatient(new Patient("Henry", "08:00")));

        for (var patient : hospital) {
            System.out.println(patient);
        }
        System.out.println();

        // add some patients at invalid times
        System.out.println(hospital.addPatient(new Patient("Hackerman", "07:59")));
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

class Node {
    PatientBase patient;

    Node previous;

    Node next;

    public Node(PatientBase patient) {
        this.patient = patient;
    }

    public PatientBase getPatient() { return patient; }
}

class DoublyLinkedList {
    Node head;

    Node tail;

    int size;

    public void doublyLikedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public void addNode(Node node) {
        if (this.size == 0) {
            this.head = node;
            this.tail = node;
        } else {
            this.tail.next = node;
            Node oldTail = this.tail;
            this.tail = node;
            node.previous = oldTail;
        }
        this.size++;
    }

    public DoublyLinkedList sliceList(Node startNode, Node endNode, int size) {
        DoublyLinkedList list = new DoublyLinkedList();
        list.head = startNode;
        list.tail = endNode;
        list.head.previous = null;
        list.tail.next = null;
        list.size = size;
        return list;
    }

    public void removeHead() {
        this.head = this.head.next;
        this.head.previous = null;
        this.size--;
    }
}
