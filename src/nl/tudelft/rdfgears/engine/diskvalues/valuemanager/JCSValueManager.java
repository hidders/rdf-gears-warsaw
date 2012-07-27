package nl.tudelft.rdfgears.engine.diskvalues.valuemanager;

import java.util.HashSet;
import java.util.Set;

import nl.tudelft.rdfgears.rgl.datamodel.value.RGLValue;

import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;

public class JCSValueManager extends AbstractValueManager {
	
	private static final String CACHE_REGION_NAME = "gears";
	private JCS cache;
	private Set<Long> registered = new HashSet<Long>();

	public JCSValueManager() {
		try {
			cache = JCS.getInstance(CACHE_REGION_NAME);
		} catch (CacheException e) {
			//FIXME
		}
	}
	
	
	@Override
	public void registerValue(RGLValue value) {
		try {
			cache.put(Long.toString(value.getId()), value);
			registered.add(value.getId());
		} catch (CacheException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public RGLValue fetchValue(long id) {
		RGLValue fetched = (RGLValue) cache.get(Long.toString(id));
		if (fetched == null) {
			System.out.println(id);
			System.out.println(registered.size());
		}
		return fetched;
	}

}
