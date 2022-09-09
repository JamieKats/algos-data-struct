import java.util.ArrayList;
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
	}

	/* Add any extra functions below */

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
		var sneeze = new Symptom("Sneese", 1);
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

		// test post order traversal
		var postOrderTraversal = tree.postOrderTraversal();
		correctTraversal = new Symptom[] { sneeze, red, cough, fever, redEyes };
		i = 0;
		for (var patient : postOrderTraversal) {
			assert Objects.equals(patient, correctTraversal[i++]);
		}

		tree.restructureTree(2);
		inOrderTraversal = tree.inOrderTraversal();
		correctTraversal = new Symptom[] { redEyes, cough, fever};
		i = 0;
		for (var patient : inOrderTraversal) {
			assert Objects.equals(patient, correctTraversal[i++]);
		}
		assert tree.getRoot() == cough;
	}
}
