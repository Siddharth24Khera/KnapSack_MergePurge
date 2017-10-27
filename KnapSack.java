import java.util.ArrayList;
import java.util.Arrays;

public class KnapSack {
	
	private Item[] inputItems;
	private int knapsackCapacity; 
	private Set[] sets; // Subsequences, sets
	private boolean[] solution; //Solution with optimal items only

	public KnapSack(Item[] inputArray, int maxCap) {
		inputItems= inputArray;
		knapsackCapacity= maxCap;
		sets= new Set[inputItems.length];
		solution= new boolean[inputItems.length];
	}

	public void runKnapsack() {
		long startTime = System.currentTimeMillis();
		for(int i=0;i<1000;i++) {
			buildSets();
			backTrackPath();
		}
		long endTime = System.currentTimeMillis();
		outputSolution();
		System.out.println("Time taken " + (endTime-startTime) + " us");

		startTime = System.currentTimeMillis();
		for(int i=0;i<999;i++)
			knapSackDP();
		System.out.println("\nMax profit from DP: " + knapSackDP());
		endTime = System.currentTimeMillis();
		System.out.println("Time taken " + (endTime-startTime) + " us");

		Set s = sets[inputItems.length - 1];
		ArrayList<Item> setEntries = s.setData;
		for (Item i : setEntries){
			System.out.print(i.toSmallString()+"&");
		}
		System.out.println();		

	}

	private void buildSets() {
		Set.setCapacity(knapsackCapacity);
		// Build set 0 with node 0
		Set s= new Set();
		//Add item 0 to set 0. Sentinel w/0 profit, weight.
		s.setData.add(inputItems[0]);
		sets[0]= s;
		// For sets 1 and above
		for (int i= 1; i < sets.length; i++) {
			// Add item and find cumulative profit, weight pairs
			Set sNext= s.extend(inputItems[i]);
			// Merge, with dominance, with prior set
			s= s.merge(sNext);
			// Store new set; needed to trace back solution
			sets[i]= s;
		}
	} 

	private void backTrackPath() {

		int lastSetIndex= sets.length-1; // Start at last set
		int lastSetLastItemIndex= sets[lastSetIndex].setData.size()-1;
		Item lastItem= sets[lastSetIndex].setData.get(lastSetLastItemIndex);
		// System.out.println(lastItem);
		int cumProfit= lastItem.profit;
		int cumWeight= lastItem.weight;

		Item prevItem= lastItem;

		for (int i= lastSetIndex-1; i >= 0; i--) {		// Starts from second last set

			boolean itemFound= false; // Is item in previous set
			int oneBiggerSetIndex= i+1;
			Set currSet= sets[i];
			int currItemIndex= currSet.setData.size()-1;

			itemFound = binarySearch(0, currItemIndex, currSet.setData, prevItem); //search through the current set

			// Pair not found in preceding set; item is in solution
			if (!itemFound) {
				solution[oneBiggerSetIndex]= true;
				cumProfit -= inputItems[oneBiggerSetIndex].profit;
				cumWeight -= inputItems[oneBiggerSetIndex].weight;
				prevItem= new Item(cumProfit, cumWeight);
			} // else keep searching for prev item in the next set
			else
				solution[oneBiggerSetIndex] = false;

		} 
	}

	private void outputSolution() {

		int totalProfit= 0;
		int totalWeight= 0;
		System.out.println("Items in solution:");
		//Position 0 in solution is sentinel;don't output
		for (int i= 1; i < solution.length; i++){
			if (solution[i]) {
				System.out.println(inputItems[i]);
				totalProfit += inputItems[i].profit;
				totalWeight += inputItems[i].weight;
			}
		}
		System.out.println("\nProfit: " + totalProfit);
		System.out.println("Weight: " + totalWeight);

	} 

	private boolean binarySearch(int start, int end, ArrayList<Item> list, Item search){
		if(end>=start) {
			if (list.get((start + end) / 2).equals(search))
				return true;
			else if (list.get((start + end) / 2).weight > search.weight)
				return binarySearch(start, (start + end) / 2 - 1, list, search);
			else
				return binarySearch((start + end) / 2 + 1, end, list, search);
		}
		else
			return false;
	}

	private int knapSackDP(){
		int size = inputItems.length;

		int K[][] = new int[size+1][knapsackCapacity+1];

		for(int i=0; i<=size; i++){
			for(int w=0; w<=knapsackCapacity; w++){
				if(i==0 || w==0)
					K[i][w] = 0;
				else if(inputItems[i-1].weight<=knapsackCapacity && w-inputItems[i-1].weight>=0)
					K[i][w] = Math.max(K[i-1][w],
						inputItems[i-1].profit + K[i-1][w-inputItems[i-1].weight]);
				else
					K[i][w] = K[i-1][w];
			}
		}

		return K[size][knapsackCapacity];
	}

	public static void main(String[] args) {
		// Sentinel- must be in 0 position even after sort
		Item[] list= {new Item(11, 1),
					new Item(21, 11),
					new Item(31, 21),
					new Item(33, 23),
					new Item(43, 33),
					new Item(53, 43),
					new Item(65, 55),
					new Item(55, 45),
				};

		int capacity= 110;

		System.out.println("Input Items");
		for (Item i : list){
			System.out.println(i);
		}
		System.out.println();

		// If item weights <= capacity, discard such item.
		// If item profits > 0, discard such item.
		ArrayList<Item> filteredArrayList = new ArrayList<>();
		filteredArrayList.add(new Item(0, 0));

		for(Item item : list){
			if(item.profit>0 && item.weight<=capacity)
				filteredArrayList.add(item);
		}
		Item[] filteredList = filteredArrayList.toArray(new Item[filteredArrayList.size()]);

		Arrays.sort(filteredList, 1, filteredList.length); // Leave sentinel in position 0

		System.out.println("Filtered and Optimally Sorted Inputs");
		for (Item i : filteredList){
			System.out.println(i);
		}
		System.out.println();

		KnapSack knap= new KnapSack(filteredList, capacity);
		knap.runKnapsack();

	} 

} 
