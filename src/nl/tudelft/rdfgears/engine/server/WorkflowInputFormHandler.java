package nl.tudelft.rdfgears.engine.server;
import java.io.IOException;
import java.io.OutputStream;

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

import com.hp.hpl.jena.n3.turtle.parser.ParseException;



class WorkflowInputFormHandler extends AbstractWorkflowHandler {
	
	
	/**
	 * This is ugly, a JSP would probably be better but i don't want to make this
	 * a full-fledged webapp now 
	 */
	@Override
	void doWorkflowAction(Workflow workflow, Request baseRequest,
			HttpServletResponse response) throws IOException {
		ServletOutputStream output = response.getOutputStream();
		
		
		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		
    	Engine.getLogger().debug("Request for workflow "+workflow.getFullName());

    	output.println("<html><head><title></title></head><body>");
    	output.println(String.format("provide workflow input parameters for workflow %s <br/>", workflow.getFullName()));
		
		assert(workflow!=null);
		output.println(String.format("<form name=\"input\" action=\"/rdfgears/%s\" method=\"get\">", workflow.getFullName()));
		
		for (String inputName : workflow.getRequiredInputNames()){
			output.print(String.format("%s: <input type=\"text\" name=\"%s\" size=\"70\"/><br/>", inputName, inputName));
			
		}
		output.println("<input type=\"submit\" value=\"run workflow\"/>");
		
		
		output.println("</form>");
		

		output.println("<br/><br/>");
		output.println("Examples: <br/>");
		output.println("&lt;http://dbpedia.org/resource/Delft&gt; <br/>");
		output.println("\"an apple\" <br/>");
		output.println("\"la pomme\"@fr <br/>");
		output.println("\"1.2\"^^&lt;http://www.w3.org/2001/XMLSchema#double&gt; <br/>");
		
		

		
		
		output.print("<html><head><title></title></head><body>");
		
		
		
		
	}
}