package nl.tudelft.rdfgears.engine.diskvalues.valuemanager;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import nl.tudelft.rdfgears.rgl.datamodel.value.RGLValue;

/**
 * @author Tomasz Traczyk
 * 
 */
public class MixedValueManager extends AbstractBDBValueManager implements SoftValueManagerIface {
	
	private int valuesCacheSize = 1000;
	
	private ReferenceQueue<RGLValueWrapper> referenceQueue = new ReferenceQueue<RGLValueWrapper>();
	
	private Thread cleanerThread;
	
	private Map<Long, RGLValueWrapper> valuesCache = new LinkedHashMap<Long, RGLValueWrapper>(valuesCacheSize, 0.75f, true) {

		private static final long serialVersionUID = -7358692784318271466L;

		@Override
		protected boolean removeEldestEntry(Entry<Long, RGLValueWrapper> eldest) {
			if (size() == valuesCacheSize) {
				dumpValue(eldest.getValue());
				softValuesCache.put(eldest.getValue().getRglValue().getId(), new SoftRGLReference(eldest.getValue(), referenceQueue));
				return true;
			} else {
				return false;
			}
		}
		
	};
	private Map<Long, SoftRGLReference> softValuesCache = new HashMap<Long, SoftRGLReference>();
	
	private boolean alive = true;
	
	public MixedValueManager() {
		cleanerThread = new SoftCleanerThread(referenceQueue, this);
		cleanerThread.setName("alaMaKota");
		cleanerThread.start();
	}

	private void putIntoCache(RGLValueWrapper v) {
		valuesCache.put(v.getRglValue().getId(), v);
	}

	public void registerValue(RGLValue value) {
		putIntoCache(new RGLValueWrapper(value, false));
	}

	@Override
	public RGLValue fetchValue(long id) {
		RGLValueWrapper fetchedValue;
		fetchedValue = valuesCache.get(id);
		if (fetchedValue == null) {
			SoftReference<RGLValueWrapper> ref = softValuesCache.get(id);
			if (ref == null || ref.get() == null) {
				fetchedValue = readValue(id);
				softValuesCache.put(id, new SoftRGLReference(fetchedValue, referenceQueue));
//				putIntoCache(fetchedValue);
				return fetchedValue.getRglValue();
			} else {
				return ref.get().getRglValue();
			}
		}
		return fetchedValue.getRglValue();
	}

	@Override
	public void updateValue(RGLValue value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(long id) {
		softValuesCache.remove(id);
	}

	@Override
	public boolean alive() {
		// TODO Auto-generated method stub
		return alive;
	}
	
	@Override
	public void shutDown() {
		super.shutDown();
		this.alive = false;
		this.cleanerThread.interrupt();
		try {
			this.cleanerThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void finalize(RGLValueWrapper value) {
//		dumpValue(value);
	}

}
