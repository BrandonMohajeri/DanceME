package data_classes;

public class ModernMove extends Move {
	private String bodypart;
	private String direction;
	private String plane;
	private String movement_quality;
	private String movement_pathway;
	private String augmentation;
	
	public ModernMove() {
		super("Name", "Modern");
		this.bodypart = "";
		this.direction = "";
		this.plane = "";
		this.movement_quality = "";
		this.movement_pathway = "";
		this.augmentation = "";
	}
	public ModernMove(String name, String image_path,
			String bodypart, String direction, String plane,
			String movement_quality, String movement_pathway,
			String augmentation) {
		super(name, "Modern");
		this.bodypart = bodypart;
		this.direction = direction;
		this.plane = plane;
		this.movement_quality = movement_quality;
		this.movement_pathway = movement_pathway;
		this.augmentation = augmentation;
	}
	@Override
	public long getId() {
		return super.getId();
	}
	public String getBodypart() {
		return bodypart;
	}
	public void setBodypart(String bodypart) {
		this.bodypart = bodypart;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public String getPlane() {
		return plane;
	}
	public void setPlane(String plane) {
		this.plane = plane;
	}
	public String getMovement_quality() {
		return movement_quality;
	}
	public void setMovement_quality(String movement_quality) {
		this.movement_quality = movement_quality;
	}
	public String getMovement_pathway() {
		return movement_pathway;
	}
	public void setMovement_pathway(String movement_pathway) {
		this.movement_pathway = movement_pathway;
	}
	public String getAugmentation() {
		return augmentation;
	}
	public void setAugmentation(String augmentation) {
		this.augmentation = augmentation;
	}
	@Override
	public void setId(long id) {
		super.setId(id);
	}
	@Override
	public String getName() {
		return super.getName();
	}
	@Override
	public void setName(String name) {
		super.setName(name);
	}
	@Override
	public String getCategory() {
		return super.getCategory();
	}
	@Override
	public void setCategory(String category) {
		super.setCategory(category);
	}
	public String toString(){
		return super.toString();
	}
}
