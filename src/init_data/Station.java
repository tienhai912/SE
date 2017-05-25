package init_data;

public class Station {
	private int id;
	private String code, name;

	public Station(int id, String code, String name) {
		this.id = id;
		this.code = code;
		this.name = name;
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

}
