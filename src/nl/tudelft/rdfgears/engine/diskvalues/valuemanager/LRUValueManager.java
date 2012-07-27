package nl.tudelft.rdfgears.engine.diskvalues.valuemanager;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import nl.tudelft.rdfgears.rgl.datamodel.value.RGLValue;

/**
 * @author Tomasz Traczyk
 * 
 */
public class LRUValueManager extends AbstractValueManager {
	private Map<Long, RGLValue> valuesCache = new HashMap<Long, RGLValue>();
	private TreeMap<Long, Long> lastUse = new TreeMap<Long, Long>();

	private int valuesCacheSize = 5;

	private void putIntoCache(RGLValue v) {
		if (valuesCache.size() == valuesCacheSize) {
			long lruId = leastRecentlyUsed(lastUse);
			lastUse.remove(lruId);
			RGLValue lruV = valuesCache.remove(lruId);
			dumpValue(lruV);
		}
		valuesCache.put(v.getId(), v);
		lastUse.put(v.getId(), System.currentTimeMillis());
	}

	public void registerValue(RGLValue value) {
		putIntoCache(value);
	}

	@Override
	public RGLValue fetchValue(long id) {
		RGLValue fetchedValue;
		fetchedValue = valuesCache.get(id);
		if (fetchedValue == null) {
			fetchedValue = readValue(id);
			putIntoCache(fetchedValue);
		}
		lastUse.put(id, System.currentTimeMillis());
		return fetchedValue;
	}

	private long leastRecentlyUsed(Map<Long, Long> lastUse) {
		long min = Long.MAX_VALUE;
		long id = -1;

		for (Entry<Long, Long> e : lastUse.entrySet()) {
			if (e.getValue() < min) {
				min = e.getValue();
				id = e.getKey();
			}
		}

		return id;
	}
}
