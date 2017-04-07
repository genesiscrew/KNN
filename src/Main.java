import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Timer;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Main {

	static ArrayList<ArrayList<String>> testArray1 = new ArrayList<ArrayList<String>>();
	static ArrayList<ArrayList<String>> trainArray1 = new ArrayList<ArrayList<String>>();
	static ArrayList<ArrayList<String>> neighbours;
	static int k = 1;

	public static void main(String[] args) throws FileNotFoundException, IOException {


		loadData(args[0], args[1]);
		if (args.length > 2) {
			k = Integer.parseInt(args[2]);
		}


		ArrayList<String> predictions = new ArrayList<String>();
		for (ArrayList<String> testSample : testArray1) {

			neighbours = getNeighbors(testSample, k);
			String result = getVote(neighbours);
			predictions.add(result);
			System.out.println("prediction is: " + result + ". actual is: " + testSample.get(4));

		}

		 System.out.println("accuracy of algorithm is " + 100*checkAccuracy(predictions, testArray1) + "%");

	}
	/**
	 * simply returns a percentage of accurate predictions
	 * @param predictions
	 * @param testArray1
	 * @return
	 */


	private static Double checkAccuracy(ArrayList<String> predictions, ArrayList<ArrayList<String>> testArray1) {
		Double count = 0.0;
		for (int i = 0; i < predictions.size(); i++) {
			if (predictions.get(i).equals(testArray1.get(i).get(4))) {
				count++;
			}

		}
		Double accuracy = count / ((double) predictions.size());

		return accuracy;

	}
	/**
	 * returns the majority class within neighbnours
	 * @param neighbours
	 * @return
	 */

	private static String getVote(ArrayList<ArrayList<String>> neighbours) {
		ArrayList<String> votes = new ArrayList<String>();
		for (ArrayList<String> neighbour : neighbours) {
			votes.add(neighbour.get(4));
		}

		Map<String, Long> occurrences = votes.stream().collect(Collectors.groupingBy(w -> w, Collectors.counting()));

		String vote = occurrences.entrySet().stream()
				.max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();

		return vote;
	}
	/**
	 * gets the closests neighbours to the test sample
	 * @param testSample
	 * @param k
	 * @return
	 */

	private static ArrayList<ArrayList<String>> getNeighbors(ArrayList<String> testSample, int k) {
		Map<Double, ArrayList<String>> distances = new TreeMap<Double, ArrayList<String>>();

		for (ArrayList<String> trainSample : trainArray1) {
			Double dist = euclideanDistance(testSample, trainSample, testSample.size() - 1);
			distances.put(dist, trainSample);

		}

		ArrayList<ArrayList<String>> neighbours = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < k; i++) {

			neighbours.add((ArrayList<String>) distances.values().toArray()[i]);

		}
		return neighbours;
	}
/**
 * calculates the euclidean distance of samples
 * @param testSample
 * @param trainSample
 * @param i
 * @return
 */
	private static Double euclideanDistance(ArrayList<String> testSample, ArrayList<String> trainSample, int i) {
		Double distance = 0.0;
		for (int u = 0; u < i; u++) {
			Double test = Double.parseDouble(testSample.get(u));
			Double train = Double.parseDouble(trainSample.get(u));
			distance += Math.pow(test - train, 2);

		}
		return Math.sqrt(distance);
	}

	private static void loadData(String first, String second) throws FileNotFoundException {
		// TODO Auto-generated method stub

		File file1 = new File(second);
		File file2 = new File(first);
		Scanner input1 = new Scanner(file1);
		Scanner input2 = new Scanner(file2);

		while (input1.hasNext()) {
			ArrayList<String> testArray = new ArrayList<String>();
			String one = input1.next();
			String two = input1.next();
			String three = input1.next();
			String four = input1.next();
			String label = input1.nextLine();

			testArray.add(one);
			testArray.add(two);
			testArray.add(three);
			testArray.add(four);
			testArray.add(label);
			testArray1.add(testArray);

		}
		input1.close();

		while (input2.hasNext()) {
			ArrayList<String> trainArray = new ArrayList<String>();
			String one = input2.next();
			String two = input2.next();
			String three = input2.next();
			String four = input2.next();
			String label = input2.nextLine();

			trainArray.add(one);
			trainArray.add(two);
			trainArray.add(three);
			trainArray.add(four);
			trainArray.add(label);
			trainArray1.add(trainArray);

		}
		input2.close();
	}

}
