package nl.tudelft.rdfgears.engine.diskvalues.valuemanager;

import java.util.HashMap;
import java.util.Map;

import nl.tudelft.rdfgears.engine.bindings.EncapsulatedBinding;
import nl.tudelft.rdfgears.engine.diskvalues.DatabaseManager;
import nl.tudelft.rdfgears.rgl.datamodel.value.RGLValue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.bind.serial.StoredClassCatalog;

public abstract class AbstractBDBValueManager extends AbstractValueManager {
	
	private SerializerThread runningThread = null;
	
	private static Logger logger = LoggerFactory.getLogger(AbstractBDBValueManager.class);

	protected void dumpValue(RGLValueWrapper value) {
		if (value == null) {
			throw new RuntimeException();
		}
		try {
			if (!value.isEverDumped()) {
				value.setEverDumped(true);
				dumpValue(value.getRglValue());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	protected void dumpValue(RGLValue value) {
		increaseWriteCount();
		EncapsulatedBinding.writeComplex(value);
//		StoredClassCatalog classCatalog = DatabaseManager.getClassCatalog();
//		EntryBinding<RGLValue> dataBinding = new SerialBinding<RGLValue>(classCatalog,
//				RGLValue.class);
//		
//		if (runningThread != null) {
//			try {
//				runningThread.join();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		runningThread = new SerializerThread(this, dataBinding, value);
//		runningThread.start();
	}
	
	public synchronized void clearThread() {
		runningThread = null;
	}

	protected RGLValueWrapper readValue(long id) {
//		if (runningThread != null && runningThread.getId() == id) {
//			try {
//				runningThread.join();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
		increaseMissCount();
		return new RGLValueWrapper(EncapsulatedBinding.entryToObject(DatabaseManager.getComplexEntry(id)), true);
	}
	
	@Override
	public void shutDown() {
		if (runningThread != null) {
			try {
				runningThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	@Override
	public void finalize(RGLValueWrapper value) {
		//do nothing
	}
	
}
