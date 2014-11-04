package tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import json.JSONObject;

public class TestHelpers {
	public static JSONObject getFirebaseSync(String url) throws IOException,
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
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("PATCH");
		OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream());
        osw.write(data);
        osw.flush();
        osw.close();
        //con.getResponseCode();
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
}
