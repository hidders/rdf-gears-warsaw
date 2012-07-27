package nl.tudelft.rdfgears.engine.bindings;

import nl.tudelft.rdfgears.engine.diskvalues.DatabaseManager;
import nl.tudelft.rdfgears.rgl.datamodel.value.RGLValue;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseEntry;

public class EncapsulatedBinding extends TupleBinding<RGLValue> {

	long id;

	public EncapsulatedBinding(long id) {
		this.id = id;
	}

	@Override
	public RGLValue entryToObject(TupleInput in) {
		String className = in.readString();
		ComplexBinding binding = (ComplexBinding) BindingFactory
				.createBinding(className);
		return binding.complexInputToObject(in, id);
	}

	@Override
	public void objectToEntry(RGLValue value, TupleOutput out) {
		out.writeString(value.getClass().getName());
		((ComplexBinding) value.getBinding()).writeComplexToOutput(value, out);
	}

	public void writeComplex(RGLValue value) {
		Database complexStore = DatabaseManager.getComplexStore();
		DatabaseEntry valueEntry = new DatabaseEntry();
		objectToEntry(value, valueEntry);
		DatabaseEntry idEntry = DatabaseManager.long2entry(value.getId());
		complexStore.put(null, idEntry, valueEntry);
	}
}
