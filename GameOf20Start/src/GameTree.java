import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * A model for the game of 20 questions
 * 
 * @author Rick Mercer
 */
public class GameTree {
	private class TreeNode {
		private String data;
		private TreeNode left;
		private TreeNode right;

		public TreeNode(String theData) {
			data = theData;
			left = null;
			right = null;
		}

		public TreeNode(String dataRef, TreeNode leftTree, TreeNode rightTree) {
			data = dataRef;
			left = leftTree;
			right = rightTree;
		}
	}

	private TreeNode root;
	private TreeNode currentNode;
	private Scanner inFile;
	private String currentFileName;

	public GameTree(String name) {
		currentFileName = name;
		try {
			inFile = new Scanner(new File(currentFileName));
		} catch (FileNotFoundException e) {
			// Throw the exception if the file is not found
			System.out.println("This file---" + currentFileName
					+ "is not exist!");
		}
		root = build();
		currentNode = root;
		inFile.close();
	}

	private TreeNode build() {
		String line = null;
		TreeNode Tree = null;
		TreeNode L;
		TreeNode R;
		line = inFile.nextLine().trim();
		if (line.indexOf("?") >= 0) {
			L = build();
			R = build();
			Tree = new TreeNode(line, L, R);
		} else {
			Tree = new TreeNode(line);
		}
		return Tree;
	}

	/*
	 * Add a new question and answer to the currentNode. If the current node has
	 * the answer chicken, theGame.add("Does it swim?", "goose"); should change
	 * that node like this:
	 */
	// -----------Feathers?-----------------Feathers?------
	// -------------/----\------------------/-------\------
	// ------- chicken horse-----Does it swim?-----horse--
	// -----------------------------/------\---------------
	// --------------------------goose--chicken-----------
	/**
	 * @param newQuestion
	 *            The question to add where the old answer was.
	 * @param newAnswer
	 *            The new Yes answer for the new question.
	 */
	private TreeNode pre;
	String fileName;
	
	

	public void add(String newQuestion, String newAnswer) {
		TreeNode l = new TreeNode(newAnswer);
		TreeNode r = new TreeNode(this.getCurrent());
		TreeNode newTreeNode = new TreeNode(newQuestion, l, r);
		currentNode = newTreeNode;
		pre.left = currentNode;
	}

	/**
	 * True if getCurrent() returns an answer rather than a question.
	 * 
	 * @return False if the current node is an internal node rather than an
	 *         answer at a leaf.
	 */
	public boolean foundAnswer() {
		boolean result = true;
		if (this.getCurrent().indexOf("?") < 0)
			return result;
		return false;
	}

	/**
	 * Return the data for the current node, which could be a question or an
	 * answer.
	 * 
	 * @return The current question or answer.
	 */
	public String getCurrent() {

		return currentNode.data;
	}

	/**
	 * Ask the game to update the current node by going left for Choice.yes or
	 * right for Choice.no Example code: theGame.playerSelected(Choice.Yes);
	 * 
	 * @param yesOrNo
	 */
	public void playerSelected(Choice yesOrNo) {
		pre = currentNode;
		if (yesOrNo == Choice.Yes) {

			currentNode = currentNode.left;
		} else {
			currentNode = currentNode.right;
		}
	}

	/**
	 * Begin a game at the root of the tree. getCurrent should return the
	 * question at the root of this GameTree.
	 */
	public void reStart() {
		pre = null;
		currentNode = root;
	}

	/**
	 * Return a textual version of this object
	 */
	@Override
	public String toString() {

		return toString(0, root);
	}

	private String toString(int i, TreeNode root2) {
		String result = "";
		if (root2.right != null)
			result += toString(i + 1, root2.right);

		for (int j = 0; j < i; j++)
			result += "- ";

		result += root2.data + "\n";

		if (root2.left != null)
			result += toString(i + 1, root2.left);
		return result;
	}

	/**
	 * Overwrite the old file for this gameTree with the current state that may
	 * have new questions added since the game started.
	 * 
	 */
	public void saveGame() {
		String newTestFile = currentFileName;
		FileWriter a = null;
		try {
			a = new FileWriter(newTestFile);
		} catch (IOException e) {
			System.out.println("This" + newTestFile + "doesn't exist!" + e);
		}

		PrintWriter b = new PrintWriter(a);

		SaveGames(root, b);

		b.close();
	}

	private void SaveGames(TreeNode root2, PrintWriter s) {
		if (root2 == null) {
			return;
		} else {
			s.println(root2.data);
			SaveGames(root2.left, s);
			SaveGames(root2.right, s);
		}

	}
}