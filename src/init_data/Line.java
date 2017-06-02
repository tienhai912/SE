package init_data;

import java.util.ArrayList;

public class Line {
	private int id;
	private String code, name;
	private ArrayList<MapPoint> coordinates;
	private float radius;

	public Line(int id, String code, String name, String coord) {
		this.id = id;
		this.code = code;
		this.name = name;
		radius = 42F;
		coordinates = new ArrayList<MapPoint>();
		slit(coord);

	}

	public void addCoordinate(float x, float y) {
		coordinates.add(new MapPoint(x, y));
	}

	public void slit(String s) {
		String time = s;
		String[] token = time.split(",");
		for (int i = 0; i < token.length; i += 3) {
			addCoordinate(Float.parseFloat(token[i]), Float.parseFloat(token[i + 1]));
		}
	}

	public void out() {
		System.out.print(id + " " + code + " " + name + " " + radius + " ");
		for (MapPoint p : coordinates) {
			System.out.print(p.getX() + " " + p.getY() + " ");
		}
		System.out.println();
	}

	public boolean isIn(float x, float y, float x2, float y2) {
		if (Math.sqrt((x - x2) * (x - x2) + (y - y2) * (y - y2)) <= 42F) {
			return true;
		}
		return false;

	}

	public boolean isInside(float x, float y) {
		MapPoint p0 = coordinates.get(0);
		MapPoint p1 = coordinates.get(1);
		if (isIn(x, y, p0.getX(), p0.getY()) || isIn(x, y, p1.getX(), p1.getY()))
			return true;

		return false;
	}

	// GETTER AND SETTER

	public int getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

}
