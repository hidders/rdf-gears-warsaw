package nl.tudelft.rdfgears.engine.diskvalues.valuemanager;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import nl.tudelft.rdfgears.rgl.datamodel.value.RGLValue;

public class SoftValueManager extends AbstractValueManager {
	
	private Map<Long, SoftReference<RGLValue>> valuesCache = new HashMap<Long, SoftReference<RGLValue>>();

	@Override
	public void registerValue(RGLValue value) {
		putIntoCache(value);
		dumpValue(value);
	}

	@Override
	public RGLValue fetchValue(long id) {
		SoftReference<RGLValue> ref = valuesCache.get(id);
		if (ref == null) {
			RGLValue fetchedValue = readValue(id);
			putIntoCache(fetchedValue);
			return fetchedValue;
		} else {
			return ref.get();
		}
	}
	
	private void putIntoCache(RGLValue value) {
		valuesCache.put(value.getId(), new SoftReference<RGLValue>(value));
	}

	@Override
	public void updateValue(RGLValue value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shutDown() {
		// TODO Auto-generated method stub
		
	}

}
