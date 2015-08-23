/**
 * 
 */
package org.strabil.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.Exception;
import java.util.Enumeration;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import org.neo4j.graphdb.*;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.strabil.market.NodeWrapper;
import org.strabil.utils.DoTest;


/**
 *  This is a class (singleton) for run control.
 *  The following (user implemented) classes must be given to the Initialize method:
 *	- {@link RunAction}
 * 	- {@link EventAction}	
 * 	- {@link PrimaryGeneratorAction}
 *
 * @author Mario Alemi
 * @version 0.1
 * 
 * NB This is the only class which MUST be instantiated in the main()
 *     
 */


public final class RunManager implements NodeWrapper {

	private EventAction userEventAction;
	private RunAction userRunAction;
	//private EventManager eventManager = new EventManager();
	private boolean initialized = false;
	private Run currentRun;
	private int nRun;
	private int verboseLevel;
	private int debugMode;
	//Number of run connected
	private static final String KEY_COUNTER = "Counter";
	private int nPeriods;
	private Node underlyingNode;
	private Properties properties;
	private Transaction tx;
	
	/**
	 * Declaring the RunManager constructor as a singleton 	 
	 */	
	private RunManager(){
	}

	private static class SingletonHolder { 
		private static final RunManager INSTANCE = new RunManager();
	}

	public static RunManager getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private GraphDatabaseService graphDb;// = new EmbeddedGraphDatabase("/Users/m/storeDir") ;

	/**
	 * Initialise the RunManager, it must be called only once.
	 * 
	 * @param myRunAction set the user's RunAction 
	 * @param myEventAction set the user's EventAction
	 * @param storeDir database's directory
	 */
	public	<myRunAction extends RunAction, myEventAction extends EventAction>  
	void Initialize(
			RunAction myRunAction, 
			EventAction myEventAction, 
			int nPeriods,
			String storeDir){

		DoTest.require(!initialized, "RunManager already initialized");
		this.graphDb =new EmbeddedGraphDatabase(storeDir);

		userRunAction = myRunAction;
		userEventAction = myEventAction;
		tx = this.graphDb.beginTx();
		underlyingNode = this.graphDb.createNode();
		this.nPeriods = nPeriods;
		nRun = 1;
		initialized = true;
	}

	public String getProperty(String prop){
		String o = this.properties.getProperty(prop);
		DoTest.debug(prop+" = "+o);
		return o;
	}
	
	public Properties getProperties(){
		return this.properties;
	}
	
	public void readConfig(String filename) throws InvalidPropertiesFormatException, IOException {

			File file = new File(filename);
			FileInputStream fileInput = new FileInputStream(file);
			this.properties = new Properties();
			this.properties.loadFromXML(fileInput);
			fileInput.close();
/*
			Enumeration<Object> enuKeys = properties.keys();
			while (enuKeys.hasMoreElements()) {
				String key = (String) enuKeys.nextElement();
				String value = properties.getProperty(key);
				DoTest.info(key + ": " + value);
			}
*/			
	}
	
	/**
	 * DoEventLoop Creates and Processes each event
	 * 
	 * @param nEvents The number of events created
	 * @throws IOException 
	 */
	private void DoEventLoop(int nEvents) {


		for(int iEvent=1;iEvent <= nEvents;iEvent++){
			Event currentEvent = currentRun.createEvent(); //The event is automatically linked to the currentRun and with the right Id
			userEventAction.BeginEventAction(currentEvent);
			for(int period = 1; period <= nPeriods; period++){
				userEventAction.StepAction(period, currentEvent);
			}

			userEventAction.EndEventAction(currentEvent);
		}
	}


	private void testInit() throws Exception {
		if(!initialized) throw new Exception("Run Manager not initialized.");
	}
	/**
	 * RunSimulation The actual simulation takes place here.
	 * 
	 * @param nEvents The number of events involved in the simulation
	 * @throws Exception if RunManager has not been initialized.
	 */
	public void RunSimulation(int nEvents) throws Exception {

		this.testInit(); //Throws RMException

		//Create the run
		currentRun = createRun();

		try {
			userRunAction.BeginRunAction();
			if(nEvents>0) DoEventLoop(nEvents);
			userRunAction.EndRunAction();

			this.tx.success();
			nRun++;
		} 
		finally {
			this.tx.finish();
			graphDb.shutdown();
		}
	}

	private Run createRun(){
		Node rn = graphDb.createNode();
		Run run = new Run(rn);
		/*Relationship relationship =*/
		rn.createRelationshipTo(this.underlyingNode, SimRelationship.LINKED);
		run.setId(getNextId());

		//To be deleted:)
		//rn.setProperty("message", "RunNode number "+this.nRun+" created.\n");
		//this.underlyingNode.setProperty("message", "RunManager: got a run ");
		//relationship.setProperty("message", "LINKED!");
		
		//System.out.print(rn.getProperty("message"));
		//System.out.print(this.underlyingNode.getProperty("message"));
		//System.out.print(relationship.getProperty("message"));
		//End TBD
		

		
		return run;

	}

	private synchronized Long getNextId()
	{
		Long counter = null;
		try
		{
			counter = ( Long ) underlyingNode.getProperty( KEY_COUNTER );
		}
		catch ( NotFoundException e )
		{
			// Create a new counter
			counter = 1L;
		}

		underlyingNode.setProperty( KEY_COUNTER, new Long( counter + 1 ) );
		return counter;
	}



	public int getVerboseLevel(){
		return verboseLevel;
	}

	public void setDebugMode(int debugMode) {
		this.debugMode = debugMode;
	}

	public int getDebugMode() {
		return debugMode;
	}

	public GraphDatabaseService getGraphDb() {
		return graphDb;
	}

	public Node getUnderlyingNode() {
		return this.underlyingNode;
	}

}



