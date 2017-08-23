# Flow-webserver

Flow webserver

Flow is a basic java based multi threaded webserver.

1.	FlowServer.java – 
a.	This is the master class for the web server which contains the main method.
b.	It starts the server on the port number entered by the user on command prompt when running the class or on the default port 7000
c.	It instantiates a fixed size thread pool for maximum 25 threads
d.	It also opens a client socket and accepts any incoming connections indefinitely, submitting them to the thread pool for implementation. 
e.	To start the server, compile and run this java class.

To Do
a.	Check if the port number is already in use and report accordingly
b.	Compare the benefits or using a cached thread pool instead of a fixed size thread pool
c.	Make use of external configuration files to accept the port number and max thread size

2.	FlowWorker.java – 
a.	This is the worker thread class each instance of which represents the execution of a request received in the Flow Master 
b.	It reads the input request, locates the accessed resource and sends the response back to the client 

3.	FlowHttpRequest.java – 
a.	This class represents a request received by the worker thread.
b.	It reads the input request and parses it to retrieve the individual request elements, viz. URI, Method, Headers and Version.

To Do
a.	Extend support for complex requests with selectors and parameters in the the URL 

4.	FlowHttpResponse.java – 
a.	This class represents the Http Response being sent by the Flow web server.
b.	It sets the response status 1xx, 2xx, 3xx, 4xx or 5xx and the response headers before creating the response body from the accessed resource.
To Do
a.	Enhance to support all response statues
b.	Enhance to support all method types
c.	Enhance to support access to all file types and directory access 
5.	FlowResponseCodes.java
a.	This class has all the supported response code strings and will grow as new response statues are supported

6.	FlowContentTypes.java
a.	This class has all the supported content type strings and will grow as new content types are supported

References -

http://pages.cs.wisc.edu/~dusseau/Classes/CS537-F07/Projects/P2/p2.html

http://www.cs.carleton.edu/faculty/dmusican/cs348/webserver.html

https://www.tutorialspoint.com/http/http_responses.htm

https://stackoverflow.com/questions/3548775/platform-independent-paths-in-java




