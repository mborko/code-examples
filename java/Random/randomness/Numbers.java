package randomness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public class Numbers extends Thread {

	private HashMap<TreeSet<Integer>, Integer> tips;
	private HashMap<Integer, ArrayList<TreeSet<Integer>>> analyze;
	private long counter;
	private long adds;
	private int numbers = 6;
	private int ceiling = 45;

	public Numbers(int numbers, int ceiling, long adds) {
		super();

		this.tips = new HashMap<TreeSet<Integer>, Integer>();
		this.analyze = new HashMap<Integer, ArrayList<TreeSet<Integer>>>();
		this.counter = 0;
		this.numbers = numbers;
		this.ceiling = ceiling;
		this.adds = adds;
	}

	private TreeSet<Integer> newTip() {
		TreeSet<Integer> ret = new TreeSet<Integer>();

		// This should be optimized!
		do {
			ret.add(((int) (Math.random() * Integer.MAX_VALUE) % ceiling) + 1);
		} while (ret.size() < numbers);

		return ret;
	}

	public void fillMap(long adds) {

		for (int i = 0; i < adds; i++) {
			TreeSet<Integer> tip = newTip();
			Integer index = 0;
			if (tips.containsKey(tip)) {
				index = tips.get(tip) + 1;
				tips.put(tip, index);
			} else {
				index = 1;
				tips.put(tip, index);
			}
			analyzeMap(index, tip);
			counter++;
		}
	}

	public void analyzeMap(Integer newIndex, TreeSet<Integer> tip) {
		if (newIndex >= 1 && tip != null) {
			if (analyze.containsKey(newIndex)) {
				analyze.get(newIndex).add(tip);
			} else {
				ArrayList<TreeSet<Integer>> temp = new ArrayList<TreeSet<Integer>>();
				temp.add(tip);
				analyze.put(newIndex, temp);
			}
			if (newIndex > 1)
				analyze.get(newIndex - 1).remove(tip);
		}
	}

	public void printRareOccurances(int min, int max, boolean verbose) {
		for (int i = min; i <= max; i++) {
			if (analyze.containsKey(i)) {
				ArrayList<TreeSet<Integer>> temp = analyze.get(i);

				System.out.println("#" + i + " occurances with " + temp.size() + " tips");
				if (verbose || (temp.size() <= 12)) {
					for (TreeSet<Integer> t : temp)
						System.out.print(t.toString() + " ");
					System.out.println();
				}
			}
		}
	}

	public void run() {
		fillMap(this.adds);
	}

	public void stopThread() {
		this.interrupt();
		System.out.println("There are now " + this.counter + " tips in the map, but with only "
				+ this.tips.size() + " unique tips!");
		this.printRareOccurances(0, 1000, false);
	}
}
