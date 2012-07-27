package nl.tudelft.rdfgears.engine.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.tudelft.rdfgears.engine.WorkflowLoader;
import nl.tudelft.rdfgears.rgl.exception.WorkflowLoadingException;
import nl.tudelft.rdfgears.rgl.workflow.Workflow;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;


abstract class AbstractWorkflowHandler extends AbstractHandler {
	
	public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response)
                    		   throws IOException, ServletException  {
		
		baseRequest.setHandled(true);
		
		try {
			Workflow workflow = WorkflowLoader.loadWorkflow(target);
			
			doWorkflowAction(workflow, baseRequest, response);
			
		} catch (WorkflowLoadingException e) {
			// TODO Auto-generated catch block

	        response.setContentType("text/html;charset=utf-8");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getOutputStream().print("The workflow could not be loaded: "+e.getMessage());
			return;
			
		} catch (IllegalArgumentException e){
			// TODO Auto-generated catch block

	        response.setContentType("text/html;charset=utf-8");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getOutputStream().print("Error: "+e.getMessage());
			return;
		} catch (RuntimeException e){
			e.printStackTrace(); // because jetty doesn't do so
			throw(e);
		}catch (AssertionError e){
			e.printStackTrace(); // because jetty doesn't do so
			throw(e);
		}
		
        
        
    }

	abstract void doWorkflowAction(Workflow workflow, Request baseRequest, HttpServletResponse response) throws IOException;
}

