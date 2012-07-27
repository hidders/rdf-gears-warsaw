package nl.tudelft.rdfgears.engine.server;


import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;

import java.net.BindException;
import nl.tudelft.rdfgears.engine.Engine;

 

/**
 * This is an ugly webapplication  
 * @author af09017
 *
 */

public class RDFGearsServer {
	private int port = 8080;
	
	public void setPort(int p){
		port = p;
	}
	
	public void start(){

	    Server server = new Server(port);

	    ContextHandler execContext = new ContextHandler();
        execContext.setContextPath("/rdfgears");
        execContext.setResourceBase(".");
        execContext.setClassLoader(Thread.currentThread().getContextClassLoader());
        execContext.setHandler(new WorkflowExecHandler());
        
        
        ContextHandler formContext = new ContextHandler();
        formContext.setContextPath("/rdfgears-input");
        formContext.setResourceBase(".");
        formContext.setClassLoader(Thread.currentThread().getContextClassLoader());
        formContext.setHandler(new WorkflowInputFormHandler());
        
        
//        

//        server.setHandler(execContext);
 
        
        ContextHandlerCollection handlerCollection = new ContextHandlerCollection();
        handlerCollection.setHandlers(new Handler[] { execContext, formContext });
        
//        ContextHandler inputContext = handlerCollection.addContext("rdfgears-input", ".");
//        inputContext.setClassLoader(Thread.currentThread().getContextClassLoader());
//        inputContext.setHandler(new WorkflowInputFormHandler());
//        
 

        server.setHandler(handlerCollection);

//        ContextHandler inputContext = new ContextHandler();
//        inputContext.setContextPath("/rdfgears-input");
//        //inputContext.setResourceBase("http://blah/geit");
//        inputContext.setClassLoader(Thread.currentThread().getContextClassLoader());
//        server.setHandler(inputContext);
// 
//        execContext.setHandler(new WorkflowInputFormHandler());
        
        
        
        try {
			server.start();
	        server.join();
		} catch (BindException e) {
			Engine.getLogger().error("Cannot bind server to port "+port+": "+e.getMessage());
			System.exit(-1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}


