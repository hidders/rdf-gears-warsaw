package nl.tudelft.rdfgears.engine;

import java.io.IOException;

import nl.tudelft.rdfgears.engine.diskvalues.DatabaseManager;
import nl.tudelft.rdfgears.engine.server.RDFGearsServer;
import nl.tudelft.rdfgears.rgl.datamodel.value.RGLValue;
import nl.tudelft.rdfgears.rgl.datamodel.value.visitors.ValueEvaluator;
import nl.tudelft.rdfgears.rgl.datamodel.value.visitors.ValueSerializerInformal;
import nl.tudelft.rdfgears.rgl.datamodel.value.visitors.ValueXMLSerializer;
import nl.tudelft.rdfgears.rgl.exception.FunctionTypingException;
import nl.tudelft.rdfgears.rgl.exception.WorkflowCheckingException;
import nl.tudelft.rdfgears.rgl.workflow.Workflow;
import uk.co.flamingpenguin.jewel.cli.ArgumentValidationException;
import uk.co.flamingpenguin.jewel.cli.CliFactory;

import com.hp.hpl.jena.rdf.model.RDFWriter;

public class RDFGears {
	public static final String NAME = "RDF Gears";
	public static final String VERSION = "0.1";
	private static final WorkflowCheckingException FunctionTypingException = null;

	/**
	 * The workflow to execute
	 */
	private static String WORKFLOW_NAME = "testendpoint";

	private static Workflow workflow; // workflow to be executed
	private static CmdOptions options;

	public static void main(String[] args) {
		/**
		 * See if we can parse cmd line options
		 */
		try {
			options = CliFactory.parseArguments(CmdOptions.class, args);
		} catch (ArgumentValidationException e) {
			printVersionInfo();
			System.out.println("Use the --help option for usage info");
			System.out.println();
			System.out.println(e.getMessage());
			System.exit(1);
		}

		Config cfg = new Config(Config.DEFAULT_CONFIG_FILE);
		if (options.isDebugLevel()) {
			cfg.setDebugLevel(options.getDebugLevel());
		} else {
			cfg.setDebugLevel("WARN");
		}
		Engine.init(cfg);

		/*********************************************
		 * Handle cmd-line options
		 */
		if (options.isDiskBased()) {
			Engine.getConfig().setDiskBased();
		}

		if (options.isWorkflowPathList()) /* configure workflow path */
			Engine.getConfig().configurePath(options.getWorkflowPathList());
		
		
		if (options.getServer()){
			/**
			 * Start RDF Gears as a RESTful HTTP Webservice 
			 */
			Engine.getLogger().debug("Starting RDF Gears as a RESTful Webservice");
			runAsServer();
		} else {
			Engine.getLogger().debug("Starting RDF Gears as workflow interpreter");
			runAsInterpreter();
		}
		
	}
	
	/**
	 * Run a single workflow and output to stdout
	 */
	private static void runAsInterpreter() {

		try {
			workflow = WorkflowLoader.loadWorkflow(options.getWorkflowName());
		} catch (Exception e){
			e.printStackTrace();
			System.exit(-1);
		}

		assert (workflow != null);
		
		if (options.isDiskBased()) {
			Engine.getConfig().setDiskBased();
		}
		
		/**
		 * First typecheck, if not disabled
		 */
		try {
			Engine.typeCheck(workflow, null);
		} catch (WorkflowCheckingException e) {
			handleTypeCheckingErrorAndExit(e);
		}
		
		if (options.getTypecheckOnly()){
			System.out.println("The workflow is well-typed");
			System.exit(0);
		}

		/**
		 * optimize if needed
		 */
		if (!options.getDisableOptimizer()) {
			try {
				assert (workflow != null);
				workflow = (new Optimizer()).optimize(workflow, false);
			} catch (WorkflowCheckingException e) {
				assert( false ) : "Typechecking should have already caught this! ";
				handleTypeCheckingErrorAndExit(e);
			}
		}
		
		if (!options.getTypecheckOnly()) {
			DatabaseManager.initialize();
			execute(workflow);
			DatabaseManager.cleanUp();
		}
		Engine.close();
	}

	private static void runAsServer() {
		RDFGearsServer gearsServer = new RDFGearsServer();
		
		if (options.isPort())
			gearsServer.setPort(options.getPort());
		
		gearsServer.start();
	}

	private static void serializeValueWithSerializer(RGLValue value){
		if (options.isOutputFormat()){
			String format = options.getOutputFormat().toLowerCase();
			if (format.equals("informal")) {
				(new ValueSerializerInformal()).serialize(value);
				return;

			} else if (format.equals("none")) {
				/*
				 * do not serialize but just evaluate it entirely, as if it were
				 * serialized
				 */
				value.accept(new ValueEvaluator());
				return;
			}
		}

		if (value.isGraph()) {
			// do not wrap it in the ValueXMLSerializer RGL-XML headers, but
			// directly serialize RDF-XML
			RDFWriter rdfWriter = new com.hp.hpl.jena.xmloutput.impl.Basic();
			rdfWriter.write(value.asGraph().getModel(), System.out, null);
		} else {
			// default format 
			(new ValueXMLSerializer(System.out)).serialize(value);
		}

	}

	public static void execute(Workflow workflow) {
		try {
			RGLValue value = Engine.executeWorkflow(workflow);

			serializeValueWithSerializer(value);

		} catch (Exception e) {
			Engine.getLogger().error("Workflow execution failed. ");
			Engine.getLogger().error(e.getMessage());

			e.printStackTrace(); // maybe it would be nice to print this to the
			// logger debug channel...
		}

	}

	private static void handleTypeCheckingErrorAndExit(
			WorkflowCheckingException e) {
		System.err
				.println("The workflow is not executable, as it did not pass the typechecking test: ");

		WorkflowCheckingException rootCause = e.getRootCause();
		if (rootCause instanceof FunctionTypingException) {
			FunctionTypingException fEx = (FunctionTypingException) rootCause;
			if (fEx.isIterationProblem())
				System.err
						.println("I think you forgot an iteration marker somewhere! ");
		}
		System.err.println(e.getProblemDescription());
		System.exit(-1);
	}

	private static void printVersionInfo() {
		System.err.println(String.format("%s version %s", NAME, VERSION));
	}
}
