import java.util.ArrayList;

public class Set {

	public ArrayList<Item> setData; 
	private static int maxKnapsackCapacity;

	public Set() {
		setData= new ArrayList<Item>();
	}

	public static void setCapacity(int c) {
		maxKnapsackCapacity= c;
	}

	public Set extend(Item other) {
		Set sDashWithItem= new Set();
		for (Item i: setData) {
			int cumWgt = i.weight + other.weight;
			if (cumWgt <= maxKnapsackCapacity) {
				int cumProf= i.profit + other.profit;
				sDashWithItem.setData.add(new Item(cumProf, cumWgt));
			}
		}
		return sDashWithItem;
	}

	public Set merge(Set sDash) {
		// Merges DPSet other with this DPSet, with dominance pruning
		// Items in any input sort order wind up in weight order

		Set sWithItem = new Set(); 

		// Define limits for while loop on DPSet other
		int indexSDash= 0;
		int maxIndexSDash= sDash.setData.size()-1;
		// Last item profit used for dominance check at end of set
		int lastItemProfitSDash= sDash.setData.get(maxIndexSDash).profit;
		// Define limits for while loop on this DPSet

		int indexSCurrent= 0;
		int maxIdnexSCurrent= setData.size() -1;
		int lastItemProfitCurrent= setData.get(maxIdnexSCurrent).profit;

		while (indexSCurrent <= maxIdnexSCurrent || indexSDash <= maxIndexSDash) {
			
			if (indexSCurrent <= maxIdnexSCurrent && indexSDash <= maxIndexSDash) {
				Item curItem= setData.get(indexSCurrent);
				Item otherItem= sDash.setData.get(indexSDash);
				
				if (curItem.weight < otherItem.weight) {
					sWithItem.setData.add(curItem); // Add item; not dominated by other item
					while (otherItem.profit <= curItem.profit && indexSDash< maxIndexSDash) {
						indexSDash++;
						otherItem= sDash.setData.get(indexSDash); // Other dominated,skip
					}
					indexSCurrent++;
				}
				
				else if (curItem.weight == otherItem.weight) {
					if (curItem.profit >= otherItem.profit) // Other item dominated
						indexSDash++;
					else indexSCurrent++; // Item dominated
				}

				else { // otherItem.weight < item.weight
					sWithItem.setData.add(otherItem); // Add other item, not dominated
					while (curItem.profit <= otherItem.profit && indexSCurrent < maxIdnexSCurrent){
						indexSCurrent++;
						curItem= setData.get(indexSCurrent); // Item dominated; skip it
					}
					indexSDash++;
				}
			}

			else if (indexSCurrent > maxIdnexSCurrent) { // Only other items left to consider
				while (indexSDash <= maxIndexSDash) {
					Item otherItem= sDash.setData.get(indexSDash);
					if (otherItem.profit > lastItemProfitCurrent)
						sWithItem.setData.add(otherItem);
					indexSDash++;
				}
			} 

			else { // indexOther > maxIndexOther. Only items left
				while (indexSCurrent <= maxIdnexSCurrent) {
					Item item= setData.get(indexSCurrent);
					if (item.profit > lastItemProfitSDash)
						sWithItem.setData.add(item);
					indexSCurrent++;
				}
			}
		}
		return sWithItem;
	}

} 