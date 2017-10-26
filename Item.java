public class Item implements Comparable {

	int profit;
	int weight;

	public Item(int p, int w) {
		profit= p;
		weight= w;
	}
	
	public boolean equals(Object other) {
		Item o= (Item) other;
		if (profit == o.profit && weight == o.weight)
			return true;
		else
			return false;
	}

	public int compareTo(Object other) {
		Item o= (Item) other;
		double ratio= (double) profit/weight;
		double otherRatio= (double) o.profit/o.weight;

		// double ratio= (double) weight;
		// double otherRatio= (double) o.weight;


		if (ratio > otherRatio) // Descending sort
			return -1;
		else if (ratio < otherRatio)
			return 1;
		else
			return 0;
	} 

	public String toString(){
		return "Profit ="+profit+" Weight ="+weight;
	}

	public String toSmallString(){
		return "("+profit+","+weight+")";
	}
} 