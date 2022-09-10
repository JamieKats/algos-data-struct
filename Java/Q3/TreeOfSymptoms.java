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

	/** Restructure by tri node restructuring
	 * 1. FInd the node with severity >= the severity given
	 * 2. use tri node restructuing to move this node to the top??*/
	@Override
	public void restructureTree(int severity) {
		/* Add your code here! */
		// in order traversal to get all nodes in tree
		ArrayList<SymptomBase> symptoms = inOrderTraversal();

		// sort arraylist
		Collections.sort(symptoms);

		// binary search on array list to find severity level
		int newRoot = binarySearch(symptoms, 0, symptoms.size() - 1, severity);

		for (var i : symptoms) {
			System.out.println(i);
		}
		System.out.println("new root at " + newRoot);
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
		System.out.println("new root set at " + rootIndex + " " + symptoms.get(rootIndex));
//		System.out.println(symptoms.size() - 1);
//		System.out.println(this.getRoot());
		BSTCreatorRecurse(symptoms, 0, rootIndex, symptoms.size() - 1);
	}

	public void BSTCreatorRecurse(ArrayList<SymptomBase> symptoms, int left, int middle,
								  int right) {
//		System.out.println("left: " + left + " middle " + middle + " right " + right);
		if (left >= middle && middle >= right) { // base case
			symptoms.get(middle).setLeft(null);
			symptoms.get(middle).setRight(null);
//			System.out.println("BASE CASE HIT");
			return;
		}

		if (left != middle) {
			if (right - left == 1) {
				symptoms.get(middle).setLeft(symptoms.get(left));
				symptoms.get(middle).setRight(null);
				BSTCreatorRecurse(symptoms, left, left, left);
			}

			// get index of left child
			int leftChild = left + (middle - left) / 2;
			symptoms.get(middle).setLeft(symptoms.get(leftChild));
			BSTCreatorRecurse(symptoms, left, leftChild, (middle - 1));
		}
		if (middle != right) {
			if (right - left == 1) {
				symptoms.get(middle).setRight(symptoms.get(right));
				symptoms.get(middle).setLeft(null);
				BSTCreatorRecurse(symptoms, right, right, right);
			}

			// get index of right child
			int rightChild = middle + (int)Math.ceil(((double)right - (double)middle) / 2);
			symptoms.get(middle).setRight(symptoms.get(rightChild));
//			System.out.println("middle " + (middle + 1));
//			System.out.println("rightChild " + rightChild);
//			System.out.println("right " + right);
			BSTCreatorRecurse(symptoms, (middle + 1), rightChild, right);
		}
	}

	public static void main(String[] args) {
		/*
		 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		 * REMOVE THE MAIN METHOD BEFORE SUBMITTING TO THE AUTOGRADER
		 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		 * The following main method is provided for simple debugging only
		 */
		var cough = new Symptom("Cough", 3);
		var fever = new Symptom("Fever", 6);
		var redEyes = new Symptom("Red Eyes", 1);

		redEyes.setLeft(cough);
		redEyes.setRight(fever);

		// coughs children
		var sneeze = new Symptom("Sneeze", 7);
		var red = new Symptom("red", 10);
		cough.setLeft(sneeze);
		cough.setRight(red);

		var tree = new TreeOfSymptoms(redEyes);
		var inOrderTraversal = tree.inOrderTraversal();
//		var correctTraversal = new Symptom[] { cough, redEyes, fever };
		var correctTraversal = new Symptom[] { sneeze, cough, red, redEyes, fever };
		int i = 0;
		for (var patient : inOrderTraversal) {
			assert Objects.equals(patient, correctTraversal[i++]);
		}
		assert tree.getRoot() == redEyes;

		// test binary search
//		ArrayList<SymptomBase> orderedTrversal = new ArrayList<>() {
//			{
//				add(redEyes);
//				add(cough);
//				add(fever);
//				add(sneeze);
////				add(red);
//			}
//		};
////		for (var symptom : orderedTrversal) {
////			System.out.println(symptom);
////		}
//		System.out.println("index of = " + tree.binarySearch(orderedTrversal, 0,
//				orderedTrversal.size() - 1, 0));
//
//		// test post order traversal
//		var postOrderTraversal = tree.postOrderTraversal();
//		correctTraversal = new Symptom[] { sneeze, red, cough, fever, redEyes };
//		i = 0;
//		for (var patient : postOrderTraversal) {
//			assert Objects.equals(patient, correctTraversal[i++]);
//		}

		tree.restructureTree(6);
//		System.out.println(tree.getRoot());
//		System.out.println(tree.getRoot().getLeft());
//		System.out.println(tree.getRoot().getRight());
//		System.out.println(tree.getRoot().getRight().getLeft());
//		System.out.println(tree.getRoot().getRight().getRight());

		inOrderTraversal = tree.inOrderTraversal();
		correctTraversal = new Symptom[] { redEyes, cough, fever};
		correctTraversal = new Symptom[] { redEyes, cough, fever, red, sneeze };
		i = 0;
		for (var patient : inOrderTraversal) {
//			assert Objects.equals(patient, correctTraversal[i++]);
			System.out.println(patient);
		}
		assert tree.getRoot() == fever;
		System.out.println("TOP OF TREE");
		System.out.println(tree.getRoot());
		System.out.println(tree.getRoot().getLeft());
		System.out.println(tree.getRoot().getLeft().getLeft());
		System.out.println(tree.getRoot().getLeft().getLeft().getLeft());
		System.out.println(tree.getRoot().getLeft().getLeft().getRight());
		System.out.println(tree.getRoot().getLeft().getRight());
		System.out.println(tree.getRoot().getRight());
		System.out.println(tree.getRoot().getRight().getLeft());
		System.out.println(tree.getRoot().getRight().getRight());
		System.out.println(tree.getRoot().getRight().getRight().getLeft());
		System.out.println(tree.getRoot().getRight().getRight().getRight());
	}
}
