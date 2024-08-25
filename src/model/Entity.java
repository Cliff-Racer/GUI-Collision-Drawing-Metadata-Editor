package model;

public class Entity {
	private String thisName;
	private Drawbox thisDrawbox;
	private Hitbox thisHitbox;
	private Formatter formaterHitbox;
	private Formatter formaterDrawbox;
	private String type;
	
	public Entity(String name,String drawbox,String hitbox) {
		thisName = new String(name);
		thisDrawbox = new Drawbox(drawbox);
		thisHitbox = new Hitbox(hitbox);
	}
	
	public Entity(String name, Hitbox hitbox, Drawbox drawbox) {
		this.thisName = name;
		this.thisHitbox = hitbox;
		this.thisDrawbox = drawbox;
	}
	
	public void setName(String outName) {
		thisName = outName;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	public void setDrawbox(Drawbox outDrawbox) {
		thisDrawbox = outDrawbox;
	};
	
	public void setHitbox(Hitbox outHitbox) {
		thisHitbox = outHitbox;
	};
	
	public void setFormaterHitbox(Formatter outFormaterHitbox) {
		formaterHitbox = outFormaterHitbox;
	};
	
	public void setFormaterDrawbox(Formatter outFormaterDrawbox) {
		formaterDrawbox = outFormaterDrawbox;
	};
	
	public String getName() {
		return thisName;
	};
	
	public Drawbox getDrawbox() {
		return thisDrawbox;
	};
	
	public Hitbox getHitbox() {
		return thisHitbox;
	};
	
	public Formatter getFormaterHitbox() {
		return formaterHitbox;
	};	
	
	public Formatter getFormaterDrawbox() {
		return formaterDrawbox;
	};
	public void PrintEntity() {
		System.out.println("---------------------");
		System.out.println("Name: "+thisName);
		this.thisDrawbox.Print();
		this.thisHitbox.Print();
		System.out.println("---------------------");
	}
}
