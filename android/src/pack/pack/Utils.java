package pack.pack;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Utils {
	static class ValueComparator implements Comparator<String> {

		Map<String, Integer> base;

		public ValueComparator(Map<String, Integer> base) {
			this.base = base;
		}

		public int compare(String a, String b) {

			if ((Integer) base.get(a) < (Integer) base.get(b)) {
				return 1;
			} else if ((Integer) base.get(a) == (Integer) base.get(b)) {
				return 0;
			} else {
				return -1;
			}
		}
	}

	static class Couple {
		String person1, person2;
		public Couple(String p1, String p2)
		{
			person1=p1;
			person2=p2;
		}
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return person1+" pays "+person2;
		}
	}
	static class SimpleDistributor extends Distributor
	{
		public SimpleDistributor(Map<String, Integer> map) {
			super(map);
			// TODO Auto-generated constructor stub
		}

		@Override
		public HashMap<Couple, Integer> doDistribute() {
			// TODO Auto-generated method stub
			HashMap<Couple, Integer> hm = new HashMap<Utils.Couple, Integer>();
			while(treeMap.size()>0)
			{
				Map.Entry<String, Integer> first = treeMap.firstEntry();
				Map.Entry<String, Integer> last = treeMap.lastEntry();
				if(first.getValue()>-last.getValue())
				{
					hm.put(new Couple(first.getKey(), last.getKey()), -last.getValue());
					treeMap.remove(last.getKey());
				}
				else if(first.getValue()<-last.getValue())
				{
					hm.put(new Couple(first.getKey(), last.getKey()), first.getValue());
					treeMap.remove(first.getKey());
				}
				else
				{
					hm.put(new Couple(first.getKey(), last.getKey()), first.getValue());					
					treeMap.remove(first.getKey());
					treeMap.remove(last.getKey());
				}
			}
			return hm;
		}
	}
	public static Distributor chooseDistributor(int distributor_id, HashMap<String, Integer> unsortedSummaryMap)
	{
		switch(distributor_id)
		{
			default:
				return new SimpleDistributor(unsortedSummaryMap);
		}
	}
}
