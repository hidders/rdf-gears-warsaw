package nl.tudelft.rdfgears.engine.server;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.tudelft.rdfgears.engine.Engine;
import nl.tudelft.rdfgears.engine.Optimizer;
import nl.tudelft.rdfgears.engine.WorkflowLoader;
import nl.tudelft.rdfgears.rgl.datamodel.type.RDFType;
import nl.tudelft.rdfgears.rgl.datamodel.type.RGLType;
import nl.tudelft.rdfgears.rgl.datamodel.value.RGLValue;
import nl.tudelft.rdfgears.rgl.datamodel.value.visitors.ImRealXMLSerializer;
import nl.tudelft.rdfgears.rgl.datamodel.value.visitors.ValueSerializer;
import nl.tudelft.rdfgears.rgl.exception.WorkflowCheckingException;
import nl.tudelft.rdfgears.rgl.exception.WorkflowLoadingException;
import nl.tudelft.rdfgears.rgl.workflow.Workflow;
import nl.tudelft.rdfgears.util.ValueParser;
import nl.tudelft.rdfgears.util.row.HashValueRow;
import nl.tudelft.rdfgears.util.row.TypeRow;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;




class WorkflowExecHandler extends AbstractWorkflowHandler {
	

	@Override
	void doWorkflowAction(Workflow workflow, Request baseRequest,
			HttpServletResponse response) throws IOException {
		ServletOutputStream output = response.getOutputStream();
		
    	Engine.getLogger().debug("Request for workflow "+workflow.getFullName());
    	
		assert(workflow!=null);
		
		/* check whether all inputs are provided */
		TypeRow workflowInputTypeRow = new TypeRow();
		HashValueRow workflowInputRow = new HashValueRow();
		 
		for (String inputName : workflow.getRequiredInputNames()){
			String strVal = baseRequest.getParameter(inputName);
			
			if (strVal==null){
				/** fall back to a html page */
				throw new IllegalArgumentException("The GET parameter '"+inputName+"' needs a value");
			}
			
    		try {
    			RGLValue val = ValueParser.parseNTripleValue(strVal);
    			assert(val!=null);
    			assert(inputName!=null);
    			workflowInputRow.put(inputName, val);
    			workflowInputTypeRow.put(inputName, RDFType.getInstance()); // typechecking doesn't distinguish URI/literal

    		} catch (ParseException e){
    			throw new IllegalArgumentException("Cannot parse value '"+strVal+"' (given for GET-param '"+inputName+"'): "+e.getMessage());
    		}
		}
		
		
        response.setContentType("application/xml;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		
		try {
			RGLType returnType = Engine.typeCheck(workflow, workflowInputTypeRow);
			/* optimize */
			workflow = (new Optimizer()).optimize(workflow, false);
			Engine.getLogger().debug("Loaded workflow "+workflow.getFullName()+"; executing. ");
			ValueSerializer serializer = new ImRealXMLSerializer(returnType, output);
			
			serializer.serialize(workflow.execute(workflowInputRow));
			
			
		} catch (WorkflowCheckingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}