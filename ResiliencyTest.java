
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import gatewayController.ReadPython;
import gatewayController.Requests;


public class ResiliencyTest {
	
	public static void main(String[] args) throws IOException, ParseException {

		char choice = '$';
		do {
			showMenu();
			choice = getUserSelection();
			switch(choice)
			{
			case '1': menuInitializeValidGatewayId();
			break;
			case '2': menuInitializeInvalidGatewayId();
			break;
			case '3': menuPostHeartbeatWithValidGatewayIdAndValidToken();
			break;
			case '4': menuPostHeartbeatWithValidGatewayIdAndInvalidToken();
			break;
			case '5': menuPostHeartbeatWithInvalidGatewayAndValidToken();
			break;
			case '6': menuPostHeartbeatWithInvalidGatewayAndInvalidToken();
			break;
			case '7': menuRunDiagnostics();
			break;
			case '8': System.out.println("now exiting....");
			break;
			default: System.out.println("Invalid choice, please choose a valid menu option (1-8)");
			}
		} while (choice != '8');
		System.out.println("Resiliency Test is now closed. ");
	}
	
	public static void showMenu()
	{
		System.out.println("\n1. initialize valid gateway id. \n"+
				"2. initialize invalid gateway id. \n"+
				"3. post heartbeat with valid gateway id and valid token.\n"+
				"4. post heartbeat with valid gateway id and invalid token. \n"+
				"5. post heartbeat with invalid gateway id and valid token.\n"+
				"6. post heartbeat with invalid gateway id and invalid token.\n"+
				"7. Run diagnostic.\n"+
				"8. quit.\n");
	}
	
	
	private static char getUserSelection() {
		Scanner input = new Scanner (System.in);
		System.out.print("Enter your menu selection: ");
		String line = input.nextLine();
		char selection='$';
		boolean check=true;
		while(check )
		{
			if(line.length() == 0)
			{
				System.out.print("You must choose a menu selection:  1-8 ");
				line = input.nextLine();
			}
			else
			{
				selection=line.charAt(0);	
				if(selection<'1' || selection > '8')
				{
					System.out.print("You must choose a menu selection: 1-8 please re-enter");
					line = input.nextLine();
				}	
				else
				{
					check=false;
				}
			}
		}
		return selection;
	}
	
