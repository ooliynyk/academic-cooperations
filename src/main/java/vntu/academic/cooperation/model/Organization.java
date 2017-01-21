package vntu.academic.cooperation.model;

public class Organization {
	
	private final String id;
	private final String name;

	public Organization(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

}
