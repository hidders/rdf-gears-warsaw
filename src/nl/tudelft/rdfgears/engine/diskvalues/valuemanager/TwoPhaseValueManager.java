package nl.tudelft.rdfgears.engine.diskvalues.valuemanager;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import nl.tudelft.rdfgears.rgl.datamodel.value.RGLValue;

public class TwoPhaseValueManager extends AbstractBDBValueManager {

	private static final int VALUES_CACHE_SIZE = 100;
	
	@SuppressWarnings("serial")
	private Map<Long, RGLValue> phaseOne = new LinkedHashMap<Long, RGLValue>(VALUES_CACHE_SIZE, 1, true) {

		@Override
		protected boolean removeEldestEntry(Entry<Long, RGLValue> eldest) {
			if (size() + phaseTwo.size() < VALUES_CACHE_SIZE) {
				return false;
			} else {
				if (!phaseTwo.isEmpty()) {
					phaseTwo.remove(phaseTwo.keySet().iterator().next());
					return false;
				} else {
					dumpValue(eldest.getValue());
					return true;
				}
			}
		}
		
	};
	
	@SuppressWarnings("serial")
	private HashMap<Long, RGLValue> phaseTwo = new LinkedHashMap<Long, RGLValue>(VALUES_CACHE_SIZE, 1, true) {
		@Override
		protected boolean removeEldestEntry(Entry<Long, RGLValue> eldest) {
			return (size() + phaseOne.size() > VALUES_CACHE_SIZE);
		}
	};
	
	@Override
	public void registerValue(RGLValue value) {
		phaseOne.put(value.getId(), value);
	}

	@Override
	public RGLValue fetchValue(long id) {
		increaseAllCount();
		RGLValue fetchedValue = phaseOne.get(id);
		
		if (fetchedValue == null) {
			fetchedValue = phaseTwo.get(id);
		}
		
		if (fetchedValue == null) {
			fetchedValue = readValue(id).getRglValue();
			phaseTwo.put(fetchedValue.getId(), fetchedValue);
		}
		
		return fetchedValue;
	}

	@Override
	public void updateValue(RGLValue value) {
		// TODO Auto-generated method stub

	}

}
