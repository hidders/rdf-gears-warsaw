package nl.tudelft.rdfgears.engine.diskvalues.valuemanager;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import nl.tudelft.rdfgears.rgl.datamodel.value.RGLValue;

/**
 * @author Tomasz Traczyk
 * 
 */
public class LRUValueManager extends AbstractBDBValueManager {
	private static int valuesCacheSize = 100;
	
	private Map<Long, RGLValueWrapper> valuesCache = new LinkedHashMap<Long, RGLValueWrapper>(valuesCacheSize, 1, true) {

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
	
	private void putIntoCache(RGLValueWrapper v) {
		valuesCache.put(v.getRglValue().getId(), v);
	}

	public void registerValue(RGLValue value) {
		putIntoCache(new RGLValueWrapper(value, false));
	}

	@Override
	public RGLValue fetchValue(long id) {
		increaseAllCount();
		RGLValueWrapper fetchedValue;
		fetchedValue = valuesCache.get(id);
		if (fetchedValue == null) {
			fetchedValue = readValue(id);
			putIntoCache(fetchedValue);
		}
		return fetchedValue.getRglValue();
	}

	@Override
	public void updateValue(RGLValue value) {
		// TODO Auto-generated method stub
	}
}
