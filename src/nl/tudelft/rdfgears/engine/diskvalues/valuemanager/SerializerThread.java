package nl.tudelft.rdfgears.engine.diskvalues.valuemanager;

import nl.tudelft.rdfgears.engine.bindings.EncapsulatedBinding;
import nl.tudelft.rdfgears.engine.diskvalues.DatabaseManager;
import nl.tudelft.rdfgears.rgl.datamodel.value.RGLValue;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.je.DatabaseEntry;

public class SerializerThread extends Thread {
	
	AbstractBDBValueManager manager;
	RGLValue value;
	EntryBinding<RGLValue> binding;
	
	public SerializerThread(AbstractBDBValueManager manager, EntryBinding<RGLValue> binding, RGLValue value) {
		this.manager = manager;
		this.value = value;
		this.binding = binding;
	}

	@Override
	public void run() {
			DatabaseEntry valueEntry = new DatabaseEntry();
			DatabaseEntry idEntry = DatabaseManager.long2entry(value.getId());
			
			binding.objectToEntry(value, valueEntry);
			
			EncapsulatedBinding.write(idEntry, valueEntry);
			manager.clearThread();
	}
	
	public long getValueId() {
		return value.getId();
	}

}
