package com.amazonaws.lambda.demo;


// lambda imports
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

//java imports for Streams
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;

// json.simple imports for JSON reading and manipulation
import org.json.simple.JSONObject;
import org.json.JSONString;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;




public class LambdaFunctionHandler implements RequestStreamHandler  {
	
	//creating new JSONParser to parse through given JSON later
	JSONParser parser = new JSONParser();

	
	@Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
    	
		// logging which lambda was invoked. This is tracked in the logs on AWS
		LambdaLogger logger = context.getLogger();
		logger.log("parse-json-input-lambda was invoked \n");
		
		
		// Creating buffer reader to read input stream from DynamoDB
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		
		// creating new JSON object to return formatted response
		JSONObject responseJson = new JSONObject();
	
		
		// try to parse the JSON provided by the reader
		try {
			JSONObject event = (JSONObject)parser.parse(reader);
			System.out.println("This is the input Stream: " + event + "\n");
			
			
			/** 
			 * Navigating stream in order to pull out specific values
			 **/
			
			// grabbing name
			if (event.get("name") != null) {
				JSONObject name = (JSONObject) event.get("name");
				Object nameValue = name.get("S");
				System.out.println("Name of Event: " + nameValue);
			}
			
			// grabbing year
			if (event.get("year") != null) {				
				JSONObject year = (JSONObject) event.get("year");   			//converting details to JSONObject
				Object yearTxt = (Object) year.get("N");						// creating new object from nested JSONObject to pull out values
				System.out.println("Year: " + yearTxt);
			}
			
			// grabbing rating
			if (event.get("rating") != null) {
				JSONObject rating = (JSONObject) event.get("rating");
				Object starRating = rating.get("S");
				System.out.println("Rating: " + starRating);
			}
			
			// grabbing fans from nested JSONArray
			if (event.get("fans") != null) {
				JSONObject fans = (JSONObject) event.get("fans");
				JSONArray fansArray = (JSONArray) fans.get("SS");						// creating JSONArray to access the independent strings
				Iterator<String> fanIterator = fansArray.iterator();					// not sure why iterator needs unchecked conversion for iterator<String>
				while (fanIterator.hasNext()) {
					System.out.println("Name of Fan: " + fanIterator.next());
				}					
			}
			
			// creates blank line in output for readability
			System.out.println("\n");
			
			
		} catch (ParseException pex) {
			System.out.println(pex.getMessage());		// catch exception from parser
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());		// catch exception from I/O operations
		}
		
    	 
  

    }
	
	
}


