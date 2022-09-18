import java.util.Iterator;
import java.util.NoSuchElementException;

public class Hospital3 extends HospitalBase {

//    private Node head;
//
//    private Node tail;

//    private int numAppointments;

    DoublyLinkedList appointments;

    int appointmentLength = -1; // -1 means appointments can be any length

    private String startTime = "08:00";
    private String endTime = "17:59";
    private String breakStart = "11:59";
    private String breakEnd = "13:00";

    public Hospital3() {
        /* Add your code here! */
//        this.head = null;
//        this.tail = null;
        this.appointments = new DoublyLinkedList();
//        this.numAppointments = appointments.size;
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

        this.appointments.addNode(newPatientNode);

//        if (this.appointments.size == 0) { // adding first patientNode
//            this.appointments.head = newPatientNode;
//            this.appointments.tail = newPatientNode;
////            this.head = newPatientNode;
////            this.tail = newPatientNode;
//        } else {
////            this.tail.setNext(newPatientNode);
//            this.appointments.tail.next = newPatientNode;
//            Node oldTail = this.appointments.tail;
//            this.appointments.tail = newPatientNode;
////            newPatientNode.setPrevious(oldTail);
//            newPatientNode.previous = oldTail;
//        }
//        this.appointments.size++;


        return true;
    }

//    /** Swap the patients at index i and p */
//    public void swapPatients(int i, int p, PatientBase[] appointments) {
//        PatientBase tmp = appointments[i];
//        appointments[i] = appointments[p];
//        appointments[p] = tmp;
//    }

    public DoublyLinkedList sort(Node head, Node tail, int numNodes) {
        DoublyLinkedList list1 = new DoublyLinkedList();
        list1.head = head;
        list1.tail = tail;
        list1.size = numNodes;
        DoublyLinkedList list = mergeSort(list1);
//        node.previous = null;
        this.appointments = list;
        return list;
    }

//    public void splitList() {
//
//    }

    /** All values between and including leftIndex and rightIndex are used for sorting */
    public DoublyLinkedList mergeSort(DoublyLinkedList list) {
//        System.out.println("merge sort hit");
//        System.out.println("numnodes = " + numNodes);
//        System.out.println(head.next);
        if (list.size == 1) {
//            System.out.println(head.getPatient());
            DoublyLinkedList singleList = new DoublyLinkedList();
            singleList.head = list.head;
            singleList.tail = list.tail;
            return singleList;
//            return;
        }


        Node middleNode = getMiddleNode(list.head, list.tail, list.size);
        int middleNodeNumber = getMiddleNodeNumber(list.size);


//        System.out.println("middle node num calc = " + middleNodeNumber);



        // split linked list by making head and tail of both halves point to null
//        System.out.println("leftlist head = " + head.getPatient());
//        System.out.println("leftlist tail = " + middleNode.getPatient());
//        System.out.println("leftlist size = " + (numNodes - middleNodeNumber));
//        System.out.println("rightlist head = " + middleNode.next.getPatient());
//        System.out.println("rightlist tail = " + tail.getPatient());
//        System.out.println("rightlist size = " + middleNodeNumber);

//        System.out.println("NUM OF NODES = " + numNodes);
//        System.out.println("MID NODE NUMBER = " + middleNodeNumber);
//        System.out.println("Start node = " + head.getPatient());
        Node leftListHead = list.head;
        Node leftListTail = middleNode;
        Node rightListHead = middleNode.next;
        Node rightListTail = list.tail;

//        System.out.println("/////// LEFT ///////");
//        printList(leftListHead);
//        System.out.println("/////// RIGHT ///////");
//        printList(rightListHead);

//        System.out.println(list.size);
//        leftListHead.previous = null;
//        leftListTail.next = null;
////        System.out.println("rightlist head = " + rightListHead);
//        rightListHead.previous = null;
//        rightListTail.next = null;


        DoublyLinkedList rightList = this.appointments.sliceList(middleNode.next, list.tail,
                list.size - middleNodeNumber);
        DoublyLinkedList leftList = this.appointments.sliceList(list.head, middleNode,
                middleNodeNumber);


//        System.out.println("/////// LEFT    split ///////");
//        printList(leftListHead);
//        System.out.println("/////// RIGHT   split ///////");
//        printList(rightListHead);
//        System.out.println("//////////////");

//        DoublyLinkedList leftList = new DoublyLinkedList();
//        leftList.size = middleNodeNumber;
//        leftList.head = leftListHead;
//        leftList.tail = leftListTail;

        DoublyLinkedList leftSorted = mergeSort(leftList);
//        System.out.println("MIDDLE NODE NUMBER SENT TO MERGESORT = " + middleNodeNumber);
//        mergeSort(leftListHead, leftListTail, middleNodeNumber);

//        DoublyLinkedList rightList = new DoublyLinkedList();
//        rightList.size = list.size - middleNodeNumber;
//        rightList.head = rightListHead;
//        rightList.tail = rightListTail;

        DoublyLinkedList rightSorted = mergeSort(rightList);
//        mergeSort(rightListHead, rightListTail, numNodes - middleNodeNumber);
        return merge(leftSorted, rightSorted);
    }

