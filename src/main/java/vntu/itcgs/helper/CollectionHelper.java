package vntu.itcgs.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CollectionHelper {
	public static <T> T mostCommon(List<T> list, double percent) {
		if (list.isEmpty())
			return null;
		
		Map<T, Integer> map = new HashMap<>();
		
		for (T t : list) {
			Integer count = map.get(t);
			map.put(t, count == null ? 1 : count + 1);
		}
		
		double freq = map.size() / (double) list.size();
		if (1.0 == freq || (1.0 - freq) < percent)
			return null;
		
		Entry<T, Integer> max = null;
		for (Entry<T, Integer> e : map.entrySet()) {
			if (max == null || e.getValue() > max.getValue()) {
				max = e;
			}
		}
		
		return max.getKey();
	}
}
