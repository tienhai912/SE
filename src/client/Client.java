package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import init_data.stationInNavi;

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
			result = result + obj.get("code") + "\n" + obj.get("name") + "\nStart time:\t"
					+ changeTimeToMinute((String) obj.get("startTime")) + "\nEnd time:\t"
					+ changeTimeToMinute((String) obj.get("endTime"));
			// result = result + "\nWait time: " + changeTimeToMinute((String)
			// obj.get("waitTime")) + "\nFrequency: "
			// + changeTimeToMinute((String) obj.get("frequency")) + "\nTrain
			// count: " + obj.get("trainCount");
			result = result + "\nDistance:\t" + obj.get("distance") + " km\nTotal time:\t"
					+ changeTimeToMinute((String) obj.get("totalTime"));
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
				result = result + obj.get("destName") + "\nNext Train:\t";
				if (obj.get("nextTrain") == null)
					result = result + "Not available";
				else
					result = result + timeRemain((String) obj.get("nextTrain"));
				result = result + "\nFirst Train:\t" + changeTimeToMinute((String) obj.get("firstTrain"))
						+ "\nLast Train:\t" + changeTimeToMinute((String) obj.get("lastTrain")) + "\n";
			}
		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		return result;
	}

	// Navigation
	public String handleRequestNavi(BufferedReader br, ArrayList<stationInNavi> stationsInNavi) {
		String output, result = "";
		try {
			while ((output = br.readLine()) != null) {
				result = result + decodeNavi(output.toString(), stationsInNavi);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// System.out.println(result);
		return result;
	}

	public String decodeNavi(String s, ArrayList<stationInNavi> stationsInNavi) {
		String jsonText = s, result = "";
		JSONParser parser = new JSONParser();

		try {
			JSONObject obj = (JSONObject) parser.parse(jsonText);
			result = result + "Distance:" + obj.get("distance") + "\nFare:\t" + obj.get("fare");
			JSONArray array = (JSONArray) obj.get("route");
			for (int i = 0; i < array.size(); i++) {
				JSONObject obj2 = (JSONObject) array.get(i);
				String str = "", str2 = "";
				str = str + obj2.get("stationId");
				str2 = str2 + obj2.get("isLineChanged");
				if (str2.equals("1")) {
					stationsInNavi.add(new stationInNavi(Integer.parseInt(str), true));
				} else {
					stationsInNavi.add(new stationInNavi(Integer.parseInt(str), false));
				}
			}
		} catch (ParseException pe) {
			System.out.println(pe);
		}
		return result;
	}

	public String changeTimeToMinute(String oldTime) {
		final String OLD_FORMAT = "HH:mm:ss";
		final String NEW_FORMAT = "HH:mm";
		String newTime;

		SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
		Date d = null;
		try {
			d = sdf.parse(oldTime);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		sdf.applyPattern(NEW_FORMAT);
		newTime = sdf.format(d);

		return newTime;
	}

	public String timeRemain(String timeString) {
		final String SECOND_FORMAT = "HH:mm:ss";
		String result = "";
		SimpleDateFormat sdf = new SimpleDateFormat(SECOND_FORMAT);
		Date currentTime = new Date();
		String currentTimeString = sdf.format(currentTime);
		try {
			Date currentTimeSecond = sdf.parse(currentTimeString);
			Date timeSecond = sdf.parse(timeString);
			Long resultSecond = timeSecond.getTime() - currentTimeSecond.getTime();
			// long diffInMinutes = java.time.Duration.between(timeSecond,
			// currentTimeSecond).toMinutes();
			long hour = resultSecond / (60 * 60 * 1000) % 24, minute = resultSecond / (60 * 1000) % 60;
			if (hour < 10)
				result = result + "0";
			result = result + hour + ":";
			if (minute < 10)
				result = result + "0";
			result = result + minute;
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}

		return result;

	}

	public void Slit(String s) {
		String time = s;
		String[] token = time.split(":");
		System.out.println(token[0] + " " + token[1] + " " + token[2]);
	}

}
