package nl.tudelft.rdfgears.rgl.function.custom;

import java.util.List;

import nl.tudelft.rdfgears.engine.ValueFactory;
import nl.tudelft.rdfgears.rgl.datamodel.type.BagType;
import nl.tudelft.rdfgears.rgl.datamodel.type.RDFType;
import nl.tudelft.rdfgears.rgl.datamodel.type.RGLType;
import nl.tudelft.rdfgears.rgl.datamodel.value.RGLValue;
import nl.tudelft.rdfgears.rgl.datamodel.value.ifaces.AbstractBagValue;
import nl.tudelft.rdfgears.rgl.datamodel.value.impl.bags.ListBackedBagValue;
import nl.tudelft.rdfgears.rgl.function.SimplyTypedRGLFunction;
import nl.tudelft.rdfgears.util.row.ValueRow;

public class BagOf10001Function extends SimplyTypedRGLFunction {

	@Override
	public RGLType getOutputType() {
		return BagType.getInstance(RDFType.getInstance());
	}

	@Override
	public RGLValue simpleExecute(ValueRow inputRow) {
		List<RGLValue> backingList = ValueFactory.createBagBackingList();
		
		for (int i = 0; i < 20001; ++i) {
			backingList.add(ValueFactory.createLiteralDouble(i));
		}
		AbstractBagValue bag = new ListBackedBagValue(backingList);
		return bag;
	}

}
