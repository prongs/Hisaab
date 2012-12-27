package pack.pack;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public abstract class Distributor {
	Map<String, Integer> map;
	Utils.ValueComparator bvc;
	TreeMap<String, Integer> treeMap;//is sorted by default

	public Distributor(Map<String, Integer> map) {
		this.map = map;
	}
	public HashMap<Utils.Couple, Integer> distribute()
	{
		preDistribute();
		return doDistribute();
	}
	public void preDistribute()
	{
		bvc=new Utils.ValueComparator(map);
		treeMap = new TreeMap<String, Integer>(bvc);
		treeMap.putAll(this.map);
	}
	public abstract HashMap<Utils.Couple, Integer>  doDistribute();
}
