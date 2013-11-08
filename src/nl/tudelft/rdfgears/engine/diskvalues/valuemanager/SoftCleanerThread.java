package nl.tudelft.rdfgears.engine.diskvalues.valuemanager;

import java.lang.ref.ReferenceQueue;

public class SoftCleanerThread extends Thread {
	
	ReferenceQueue<RGLValueWrapper> referenceQueue;
	SoftValueManagerIface manager;
	
	
	public SoftCleanerThread(ReferenceQueue<RGLValueWrapper> referenceQueue, SoftValueManagerIface manager) {
		super();
		this.referenceQueue = referenceQueue;
		this.manager = manager;
	}

	@Override
	public void run() {
		while (manager.alive()) {
			SoftRGLReference reference;
			try {
				reference = (SoftRGLReference) referenceQueue.remove();
				manager.remove(reference.getId());
			} catch (InterruptedException e) {
				//nothing to do here - waked up by manager
			}
		}
	}

}
