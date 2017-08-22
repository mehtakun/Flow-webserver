/**
 * FlowHttpRequest - This class represents the Http Response being sent by the Flow web server.
 * It has properties designed to store the various elements of the http response. Currently the
 * implementation is limited to sending only a 404 or 200 response code, but will in future be enhanced
 * to cover all 1xx, 2xx, 3xx, 4xx and 5xx responses. Also the content type is currently hard-coded to
 * text/html. This will be enhanced in the future to handle requests for all file types
 * 
 * Future enhancements -
 * 1. Add support for all response statues 
 * 2. Add support to handle different method types
 * 3. Add support to handle directory access
 * @author kunal mehta
 * @version 0.1
 */
package flow.webserver;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.log4j.Logger;

public class FlowHttpResponse {

	private static Logger logger = Logger.getLogger(FlowHttpResponse.class);
	final static String STR_CRLF = "\r\n";
	final static String STR_DOCROOT = "./src/main/resources/docroot";
	byte[] arrResponse;
	String strResponseCode;
	String strHeaders;
	File file = null;

	/**
	 * This constructor locates the accessed resource and generates a response.
	 * @param request - Request received
	 */
	public FlowHttpResponse(FlowHttpRequest request) {
		file = new File(STR_DOCROOT+request.uri);
		setResponseCode();
		setHeaders();
		
		logger.debug("Preparing response for - " + file.getPath());
		logger.debug("Response Code - " + strResponseCode);
		logger.debug("Response Headers - " + strHeaders);

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			//Reading the contents for requested file and setting in byte array
			int length = (int) file.length();
			arrResponse = new byte[length];
			int offset = 0;
			while (offset < length) {
				int count = fis.read(arrResponse, offset, (length - offset));
				offset += count;
			}
	        
		} catch (FileNotFoundException e) {
			logger.error("FileNotFoundException while reading requested resource from filesystem " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("IOException while reading requested resource from filesystem " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("Exception while reading requested resource from filesystem " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				logger.error("Exception while closing file input stream " + e.getMessage());
				fis = null;
				e.printStackTrace();
			}
		}
	}

	/**
	 * This method builds the response and sends it to the output stream
	 * Future enhancements - 
	 */
	public void sendResponse(OutputStream outputStream) {
		DataOutputStream output = new DataOutputStream(outputStream);
		if (arrResponse != null) {
			try {
				logger.debug("Writing output ");
				output.writeBytes(strResponseCode);
				output.writeBytes(STR_CRLF);
				output.writeBytes(strHeaders);
				output.writeBytes(STR_CRLF);
				output.writeBytes(STR_CRLF);
				output.write(arrResponse);
				output.flush();
				logger.debug("Output written");
				
			} catch (IOException e) {
				logger.error("IOException while writing to output stream " + e.getMessage());
				e.printStackTrace();
			} catch (Exception e) {
				logger.error("Exception while writing to output stream " + e.getMessage());
				e.printStackTrace();
			} finally {
				try {
					output.close();
				} catch (Exception e) {
					logger.error("Exception while closing output writer stream " + e.getMessage());
					output = null;
					e.printStackTrace();
				}
			}
		}

	}
	
	/**
	 * This method sets the response status 1xx, 2xx, 3xx, 4xx or 5xx
	 * Future enhancements - 
	 * 1. Add support for response statuses other than 200 and 404
	 * 2. Add support for access to directories or root level /
	 */

	private void setResponseCode () {
		if (!file.exists() || file.isDirectory()) {
			file = new File(STR_DOCROOT+"/404.html");
			strResponseCode = FlowResponseCodes.STATUS_404;
		} else {
			strResponseCode = FlowResponseCodes.STATUS_200;
		}
	}
	
	/**
	 * This method sets the response headers, for now only the content type is set
	 * Future enhancements - 
	 * 1. Add support for various other response headers
	 */
	private void setHeaders () {
		//Hardcoding the content type to text/html for now
		strHeaders = FlowContentTypes.TYPE_TEXT_HTML; 
	}

}
