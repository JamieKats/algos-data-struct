import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

class Symptom extends SymptomBase {

	public Symptom(String symptom, int severity) {
		super(symptom, severity);
	}

	@Override
	public int compareTo(SymptomBase o) {
		/* Add your code here! */
		return this.getSeverity() - o.getSeverity();
	}
}

public class TreeOfSymptoms extends TreeOfSymptomsBase {

	public TreeOfSymptoms(SymptomBase root) {
		super(root);
	}

	@Override
	public ArrayList<SymptomBase> inOrderTraversal() {
		/* Add your code here! */
		ArrayList<SymptomBase> symptoms = new ArrayList<>();
		inOrderTraversalRecurse(this.getRoot(), symptoms);
		return symptoms;
	}

	public void inOrderTraversalRecurse(SymptomBase symptom, ArrayList<SymptomBase> symptoms) {
		if (symptom.getLeft() != null) {
			inOrderTraversalRecurse(symptom.getLeft(), symptoms);
		}
		symptoms.add(symptom);
		if (symptom.getRight() != null) {
			inOrderTraversalRecurse(symptom.getRight(), symptoms);
		}
	}

	@Override
	public ArrayList<SymptomBase> postOrderTraversal() {
		/* Add your code here! */
		ArrayList<SymptomBase> symptoms = new ArrayList<>();
		postOrderTraversalRecurse(this.getRoot(), symptoms);
		return symptoms;
	}

	public void postOrderTraversalRecurse(SymptomBase symptom, ArrayList<SymptomBase> symptoms) {
		if (symptom.getLeft() != null) {
			postOrderTraversalRecurse(symptom.getLeft(), symptoms);
		}
		if (symptom.getRight() != null) {
			postOrderTraversalRecurse(symptom.getRight(), symptoms);
		}
		symptoms.add(symptom);
	}

	@Override
	public void restructureTree(int severity) {
		/* Add your code here! */
		// in order traversal to get all nodes in given tree
		ArrayList<SymptomBase> symptoms = inOrderTraversal();

		// sort arraylist
		Collections.sort(symptoms);

		// binary search on array list to find severity level
		int newRoot = binarySearch(symptoms, 0, symptoms.size() - 1, severity);

		// construct new tree
		BSTCreator(symptoms, newRoot);
	}

	/* Add any extra functions below */
	/** Returns the index of the symptom that has the smallest severity that satisfies severity
	 * >= threshold */
	public int binarySearch(ArrayList<SymptomBase> symptoms, int left, int right, int severity) {
		// basecase
		if (left > right) { // didnt find value
			if (left == symptoms.size()) {
				return left - 1;
			}
			return left;
		}
		int middle = left + (right - left) / 2;

		if (symptoms.get(middle).getSeverity() == severity) { // found value
			return middle;
		}
		if (symptoms.get(middle).getSeverity() < severity) { // search left
			return binarySearch(symptoms, middle + 1, right, severity);
		}
		return binarySearch(symptoms, left, middle - 1, severity);
	}

	public void BSTCreator(ArrayList<SymptomBase> symptoms, int rootIndex) {
		// set root
		this.setRoot(symptoms.get(rootIndex));
		this.getRoot().setLeft(null);
		this.getRoot().setRight(null);
		BSTCreatorRecurse(symptoms, 0, rootIndex, symptoms.size() - 1);
	}

	public void BSTCreatorRecurse(ArrayList<SymptomBase> symptoms, int left, int middle,
								  int right) {
		if (left >= middle && middle >= right) { // base case
			symptoms.get(middle).setLeft(null);
			symptoms.get(middle).setRight(null);
			return;
		}

		if (left != middle) {
			if (right - left == 1) { // only two nodes remaining
				symptoms.get(middle).setLeft(symptoms.get(left));
				symptoms.get(middle).setRight(null);
				BSTCreatorRecurse(symptoms, left, left, left);
			}

			// get index of left child
			int leftChild = left + (middle - left) / 2;

			// set the left child of current middle node and recursively construct tree for left
			// child
			symptoms.get(middle).setLeft(symptoms.get(leftChild));
			BSTCreatorRecurse(symptoms, left, leftChild, (middle - 1));
		}
		if (middle != right) {
			if (right - left == 1) { // only two nodes remaining
				symptoms.get(middle).setRight(symptoms.get(right));
				symptoms.get(middle).setLeft(null);
				BSTCreatorRecurse(symptoms, right, right, right);
			}

			// get index of right child
			int rightChild = middle + (int)Math.ceil(((double)right - (double)middle) / 2);

			// set the right child of current middle node and recursively construct tree for right
			// child
			symptoms.get(middle).setRight(symptoms.get(rightChild));
			BSTCreatorRecurse(symptoms, (middle + 1), rightChild, right);
		}
	}
}
