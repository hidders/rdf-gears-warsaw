package nl.tudelft.rdfgears.engine.diskvalues.valuemanager;

import nl.tudelft.rdfgears.rgl.datamodel.value.RGLValue;

import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;
import org.apache.jcs.engine.CompositeCacheAttributes;
import org.apache.log4j.Logger;

public class JCSValueManager extends AbstractValueManager {

	private static final String CACHE_REGION_NAME = "gears";
	private JCS cache;
	private Logger logger = Logger.getLogger("manager");

	public JCSValueManager() {
		
		try {
			cache = JCS.getInstance(CACHE_REGION_NAME);
			CompositeCacheAttributes cca = new CompositeCacheAttributes();
		} catch (CacheException e) {
			// FIXME
		}
	}

	@Override
	public void registerValue(RGLValue value) {
		try {
			cache.put(Long.toString(value.getId()), value);
		} catch (CacheException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public RGLValue fetchValue(long id) {
		RGLValue fetched = (RGLValue) cache.get(Long.toString(id));
		
		if (fetched == null) {
			throw new RuntimeException("Not found for:\t" + id);
//			System.err.println("Not found for:\t" + id);
		}

		return fetched;
	}

	@Override
	public void updateValue(RGLValue value) {
		registerValue(value);
	}

	@Override
	public void shutDown() {
		// TODO Auto-generated method stub
		try {
			cache.clear();
		} catch (CacheException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		cache.dispose();
	}

}
