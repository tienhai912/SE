package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import init_data.Line;
import init_data.Station;

import org.json.simple.parser.JSONParser;

public class Client {
	private CloseableHttpClient httpClient;

	public Client() {
		httpClient = HttpClients.createDefault();

	}

	public BufferedReader handleRequest(String s) {
		BufferedReader br = null;
		try {
			HttpGet getRequest = new HttpGet(s);
			getRequest.addHeader("accept", "application/json");
			HttpResponse response;
			response = httpClient.execute(getRequest);
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			}
			br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return br;

	}

	public void close() {
		try {
			httpClient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Stations

	public void handleRequestStations(BufferedReader br, HashMap<String, Station> nameStations,
			HashMap<Integer, Station> idStations) {
		String output;
		try {
			while ((output = br.readLine()) != null) {
				decodeStations(output.toString(), nameStations, idStations);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void decodeStations(String s, HashMap<String, Station> nameStations, HashMap<Integer, Station> idStations) {
		String jsonText = s;
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(jsonText);
			JSONArray array = (JSONArray) obj;
			for (int i = 0; i < array.size(); i++) {
				JSONObject obj2 = (JSONObject) array.get(i);
				Station newStation = new Station(Integer.parseInt((String) obj2.get("id")), (String) obj2.get("code"),
						(String) obj2.get("name"), (String) obj2.get("shape"), (String) obj2.get("coords"));

				nameStations.put((String) obj2.get("name"), newStation);
				idStations.put(Integer.parseInt((String) obj2.get("id")), newStation);

			}
		} catch (ParseException ex) {
			Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	////// Lines
	public void handleRequestLines(BufferedReader br, HashMap<String, Line> idLines) {
		String output;
		try {
			while ((output = br.readLine()) != null) {
				decodeLines(output.toString(), idLines);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void decodeLines(String s, HashMap<String, Line> idLines) {
		String jsonText = s;
		JSONParser parser = new JSONParser();

		try {
			JSONArray array = (JSONArray) parser.parse(jsonText);
			for (int i = 0; i < array.size(); i++) {
				JSONObject obj = (JSONObject) array.get(i);
				Line newLine = new Line(Integer.parseInt((String) obj.get("id")), (String) obj.get("code"),
						(String) obj.get("name"), (String) obj.get("coords1") + "," + (String) obj.get("coords2"));
				idLines.put((String) obj.get("code"), newLine);
			}
		} catch (ParseException pe) {
			System.out.println(pe);
		}
	}

	////// LineId

	public String handleRequestLineId(BufferedReader br, ArrayList<Integer> infoLineStationsId) {
		String output, result = "";
		try {
			while ((output = br.readLine()) != null) {
				result = result + decodeLineId(output.toString(), infoLineStationsId);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public String decodeLineId(String s, ArrayList<Integer> infoLineStationsId) {
		String jsonText = s, result = "";
		JSONParser parser = new JSONParser();

		try {
			JSONObject obj = (JSONObject) parser.parse(jsonText);
			result = result + obj.get("code") + "\n" + obj.get("name") + "\nStart time: " + obj.get("startTime")
					+ "\nEnd time: " + obj.get("endTime") + "\nWait time: " + obj.get("waitTime") + "\nFrequency: "
					+ obj.get("frequency") + "\nTrain count: " + obj.get("trainCount") + "\nDistance: "
					+ obj.get("distance") + " km\nTotal time: " + obj.get("totalTime");
			JSONArray array = (JSONArray) obj.get("route");
			for (int i = 0; i < array.size(); i++) {
				JSONObject obj2 = (JSONObject) array.get(i);
				String tempId = "" + obj2.get("id");
				infoLineStationsId.add(Integer.parseInt(tempId));
			}
		} catch (ParseException pe) {
			System.out.println(pe);
		}
		return result;
	}

	///// ID

	public void decodeStationId(String s) {
		String jsonText = s;
		JSONParser parser = new JSONParser();

		try {
			Object obj = parser.parse(jsonText);
			JSONObject obj2 = (JSONObject) obj;
			System.out.println("name: " + obj2.get("name") + "\nis mutual " + obj2.get("isMutual"));
			JSONArray array = (JSONArray) obj2.get("line");
			for (int i = 0; i < array.size(); i++) {
				JSONObject obj3 = (JSONObject) array.get(i);
				System.out.println("lineId " + obj3.get("lineId"));
			}
		} catch (ParseException pe) {
			System.out.println(pe);
		}
	}

	/////// Arrival time

	public String handleRequestArrivalTime(BufferedReader br) {
		String output, result = "";
		try {
			while ((output = br.readLine()) != null) {
				result = result + decodeArrivalTime(output.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// System.out.println(result);
		return result;
	}

	public String decodeArrivalTime(String s) {
		String jsonText = s, result = "";
		JSONParser parser = new JSONParser();

		try {
			JSONArray array = (JSONArray) parser.parse(jsonText);
			for (int i = 0; i < array.size(); i++) {
				JSONObject obj = (JSONObject) array.get(i);
				result = result + obj.get("destName") + "\nNext Train: ";
				if (obj.get("nextTrain") == null)
					result = result + "Not available";
				else
					result = result + obj.get("nextTrain");
				result = result + "\nFirst Train: " + obj.get("firstTrain") + "\nLast Train: " + obj.get("lastTrain")
						+ "\n";
			}
		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		return result;
	}

	// Navigation
	public String handleRequestNavi(BufferedReader br, ArrayList<Integer> naviStationsId) {
		String output, result = "";
		try {
			while ((output = br.readLine()) != null) {
				result = result + decodeNavi(output.toString(), naviStationsId);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// System.out.println(result);
		return result;
	}

	public String decodeNavi(String s, ArrayList<Integer> naviStationsId) {
		String jsonText = s, result = "";
		JSONParser parser = new JSONParser();

		try {
			JSONObject obj = (JSONObject) parser.parse(jsonText);
			result = result + "Distance: " + obj.get("distance") + "\nFare: " + obj.get("fare");
			JSONArray array = (JSONArray) obj.get("route");
			for (int i = 0; i < array.size(); i++) {
				JSONObject obj2 = (JSONObject) array.get(i);
				String str = "";
				str = str + obj2.get("stationId");
				naviStationsId.add(Integer.parseInt(str));
			}
		} catch (ParseException pe) {
			System.out.println(pe);
		}
		return result;
	}

	public void Slit(String s) {
		String time = s;
		String[] token = time.split(":");
		System.out.println(token[0] + " " + token[1] + " " + token[2]);
	}

}
