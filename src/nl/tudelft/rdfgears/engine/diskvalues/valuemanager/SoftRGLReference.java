package nl.tudelft.rdfgears.engine.diskvalues.valuemanager;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

public class SoftRGLReference extends SoftReference<RGLValueWrapper> {
	
	private long id;

	public SoftRGLReference(RGLValueWrapper referent, ReferenceQueue<RGLValueWrapper> queue) {
		super(referent, queue);
		this.id = referent.getRglValue().getId();
	}

	public long getId() {
		return id;
	}

}
