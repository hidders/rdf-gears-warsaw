package nl.tudelft.rdfgears.rgl.function.custom;

import nl.tudelft.rdfgears.rgl.datamodel.type.BagType;
import nl.tudelft.rdfgears.rgl.datamodel.type.BooleanType;
import nl.tudelft.rdfgears.rgl.datamodel.type.RGLType;
import nl.tudelft.rdfgears.rgl.datamodel.value.RGLValue;
import nl.tudelft.rdfgears.rgl.datamodel.value.impl.BooleanValueImpl;
import nl.tudelft.rdfgears.rgl.function.SimplyTypedRGLFunction;
import nl.tudelft.rdfgears.util.row.ValueRow;

public class SizeOf2Function extends SimplyTypedRGLFunction {
	
	public SizeOf2Function() {
		requireInput("value");
	}

	@Override
	public RGLType getOutputType() {
		return BooleanType.getInstance();
	}

	@Override
	public RGLValue simpleExecute(ValueRow inputRow) {
		if (inputRow.get("value").asBag().size() > 1) {
			return BooleanValueImpl.getTrueInstance();
		}
		return BooleanValueImpl.getFalseInstance();
	}

}
