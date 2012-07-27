package nl.tudelft.rdfgears.rgl.datamodel.value;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import nl.tudelft.rdfgears.engine.Engine;
import nl.tudelft.rdfgears.rgl.datamodel.value.ifaces.RenewablyIterableBag;
import nl.tudelft.rdfgears.util.RenewableIterator;

public abstract class OrderedBagValue extends BagValue implements
		RenewablyIterableBag {

	protected Map<Long, Integer> iteratorPosition = new HashMap<Long, Integer>();

	public final Map<Long, Integer> getIteratorMap() {
		return iteratorPosition;
	}

	@Override
	public final RenewableIterator<RGLValue> renewableIterator(long id) {
		int pointAt;
		if (!iteratorPosition.containsKey(id)) {
			iteratorPosition.put(id, 0);
			pointAt = 0;
		} else {
			pointAt = iteratorPosition.get(id);//Math.max(iteratorPosition.get(id) - 1 , 0);
			// this way, we gave the last element of the last iteration again,
			// if you don't want it - simply skip it.
		}

		return new OrderedBagIterator(id, pointAt);
	}
	
//	@Override
//	public final RenewableIterator<RGLValue> previousRenewableIterator(long id) { //FIXME wydzielić część wspólną z renewableIterator
//		int pointAt;
//		if (!iteratorPosition.containsKey(id)) {
//			iteratorPosition.put(id, 0);
//			pointAt = 0;
//		} else {
//			pointAt = iteratorPosition.get(id);//Math.max(iteratorPosition.get(id) - 1 , 0);
//			// this way, we gave the last element of the last iteration again,
//			// if you don't want it - simply skip it.
//		}
//
//		return new OrderedBagIterator(id, Math.max(0, pointAt - 1));
//	}

	protected Iterator<RGLValue> iteratorAt(int position) {
		Engine.getLogger().info("iteratorAt(" + position + ")");
		Iterator<RGLValue> iterator = iterator();
		for (int i = 0; i < position; ++i) {
			iterator.next();
		}
		return iterator;
	}

	protected class OrderedBagIterator implements RenewableIterator<RGLValue> {
		private Iterator<RGLValue> innerIterator;
		private int position;
		private long id;

		public OrderedBagIterator(long id, int position) {
			this.id = id;
			this.position = position;
			innerIterator = iteratorAt(position);
		}

		@Override
		public boolean hasNext() {
			return innerIterator.hasNext();
		}

		@Override
		public RGLValue next() {
			position++;
			iteratorPosition.put(id, position);
			return innerIterator.next();
		}

		@Override
		public void remove() {
			assert (false) : "Not implemented";
		}

	}

}