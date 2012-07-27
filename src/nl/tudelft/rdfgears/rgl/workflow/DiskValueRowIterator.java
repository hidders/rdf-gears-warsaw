package nl.tudelft.rdfgears.rgl.workflow;

import java.util.HashMap;
import java.util.Iterator;

import nl.tudelft.rdfgears.engine.ValueFactory;
import nl.tudelft.rdfgears.rgl.datamodel.value.RGLValue;
import nl.tudelft.rdfgears.rgl.datamodel.value.ifaces.RenewablyIterableBag;
import nl.tudelft.rdfgears.util.row.ValueRow;

/**
 * ValueRowIterator is an iterator over ValueRow objects. Construction takes an
 * ValueRow and a processor that iterates. It then offers a way to iterate over
 * ValueRows that are constructed from the given ValueRow according to the RGL
 * specification. That is, the types of the non-iterating ports are preserved
 * and the values simply passed on; But for the iterating ports, the value in
 * the returned ValueRow is some value taken from the bag in the original
 * ValueRow.
 * 
 * @author Tomasz Traczyk
 * 
 */
public class DiskValueRowIterator extends AbstractValueRowIterator {

	/*
	 * A map with iterators over the bags. Each iterator has a certain state.
	 * Altogether these states determine how far the iteration is.
	 */
	private HashMap<String, Long> inputIterIdMap;

	private Long newestIterationId;

	public DiskValueRowIterator(ValueRow originalInputs,
			FunctionProcessor processor, boolean recycleReturnRow) {
		super(originalInputs, processor, recycleReturnRow);
		newestIterationId = ValueFactory.getNewId();
	}

	@Override
	protected Iterator<RGLValue> resetBagIterator(String name) {
		if (inputIterIdMap == null)
			inputIterIdMap = new HashMap<String, Long>();
		if (newestIterationId == null)
			newestIterationId =  ValueFactory.getNewId();
		if (newestIterationId.equals(inputIterIdMap.get(name))) {
			newestIterationId = ValueFactory.getNewId();
		}
		Iterator<RGLValue> bagIter = ((RenewablyIterableBag) originalInputs
				.get(name).asBag()).renewableIterator(newestIterationId);
		inputIterIdMap.put(name, newestIterationId);
		return bagIter;
	}

	@Override
	protected Iterator<RGLValue> getBagIterator(String name) {
		Iterator<RGLValue> it = ((RenewablyIterableBag) originalInputs.get(name).asBag())
		.renewableIterator(inputIterIdMap.get(name));
		return it;
	}

}
