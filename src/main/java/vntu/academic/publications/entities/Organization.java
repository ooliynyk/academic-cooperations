package vntu.academic.publications.entities;

public class Organization {
	private final String id;
	private final String name;
	private final String site;

	public Organization(String id, String name, String site) {
		super();
		this.id = id;
		this.name = name;
		this.site = site;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getSite() {
		return site;
	}

}
