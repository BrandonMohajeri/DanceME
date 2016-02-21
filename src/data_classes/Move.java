package data_classes;

public abstract class Move {
	private long id; //For database navigation
	private String name;
	private String category; //not used

	public Move(){
		this.id = 0;
		this.name = "";
		this.category = "";
	}
	
	public Move(String name, String category) {
//		long id = Math.abs(new Random().nextLong());
//		this.id = id;
		this.name = name;
		this.category = category;
	}

	protected long getId() {
		return id;
	}

	protected void setId(long id) {
		this.id = id;
	}

	protected String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	protected String getCategory() {
		return category;
	}

	protected void setCategory(String category) {
		this.category = category;
	}
	public String toString(){
		return this.name;
	}
}
