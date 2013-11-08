package nl.tudelft.rdfgears.engine.diskvalues.valuemanager;

import nl.tudelft.rdfgears.rgl.datamodel.value.RGLValue;

public class PureBDBValueManage extends AbstractBDBValueManager {

	@Override
	public void registerValue(RGLValue value) {
		dumpValue(value);
	}

	@Override
	public RGLValue fetchValue(long id) {
		return readValue(id).getRglValue();
	}

	@Override
	public void updateValue(RGLValue value) {
		// TODO Auto-generated method stub

	}


}
