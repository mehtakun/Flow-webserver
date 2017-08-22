/**
 * FlowWorker - This class represents the worker thread, each instance of which
 * represents the execution of a request received in the Flow Master class.
 * @author kunal mehta
 * @version 0.1
 */

package flow.webserver;

import java.io.IOException;
import java.net.Socket;

import org.apache.log4j.Logger;

public class FlowWorker implements Runnable {

	
	private Socket clientSocket;
	final Logger logger = Logger.getLogger(FlowWorker.class);
	
	/**
	 * This constructor instantiates the local clientSocket variable with the
	 * Socket variable sent from the calling main method.
	 */
	public FlowWorker(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	/**
	 * Run method is called each time an instance of this worker thread is 
	 * submitted to the  thread pool. This method calls the methods to process
	 * the request and prepare/send the response.
	 */
	public void run() {
		try {
			logger.debug("**** Start of worker thread ****");
			FlowHttpRequest request = new FlowHttpRequest (clientSocket.getInputStream());
			FlowHttpResponse response = new FlowHttpResponse(request);
			response.sendResponse (clientSocket.getOutputStream());
			clientSocket.close();
		} catch (IOException e) {
			logger.error("IOException exception in worker thread " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("Unknown exception in worker thread " + e.getMessage());
			e.printStackTrace();
		}
	}
}
