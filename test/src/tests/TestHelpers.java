package tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class TestHelpers {
	public static JSONObject getFirebaseSync(String url) throws IOException,
			ProtocolException {
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		HttpResponse response = client.execute(request);
		String json = "";
		// Get the response
		BufferedReader rd = new BufferedReader
		  (new InputStreamReader(response.getEntity().getContent()));
		    
		String line = "";
		while ((line = rd.readLine()) != null) {
		  json += line;
		} 
		return new JSONObject(json.toString());
	}
	
	public static JSONObject getOLDFirebaseSync(String url) throws IOException,
	ProtocolException {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
				String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		return new JSONObject(response.toString());
	}

	public static String getFirstKey(JSONObject json) {
		return json.keySet().toArray()[0].toString();
	}

	public static JSONObject patchFirebaseSync(String url, String data) throws IOException,
			ProtocolException {
		HttpClient client = new DefaultHttpClient();
		HttpPatch request = new HttpPatch(url);
		String json = "";
		StringEntity entity = new StringEntity(data, "UTF-8");
		entity.setContentType("application/json");
		request.setEntity(entity);
		// Get the response
		HttpResponse response = client.execute(request);
		BufferedReader rd = new BufferedReader
		  (new InputStreamReader(response.getEntity().getContent()));
		    
		String line = "";
		while ((line = rd.readLine()) != null) {
		  json += line;
		} 
		return new JSONObject(json.toString());
	}
}
