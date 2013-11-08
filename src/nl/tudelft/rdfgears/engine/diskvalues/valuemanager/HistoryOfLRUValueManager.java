package nl.tudelft.rdfgears.engine.diskvalues.valuemanager;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import nl.tudelft.rdfgears.rgl.datamodel.value.RGLValue;

/**
 * @author Tomasz Traczyk
 * 
 */
public class HistoryOfLRUValueManager extends AbstractBDBValueManager {
	private static int valuesCacheSize = 1000;
	
//	private Map<Long, RGLValueWrapper> valuesCache = new HashMap<Long, RGLValueWrapper>(valuesCacheSize);
	
//	private Map<Long, Long> lastUse = new HashMap<Long, Long>();
	
	private Map<Long, RGLValueWrapper> valuesCache = new LinkedHashMap<Long, RGLValueWrapper>(valuesCacheSize, 0.75f, true) {

		private static final long serialVersionUID = -7358692784318271466L;

		@Override
		protected boolean removeEldestEntry(Entry<Long, RGLValueWrapper> eldest) {
			if (size() == valuesCacheSize) {
				dumpValue(eldest.getValue());
				return true;
			} else {
				return false;
			}
		}
		
	};
	
	//would be nice to use TreeBiDiMap
//	private Map<Long, Long> idTimeMap = new HashMap<Long, Long>(); 
//	private TreeMap<Long, Long> timeIdMap = new TreeMap<Long, Long>();
	
//	private List<Long> accessedIds = new LinkedList<Long>();

	private void putIntoCache(RGLValueWrapper v) {
//		if (valuesCache.size() == valuesCacheSize) {
////			long lruId = popLeastRecentlyUsed(idTimeMap, timeIdMap);
//			long lruId = leastRecentlyUsed(lastUse);
//			lastUse.remove(lruId);
//			RGLValueWrapper lruV = valuesCache.remove(lruId);
//			dumpValue(lruV);
//		}
		valuesCache.put(v.getRglValue().getId(), v);
//		recordUsage(v.getRglValue());
//		lastUse.remove(v.getRglValue().getId());
//		lastUse.put(v.getRglValue().getId(), System.currentTimeMillis());
	}

	public void registerValue(RGLValue value) {
		putIntoCache(new RGLValueWrapper(value, false));
	}

	@Override
	public RGLValue fetchValue(long id) {
		RGLValueWrapper fetchedValue;
		fetchedValue = valuesCache.get(id);
		if (fetchedValue == null) {
			fetchedValue = readValue(id);
			putIntoCache(fetchedValue);
		}
//		recordUsage(fetchedValue.getRglValue());
//		lastUse.remove(id);
//		lastUse.put(id, System.currentTimeMillis());

		return fetchedValue.getRglValue();
	}

//	protected Long popLeastRecentlyUsed(Map<Long, Long> idTimeMap,
//			TreeMap<Long, Long> timeIdMap) {
//		Entry<Long, Long> timeId = timeIdMap.pollFirstEntry();
//		if (timeId != null) {
//			idTimeMap.remove(timeId.getValue());
//			return timeId.getValue();
//		} else {
//			return null;
//		}
//	}

//	protected void recordUsage(RGLValue value) {
//		long newTime = System.currentTimeMillis() % 1000 * 100000
//				+ (new Random()).nextInt(99999); // to avoid clashes
//		Long oldTime = idTimeMap.remove(value.getId());
//		idTimeMap.put(value.getId(), newTime);
//		timeIdMap.put(newTime, value.getId());
//		if (oldTime != null) {
//			timeIdMap.remove(oldTime);
//		}
//	}

//	private long leastRecentlyUsed(Map<Long, Long> lastUse) {
//		long min = Long.MAX_VALUE;
//		long id = -1;
//
//		for (Entry<Long, Long> e : lastUse.entrySet()) {
//			if (e.getValue() < min) {
//				min = e.getValue();
//				id = e.getKey();
//			}
//		}
//		return id;
//	}

	@Override
	public void updateValue(RGLValue value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void shutDown() {
		// TODO Auto-generated method stub

	}
}
