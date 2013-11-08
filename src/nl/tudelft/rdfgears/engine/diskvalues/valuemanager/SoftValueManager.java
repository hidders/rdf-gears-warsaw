package nl.tudelft.rdfgears.engine.diskvalues.valuemanager;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import nl.tudelft.rdfgears.rgl.datamodel.value.RGLValue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SoftValueManager extends AbstractBDBValueManager implements SoftValueManagerIface {

	private Map<Long, SoftRGLReference> valuesCache = new HashMap<Long, SoftRGLReference>();
	private static Logger logger = LoggerFactory
			.getLogger(SoftValueManager.class);
	private ReferenceQueue<RGLValueWrapper> referenceQueue = new ReferenceQueue<RGLValueWrapper>();
	private boolean alive = true;
	private Thread cleanerThread;

	public SoftValueManager() {
		cleanerThread = new SoftCleanerThread(referenceQueue, this);
		cleanerThread.setName("alaMaKota");
		cleanerThread.start();
	}

	@Override
	public void registerValue(RGLValue value) {
		logger.debug("put");
		RGLValueWrapper wrapper = new RGLValueWrapper(value, false);
		putIntoCache(wrapper);
//		dumpValue(wrapper);
	}

	@Override
	public RGLValue fetchValue(long id) {
		SoftReference<RGLValueWrapper> ref = valuesCache.get(id);
		if (ref == null || ref.get() == null) {
			RGLValueWrapper fetchedValue = readValue(id);
			putIntoCache(fetchedValue);
			return fetchedValue.getRglValue();
		} else {
			return ref.get().getRglValue();
		}
	}

	private void putIntoCache(RGLValueWrapper value) {
		valuesCache.put(value.getRglValue().getId(), new SoftRGLReference(value, referenceQueue));
	}

	@Override
	public void updateValue(RGLValue value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(long id) {
		valuesCache.remove(id);
	}

	@Override
	public boolean alive() {
		return this.alive;
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
		dumpValue(value);
	}

}