	private static String validGWID = "5c10868ed4a607255b9d2e35" ;
	private static String invalidGWID = "5bf08ff7a1987";
	private static String validToken = "batool@stedwards.edu" ;
	private static String invalidToken = "123" ;
	////////////
	private static void menuInitializeValidGatewayId()
	{
		//System.out.print("initialize valid gateway id ");
		
		JSONObject jsonObjectForToken=null;
		File file = new File("gatewayID.txt");
		JSONObject jsonObject=null;
		String response="";
		JSONObject urlParametersJson = new JSONObject();
        String gwId="5c10868ed4a607255b9d2e35";
        urlParametersJson.put("GatewayId",gwId);
        String url = "https://team12.app.softwareengineeringii.com/api/gateway/newGateway";
        
        try {
	            response = Requests.sendPost(url, urlParametersJson);
//	            //System.out.println("response"+response);
//	    			JSONParser jsonParser = new JSONParser();
//	           jsonObject = (JSONObject) jsonParser.parse(response);
//	           jsonObjectForToken = (JSONObject) jsonObject.get("Gateway");
//	           if(jsonObjectForToken!=null)
//	           {
//	           		gatewayController.MainEventLoop.writeToFileGwIdAndToken(gwId,jsonObjectForToken);
//	           }
//	           if(jsonObjectForToken==null)
//	           {
//	           		System.out.println("--Error-- Gateway is not found!! ");
//	           }
           
       } catch (Exception e) {

           e.printStackTrace();
       }
       System.out.println("\nresponse for initializing GW "+response);
	}
	////////////
	private static void menuInitializeInvalidGatewayId()
	{
		//System.out.print("initialize invalid gateway id ");
		
		JSONObject jsonObjectForToken=null;
		File file = new File("gatewayID.txt");
		JSONObject jsonObject=null;
		String response="";
		JSONObject urlParametersJson = new JSONObject();
        String gwId="5bf08ff7a19877";
        urlParametersJson.put("GatewayId",gwId);
        String url = "https://team12.app.softwareengineeringii.com/api/gateway/newGateway";
        
        try {
	            response = Requests.sendPost(url, urlParametersJson);
         
       } catch (Exception e) {

           e.printStackTrace();
       }
       System.out.println("\nresponse for initializing GW "+response);
	}
	////////////
	private static void menuPostHeartbeatWithValidGatewayIdAndValidToken()
	{
		
		JSONObject urlParametersJson = null;
		try
		{
			urlParametersJson=ReadPython.readPython();
		} catch (IOException | ParseException e1)
		{
			e1.printStackTrace();
		}
		//System.out.println(urlParametersJson.toString());
		String url="";
		url = "https://team12.app.softwareengineeringii.com/api/gateway/heartbeat/"+validGWID;

		try
        {
			String response = Requests.sendPost(url, urlParametersJson);
			System.out.println("response for heartbeat"+response.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	///////////
	public static JSONObject readPythonWithWrongToken() throws IOException, ParseException 
	{
		JSONObject json = new JSONObject();
		json.put("GatewayId",validGWID);	
		Process pDiagnostics = Runtime.getRuntime().exec("python /Users/batoolalsmael/Desktop/SW#2/gateway/timestamp.py");
		BufferedReader inDiagnostics = new BufferedReader(new InputStreamReader(pDiagnostics.getInputStream()));
		json.put("TimeStamp",inDiagnostics.readLine());
		json.put("Token",invalidToken);
		//System.out.println("readPythonWithWrongToken() "+json.toString());
		return json;
	}
	private static void menuPostHeartbeatWithValidGatewayIdAndInvalidToken() throws IOException, ParseException
	{
		//System.out.print("post heartbeat with valid gateway id and invalid token ");
		JSONObject urlParametersJson = null;
		try
		{
			urlParametersJson=readPythonWithWrongToken();
		} catch (IOException | ParseException e1)
		{
			e1.printStackTrace();
		}
		String url = "https://team12.app.softwareengineeringii.com/api/gateway/heartbeat/"+validGWID;

		try
        {
			String response = Requests.sendPost(url, urlParametersJson);
			System.out.println("response for heartbeat"+response.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	///////////
	private static JSONObject readPythonWithWrongId() throws IOException, ParseException 
	{
		JSONObject json = new JSONObject();
		json.put("GatewayId",invalidGWID);	
		Process pDiagnostics = Runtime.getRuntime().exec("python /Users/batoolalsmael/Desktop/SW#2/gateway/timestamp.py");
		BufferedReader inDiagnostics = new BufferedReader(new InputStreamReader(pDiagnostics.getInputStream()));
		json.put("TimeStamp",inDiagnostics.readLine());
		json.put("Token",validToken);
		return json;
	}
	private static void menuPostHeartbeatWithInvalidGatewayAndValidToken() throws IOException, ParseException
	{
		
		
		JSONObject urlParametersJson = null;
		try
		{
			urlParametersJson=readPythonWithWrongId();
		} catch (IOException | ParseException e1)
		{
			e1.printStackTrace();
		}
		String url = "https://team12.app.softwareengineeringii.com/api/gateway/heartbeat/"+invalidGWID;

		try
        {
			String response = Requests.sendPost(url, urlParametersJson);
			System.out.println("response for heartbeat"+response.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static JSONObject readPythonWithWrongIdAndToken() throws IOException, ParseException 
	{
		
		JSONObject json = new JSONObject();
		json.put("GatewayId",invalidGWID);	
		Process pDiagnostics = Runtime.getRuntime().exec("python /Users/batoolalsmael/Desktop/SW#2/gateway/timestamp.py");
		BufferedReader inDiagnostics = new BufferedReader(new InputStreamReader(pDiagnostics.getInputStream()));
		json.put("TimeStamp",inDiagnostics.readLine());
		json.put("Token",invalidToken);
		
		
		return json;
	}
	private static void menuPostHeartbeatWithInvalidGatewayAndInvalidToken() throws IOException, ParseException
	{
		JSONObject urlParametersJson = null;
		try
		{
			urlParametersJson=readPythonWithWrongIdAndToken();
		} catch (IOException | ParseException e1)
		{
			e1.printStackTrace();
		}
		String url = "https://team12.app.softwareengineeringii.com/api/gateway/heartbeat/"+invalidGWID;

		try
        {
			String response = Requests.sendPost(url, urlParametersJson);
			System.out.println("response for heartbeat"+response.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//////////
	@SuppressWarnings("unchecked")
	public static JSONObject readPython() throws IOException, ParseException 
	{
		
		JSONObject json = new JSONObject();
		json.put("GatewayId",readGatewayControllerID());	
		Process pDiagnostics = Runtime.getRuntime().exec("python /Users/batoolalsmael/Desktop/SW#2_old/gateway/timestamp.py");
		BufferedReader inDiagnostics = new BufferedReader(new InputStreamReader(pDiagnostics.getInputStream()));
		json.put("TimeStamp",inDiagnostics.readLine());
		json.put("Token",Requests.readAccessToken());
		
		
		return json;
	}
	
	public static  String readGatewayControllerID()
	{
		String filename = "gatewayID.txt";
		Scanner inputFile = null;
		String gatewayID="";
		
		try {
			inputFile = new Scanner(new File(filename));
		
		}catch(FileNotFoundException e) {
			System.out.println("FAILURE cannot open file: " + filename + " for input" +
					" EXIT ON FAILURE TO OPEN FILE.");
			System.exit(0);
		}
	
		if(inputFile.hasNext())
		{
			gatewayID = inputFile.next();
		}	
		inputFile.close();
		return gatewayID;
	}
	private static void menuRunDiagnostics() throws IOException, ParseException
	{
		System.out.println(readPython().toString());
	}
	

}
