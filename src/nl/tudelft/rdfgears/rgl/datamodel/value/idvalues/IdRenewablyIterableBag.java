package nl.tudelft.rdfgears.rgl.datamodel.value.idvalues;

import java.util.Iterator;

import nl.tudelft.rdfgears.engine.Engine;
import nl.tudelft.rdfgears.engine.bindings.idvalues.IdBagBinding;
import nl.tudelft.rdfgears.rgl.datamodel.value.RGLValue;
import nl.tudelft.rdfgears.rgl.datamodel.value.ifaces.AbstractBagValue;
import nl.tudelft.rdfgears.rgl.datamodel.value.ifaces.RenewablyIterableBag;
import nl.tudelft.rdfgears.util.RenewableIterator;

import com.sleepycat.bind.tuple.TupleBinding;

public class IdRenewablyIterableBag extends IdRGLValue implements RenewablyIterableBag {

	public IdRenewablyIterableBag(long id) {
		super(id);
	}

	public IdRenewablyIterableBag(RGLValue value) {
		super(value);
	}

	@Override
	public RenewableIterator<RGLValue> renewableIterator(long id) {
		return new IdRenewableIterator(this, id, false);
	}

	@Override
	public Iterator<RGLValue> iterator() {
		return ((RenewablyIterableBag) fetch().asBag()).iterator();
	}

	@Override
	public int size() {
		return ((AbstractBagValue) fetch()).size();
	}

	@Override
	public AbstractBagValue asBag() {
		return this;
	}

	@Override
	public boolean isBag() {
		return true;
	}

	@Override
	public TupleBinding<RGLValue> getBinding() {
		return new IdBagBinding();
	}

	// @Override
	// public RenewableIterator<RGLValue> previousRenewableIterator(long id) {
	// return new IdRenewableIterator(this, id, true);
	// }

}

class IdRenewableIterator implements RenewableIterator<RGLValue> {

	private IdRenewablyIterableBag bag;
	private long id;
	private boolean previous;

	public IdRenewableIterator(IdRenewablyIterableBag bag, long id, boolean previous) {
		Engine.getLogger().info(bag.toString() + " id = " + id);
		this.bag = bag;
		this.id = id;
		this.previous = previous;
	}

	@Override
	public boolean hasNext() {
		return ((RenewablyIterableBag) bag.fetch().asBag()).renewableIterator(id).hasNext();
	}

	@Override
	public RGLValue next() {
		return ((RenewablyIterableBag) bag.fetch().asBag()).renewableIterator(id).next();
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub

	}

}
