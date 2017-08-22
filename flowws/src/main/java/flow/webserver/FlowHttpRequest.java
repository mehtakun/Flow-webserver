/**
 * FlowHttpRequest - This class represents the Http Request received by the Flow web server.
 * It has properties designed to store the various elements of the http request. Currently
 * only the URI, Method and Version are extracted, rest all is stored in the headers arraylist.
 * This class has methods to read the input stream on the client socket and extract the request
 * information into its member variables
 * 
 * @author kunal mehta
 * @version 0.1
 */

package flow.webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class FlowHttpRequest {

	private static Logger logger = Logger.getLogger(FlowHttpRequest.class);

	String uri;
	String method;
	String version;
	List<String> headers = new ArrayList<String>();

	/**
	 * This constructor reads the input stream from the client socket and extracts the 
	 * http request information.
	 * @param inStream - Input stream from the client socket
	 */
	public FlowHttpRequest(InputStream inStream) {
		try {
			logger.debug("Start of FlowHttpRequest constructor");

			BufferedReader buffIn = new BufferedReader(new InputStreamReader(inStream));

			String strFirstLine = buffIn.readLine();
			String strHeaderLine;
			logger.debug("strFirstLine " + strFirstLine);
			parseRequestLine (strFirstLine);

			while(((strHeaderLine = buffIn.readLine()) != null) && 
					(strHeaderLine.length() > 0)) {
				headers.add(strHeaderLine);
			}
			logger.debug("Method " + method);
			logger.debug("URI " + uri);
			logger.debug("Version " + version);
			logger.debug("Request Headers " + headers);

		} catch (IOException e) {
			logger.error("IOException in FlowHttpRequest constructor " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("Unknown exception in FlowHttpRequest constructor " + e.getMessage());
			e.printStackTrace();
		} 
	}

	/**
	 * This method extracts the method, uri and version from the first line of
	 * the http request. Eg. GET /abc.html HTTP/1.1
	 * Future enhancements - 
	 * 1. Add support for parameters and selectors in the URI, for now any 
	 * 	  request with selectors in the uri will result in 404
	 * @param strURILine - Input stream from the client socket
	 */
	private void parseRequestLine(String strURILine) {
		if (null != strURILine && !strURILine.trim().equals("")) {
			String[] strArrFristLine = strURILine.split("\\s+");
			method = strArrFristLine[0];
			uri = strArrFristLine[1];
			version = strArrFristLine[2];
			
		} else {
			//To-do : handle bad request, for now hard coding to root directory
			method = "GET";
			uri = "/";
			version = "HTTP/1.1";
		}
	}
}
