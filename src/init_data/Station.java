package init_data;

import java.util.ArrayList;

public class Station {
	private int id;
	private String code, name;
	private boolean isRectangle;
	private ArrayList<MapPoint> coordinates;

	public Station(int id, String code, String name, String shape, String coord) {
		this.id = id;
		this.code = code;
		this.name = name;
		if (shape.equals("rect"))
			isRectangle = true;
		else
			isRectangle = false;

		coordinates = new ArrayList<MapPoint>();
		slit(coord);

	}

	public void addCoordinate(float x, float y) {
		coordinates.add(new MapPoint(x, y));
	}

	public void slit(String s) {
		String time = s;
		String[] token = time.split(",");
		for (int i = 0; i < token.length; i += 2) {
			addCoordinate(Float.parseFloat(token[i]), Float.parseFloat(token[i + 1]));
		}
	}

	public void out() {
		System.out.print(id + " " + code + " " + name + " " + isRectangle + " ");
		for (MapPoint p : coordinates) {
			System.out.print(p.getX() + " " + p.getY() + " ");
		}
		System.out.println();
	}

	public boolean isInside(float x, float y) {
		if (isRectangle) {
			MapPoint p0 = coordinates.get(0);
			MapPoint p1 = coordinates.get(1);
			if (x > p0.getX() && y > p0.getY() && x < p1.getX() && y < p1.getY())
				return true;
		} else {
			MapPoint p0 = coordinates.get(0);
			MapPoint p2 = coordinates.get(2);
			MapPoint p4 = coordinates.get(4);
			MapPoint p6 = coordinates.get(6);
			if (x > p0.getX() && y > p0.getY() && x < p6.getX() && y < p6.getY())
				return true;
			if (x > p6.getX() && y > p2.getY() && x < p4.getX() && y < p4.getY())
				return true;

		}
		return false;
	}

	// SETTER AND GETTER
	public int getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public boolean isRectangle() {
		return isRectangle;
	}

	public ArrayList<MapPoint> getCoordinates() {
		return coordinates;
	}

}
