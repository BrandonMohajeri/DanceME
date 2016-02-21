package data_classes;

import java.util.ArrayList;
import java.util.List;

public class Dance {
	private String id;
	private String name;
	private String category; //not used
	private String difficulty; // not used
	private List<TapMove> moves;
//	private List<String> how_to; //Strings for picture resources
	
	public Dance(){
		this.id = "";
		this.name = "";
		this.category = "";
		this.difficulty = "";
		this.moves = new ArrayList<TapMove>();
	}
	
	public Dance(String id, String name, String category, String difficulty,
			List<TapMove> moves){//, List<String> how_to) {
		super();
		this.id = id;
		this.name = name;
		this.category = category;
		this.difficulty = difficulty;
		this.moves = moves;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getDifficulty() {
		return difficulty;
	}
	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}
	public List<TapMove> getMoves() {
		return moves;
	}
	public void setMoves(List<TapMove> moves) {
		this.moves = moves;
	}
	public void addMove(TapMove m){
		moves.add(m);
	}
	@Override
	public String toString() {
		return getName();
	}
}
