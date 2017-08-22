/**
 * FlowServer - This class contains the main method for execute the 
 * Flow Multi-Threaded Web Server. It creates a primary thread that
 * listens for connections on the user entered port number or the 
 * default port number 7000 if there is no input
 * 
 * Future enhancements - 
 * 1. Implement a cached thread pool for better resource usage
 * 2. Implement a mechanism to queue the incoming requests in a buffer
 * 
 * @author kunal mehta
 * @version 0.1
 */

package flow.webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

public class FlowServer {

	private static final Logger logger = Logger.getLogger(FlowServer.class);
	private static final int DEFAULT_PORT = 7000; 


	/** 
	 * This is the main method, it does the following -
	 * 1. Accepts the port number and validates it, defaults to 7000 if not valid
	 * 2. Instantiates a server side socket that listens on the above port number
	 * 3. Instantiates a fixed size thread pool
	 * 4. Accepts client connection and submits it to the thread pool for execution
	 * 
	 * @param args String array of arguments to the main method. First argument should
	 * 			   be the port number (0 < port number < 65535)
	 */
	public static void main(String[] args) {

		logger.debug("**** Start of the main method ****");
		int port;

		ServerSocket flowServer = null;
		Socket flowClient = null;
		ExecutorService executor = null; 

		try {
			if (args.length > 0) {
				port = isValidPort (args[0]);
			} else {
				port = DEFAULT_PORT;
			}

			//Creating a server socket to listen to port 7000 
			flowServer = new ServerSocket (port);

			logger.debug("Server started on port " + port);

			
			//Creating a fixed size thread pool
			executor = Executors.newFixedThreadPool(25);

			while (true) {
				//Accepting connection request from client socket
				flowClient = flowServer.accept();

				//and submitting it to the thread pool for execution
				FlowWorker worker = new FlowWorker(flowClient);
				executor.submit (worker);

				/* This is for a single threaded implementation 
				 * FlowWorker workerThread = new FlowWorker(flowClient);
				Thread thread = new Thread(workerThread);
				thread.start();*/

			}
		} catch (UnknownHostException e) {
			logger.error("UnknownHostException in main method " + e.getMessage());
			e.printStackTrace(); //Stack traces to be removed for production readiness
		} catch (IOException e) {
			logger.error("IOException in main method " + e.getMessage());
			e.printStackTrace();
		} catch (NumberFormatException e) {
			logger.error("Invalid port number in main method " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("Unknown exception in main method " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * This method checks if the port number is a valid port number
	 * @param strPort - String representation of the port number
	 * @return int - int representation of the port number if the number is between 0 & 65535
	 * @throws NumberFormatException - Thrown in case the port number is not valid
	 */
	private static int isValidPort (String strPort) throws NumberFormatException {
		try {
			int port = Integer.parseInt(strPort);
			if (port > 0 && port < 65535) {
				return port;
			} else {
				throw new NumberFormatException("Invalid port number");
			}
		} catch (Exception e) {
			throw new NumberFormatException("Invalid port number");
		}
	}
}
