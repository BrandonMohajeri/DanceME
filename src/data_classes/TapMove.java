package data_classes;

public class TapMove extends Move {
	private int picture;

	public TapMove(){
		super("NAME", "Tap");
		this.picture = 0;
	}
	
	public TapMove(String name, int pic) {
		super(name, "Tap");
		this.picture = pic;
	}

	public int getFilePath() {
		return picture;
	}

	public void setFilePath(int picture) {
		this.picture = picture;
	}
	
	public String getName(){
		return super.getName();
	}
	
	public void setName(String name){
		super.setName(name);
	}
	
	public long getId(){
		return super.getId();
	}
	
	public void setId(long id){
		super.setId(id);
	}
	
	public String getCategory(){
		return super.getCategory();
	}
}