    /** left, middle and right index are the indexes of the values that need to be swapped */
    public DoublyLinkedList merge(DoublyLinkedList leftList, DoublyLinkedList rightList) {
        Node leftNode = leftList.head;
        Node rightNode = rightList.head;

        DoublyLinkedList mergedList = new DoublyLinkedList();
        // setting up the initial node in the list
//        Node initialNode = new Node(null);
//        mergedList.head = initialNode;
//        mergedList.tail = initialNode;
//        mergedList.size++;

        while (leftNode != null && rightNode != null) {
            if (leftNode.getPatient().compareTo(rightNode.getPatient()) <= 0) {
//                mergedList.tail.next = leftNode;
//                Node oldTail = mergedList.tail;
//                mergedList.tail = leftNode;
//                leftNode.previous = oldTail;
                mergedList.addNode(leftNode);

                leftNode = leftNode.next;
            } else {
//                mergedList.tail.next = rightNode;
//                Node oldTail = mergedList.tail;
//                mergedList.tail = rightNode;
//                rightNode.previous = oldTail;
                mergedList.addNode(rightNode);

                rightNode = rightNode.next;
            }
        }
        while (leftNode != null) { // Copy rest of left array
//            mergedList.tail.next = leftNode;
//            Node oldTail = mergedList.tail;
//            mergedList.tail = leftNode;
//            leftNode.previous = oldTail;
            mergedList.addNode(leftNode);

            leftNode = leftNode.next;
        }
        while (rightNode != null) { // Copy rest of right array
//            mergedList.tail.next = rightNode;
//            Node oldTail = mergedList.tail;
//            mergedList.tail = rightNode;
//            rightNode.previous = oldTail;
            mergedList.addNode(rightNode);

            rightNode = rightNode.next;
        }

//        mergedList.head = mergedList.head.next;
//        mergedList.head.previous = null;
//        mergedList.removeHead(); // clean dummy head off list

        return mergedList;
    }

    public void printList(Node node) {
        while (node != null) {
            System.out.println(node.getPatient());
            node = node.next;
        }
    }

    public Node getMiddleNode(Node head, Node tail, int numNodes) {
//        System.out.println("in get middle node");
//        System.out.println(head.getPatient());
//        System.out.println(numNodes);
        if (numNodes == 1) {
            return head;
        }
        // calculate the middle node
        int middleNum = getMiddleNodeNumber(numNodes);
//        if (numNodes % 2 == 0) {
//            middleNum = (numNodes - 1) / 2;
//        } else {
//            middleNum = numNodes / 2;
//        }


        Node middleNode = head;
        for (int i = 1; i < middleNum; i++) {
            middleNode = middleNode.getNext();
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


    /** TODO see if sorting can be only done when the array is out of order */
    @Override
    public Iterator<PatientBase> iterator() {
        /* Add your code here! */
        return new Iterator<PatientBase>() {

            boolean isSorted = false;

//            Node currentNode = getHead();
            DoublyLinkedList list = sort(appointments.head, appointments.tail, appointments.size);
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
                    throw new NoSuchElementException();
                }
                Node nextNode = this.currentNode;
                this.currentNode = this.currentNode.getNext();
                return nextNode.getPatient();
            }
        };
    }

