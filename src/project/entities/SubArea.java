package project.entities;
/**
 * 
 * @author Kavitha Muthu, Swathi Prasad
 *
 */
public class SubArea {

	private int id;
	private int x1;
	private int x2;
	private int y1;
	private int y2;
	private String title;
	private boolean fireSensor;
	private boolean burglarySensor;
	
	/**
	 * 
	 * @param id  id of respective sub areas
	 * @param title	 title of each sub areas
	 * @param fireSensor  flag to indicate fire sensor's presence
	 * @param burglarySensor  flag to indicate burglary sensor's presence
	 */
	public SubArea(int id, String title, boolean fireSensor, boolean burglarySensor) {
		super();
		this.id = id;
		this.title=title;
		this.fireSensor = fireSensor;
		this.burglarySensor = burglarySensor;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public SubArea(){}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isFireSensor() {
		return fireSensor;
	}

	public void setFireSensor(boolean fireSensor) {
		this.fireSensor = fireSensor;
	}

	public boolean isBurglarySensor() {
		return burglarySensor;
	}

	public void setBurglarySensor(boolean burglarySensor) {
		this.burglarySensor = burglarySensor;
	}
	
}

