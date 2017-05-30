package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.ParseException;

import init_data.Station;
import normalize.Normalize;

import org.json.simple.parser.JSONParser;

public class Client {
	private CloseableHttpClient httpClient;

	public Client() {
		httpClient = HttpClients.createDefault();

	}

	public void gethttp(String s, int code) {
		try {

			HttpGet getRequest = new HttpGet(s);
			getRequest.addHeader("accept", "application/json");
			HttpResponse response = httpClient.execute(getRequest);
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
			String output;

			httpClient.close();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
				naviStationsId.add(Integer.parseInt((String) obj2.get("stationId")));
			}
		} catch (ParseException pe) {
			System.out.println(pe);
		}
		return result;
	}

	public String decodeArrivalTime1(String s) {
		String jsonText = s, result = "";
		JSONParser parser = new JSONParser();

		ContainerFactory containerFactory = new ContainerFactory() {
			@Override
			public List creatArrayContainer() {
				return new LinkedList();
			}

			@Override
			public Map createObjectContainer() {
				return new LinkedHashMap();
			}
		};

		try {
			Map json = (Map) parser.parse(jsonText, containerFactory);
			Iterator iter = json.entrySet().iterator();

			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				System.out.println(entry.getKey());

				LinkedHashMap map = (LinkedHashMap) entry.getValue();
				System.out.println("firstTrain " + map.get("firstTrain"));
				// Slit((String) map.get("firstTrain"));
				System.out.println("nextTrain " + map.get("nextTrain"));
				// Slit((String) map.get("nextTrain"));
				System.out.println("lastTrain " + map.get("lastTrain"));
				// Slit((String) map.get("lastTrain"));

				result = result + (String) entry.getKey() + "\nFirst Train: " + (String) map.get("firstTrain")
						+ "\nNext Train: " + (String) map.get("nextTrain") + "\nLast Train: "
						+ (String) map.get("lastTrain") + "\n";

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