    /* Add any extra functions below */

//    public Node getHead() { return this.head; }
//
//    public Node getTail() { return this.tail; }

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

        hospital.addPatient(p1);
        hospital.addPatient(p2);
        hospital.addPatient(p3);
        hospital.addPatient(p4);
        hospital.addPatient(p5);
        hospital.addPatient(p6);
//        Node newHead = hospital.mergeSort(hospital.head, hospital.tail, hospital.numAppointments);
//        Node newHead = hospital.sort(hospital.head, hospital.tail, hospital.numAppointments);
//        System.out.println();
//        hospital.printList(newHead);
////        hospital.printList(newHead.previous);
//        System.out.println(newHead.previous);
//        System.out.println();


//        hospital.addPatient(p7);

//        var hospital1 = new Hospital3();
//        hospital1.addPatient(p4);
//        hospital1.addPatient(p5);
//        hospital1.addPatient(p6);
//        System.out.println();
//        hospital1.printList(hospital.merge(hospital.head, hospital1.head));
//        System.out.println();
//        hospital.printList(hospital.head);
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
//        System.out.println("middle patient = " + hospital.getMiddleNode(hospital.getHead(),
//                hospital.getTail(), hospital.numAppointments).getPatient());
        System.out.println();
        System.out.println();
        for (var patient : hospital) {
            System.out.println('/');
            System.out.println(patient);
////            System.out.println();
//////            assert Objects.equals(patient, patients[i++]);
        }
        System.out.println("Linked list printed above...");
//        System.out.println(hospital.head.previous.previous.previous.previous);
//        System.out.println(hospital.head.previous.previous.previous);
//        System.out.println(hospital.head.previous.previous);
        System.out.println(hospital.appointments.head.previous);
        System.out.println("head = " + hospital.appointments.head.getPatient());
        System.out.println(hospital.appointments.head.next.getPatient());
        System.out.println(hospital.appointments.head.next.next.getPatient());
        System.out.println(hospital.appointments.head.next.next.next.getPatient());
        System.out.println(hospital.appointments.head.next.next.next.next.getPatient());
        System.out.println(hospital.appointments.head.next.next.next.next.next.getPatient());
        System.out.println(hospital.appointments.head.next.next.next.next.next.next);
        System.out.println("Num patients = " + hospital.appointments.size);
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
//        hospital.addPatient(new Patient("Emily", "08:19"));
        System.out.println(hospital.appointments.head.previous);
        System.out.println("head = " + hospital.appointments.head.getPatient());
        System.out.println(hospital.appointments.head.next.getPatient());
        System.out.println(hospital.appointments.head.next.next.getPatient());
        System.out.println(hospital.appointments.head.next.next.next.getPatient());
        System.out.println(hospital.appointments.head.next.next.next.next.getPatient());
        System.out.println(hospital.appointments.head.next.next.next.next.next.getPatient());
        System.out.println(hospital.appointments.head.next.next.next.next.next.next.getPatient());
        System.out.println(hospital.appointments.head.next.next.next.next.next.next.next);

        System.out.println("Printing list after adding patient");
        System.out.println("SIZE OF APPTS = " + hospital.appointments.size);
        for (var patient : hospital) {
            System.out.println(patient);
        }
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

//    public void addToTail(Node node) {
//        this.tail.next = node;
//        Node oldTail = this.tail;
//        this.tail = node;
//        node.previous = oldTail;
//        this.size++;
//    }

    public void removeHead() {
        this.head = this.head.next;
        this.head.previous = null;
        this.size--;
    }
}
