import java.util.Iterator;

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
            DoublyLinkedList list = sort(appointments);

            Node currentNode = list.head;

            @Override
            public boolean hasNext() {
                if (this.currentNode != null) {
                    return true;
                }
                return false;
            }

            @Override
            public PatientBase next() {
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
}
