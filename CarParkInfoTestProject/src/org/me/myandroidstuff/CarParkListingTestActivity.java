package org.me.myandroidstuff;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CarParkListingTestActivity extends Activity 
{
	private TextView response;
	private TextView errorText;
	private String result, text, holder;
    private String sourceListingURL = "http://open.glasgow.gov.uk/api/live/parking.php?type=xml";
    
	private List<CarPark> carList;
	private CarPark carPark;
	
	ListView listView;

	public List<CarPark> CarList() {
		return carList;
	}
	
	public CarParkListingTestActivity(){
		carList = new ArrayList<CarPark>();
	}

    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);
        
        
        // Get the TextView object on which to display the results
        response = (TextView)findViewById(R.id.urlResponse);
        
        try
        {
        	// Get the data from the RSS stream as a string
        	result =  sourceListingString(sourceListingURL);
        	XML_Retrieve(result);
        	
        	String temps = "";
        	 ArrayList<String> list = new ArrayList<String>();
        	 
        	for (CarPark i : carList) {
        		
        		list.add(i.getCarParkIdentity()+ "\n" +
        				i.getCarParkOccupancy() + " \n" +
						i.getCarParkStatus() + " \n" +
						i.getOccupiedSpaces() + " \n" +
						i.getTotalCapacity() + "\n\n");
        		
        	       
        	
        		
        	}
        	 ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                     android.R.layout.simple_list_item_1,  list);
        	 
             
 	        listView.setAdapter(adapter);
        }
        catch(IOException ae)
        {
        	// Handle error
        	response.setText("Error");
        	// Add error info to log for diagnostics
        	errorText.setText(ae.toString());
        } 
        
    } // End of onCreate
    
    // Method to handle the reading of the data from the XML stream
    private static String sourceListingString(String urlString)throws IOException
    {
	 	String result = "";
    	InputStream anInStream = null;
    	int response = -1;
    	URL url = new URL(urlString);
    	URLConnection conn = url.openConnection();
    	
    	// Check that the connection can be opened
    	if (!(conn instanceof HttpURLConnection))
    			throw new IOException("Not an HTTP connection");
    	try
    	{
    		// Open connection
    		HttpURLConnection httpConn = (HttpURLConnection) conn;
    		httpConn.setAllowUserInteraction(false);
    		httpConn.setInstanceFollowRedirects(true);
    		httpConn.setRequestMethod("GET");
    		httpConn.connect();
    		response = httpConn.getResponseCode();
    		// Check that connection is Ok
    		if (response == HttpURLConnection.HTTP_OK)
    		{
    			// Connection is Ok so open a reader 
    			anInStream = httpConn.getInputStream();
    			InputStreamReader in= new InputStreamReader(anInStream);
    			BufferedReader bin= new BufferedReader(in);
    			
    			// Read in the data from the XML stream
    			String line = new String();
    			while (( (line = bin.readLine())) != null)
    			{
    				result = result + line;
    			}
    		}
    	}
    	catch (Exception ex)
    	{
    			throw new IOException("Error connecting");
    	}
    	
    	// Return result as a string for further processing
    	return result;
    	
    } // End of sourceListingString
    
	public List<CarPark> XML_Retrieve(String dataToParse) {

		try {

			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);

			XmlPullParser xpp = factory.newPullParser();
			xpp.setInput(new StringReader(dataToParse));
			int eventType = xpp.getEventType();
			
			while (eventType != XmlPullParser.END_DOCUMENT) {

				String tagname = xpp.getName();

				switch (eventType) {

				case XmlPullParser.START_TAG:
					if (tagname.equalsIgnoreCase("situation")) {
						carPark = new CarPark();
					}

					break;

				case XmlPullParser.TEXT:
					text = xpp.getText();
					break;

				case XmlPullParser.END_TAG:
					
					if (tagname.equalsIgnoreCase("situation")) {
						carList.add(carPark);
					} else if (tagname.equalsIgnoreCase("carParkIdentity")) {
						carPark.setCarParkIdentity(text);
					} else if (tagname.equalsIgnoreCase("carParkOccupancy")) {
						carPark.setCarParkOccupancy(Integer.parseInt(text));
					} else if (tagname.equalsIgnoreCase("carParkStatus")) {
						carPark.setCarParkStatus(text);
					} else if (tagname.equalsIgnoreCase("occupiedSpaces")) {
						carPark.setOccupiedSpaces(Integer.parseInt(text));
					} else if (xpp.getName().equalsIgnoreCase("totalCapacity")) {
						carPark.setTotalCapacity(Integer.parseInt(text));
					}
					break;

				default:
					break;

				}
				// Get the next event
				eventType = xpp.next();				
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		
		return carList;
	}

    
    
    
} // End of Activity class