package init_data;

public class stationInNavi {
	private int stationId;
	private boolean changeLine;

	public stationInNavi(int stationId, boolean changeLine) {
		this.stationId = stationId;
		this.changeLine = changeLine;
	}
	
	// GETTER AND SETTER
	public int getStationId() {
		return stationId;
	}

	public boolean isChangeLine() {
		return changeLine;
	}

	
}
