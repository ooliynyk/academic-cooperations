package vntu.academcoop.model;

public class Author {
	
	private final String id;
	private final String name;
	private final String organizationId;
	private final boolean hasCoAuthors;

	public Author(String id, String name, String organizationId, boolean hasCoAuthors) {
		this.id = id;
		this.name = name;
		this.organizationId = organizationId;
		this.hasCoAuthors = hasCoAuthors;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public boolean hasCoAuthors() {
		return hasCoAuthors;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((organizationId == null) ? 0 : organizationId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Author other = (Author) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (organizationId == null) {
			if (other.organizationId != null)
				return false;
		} else if (!organizationId.equals(other.organizationId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Author [id=" + id + ", name=" + name + ", organizationId=" + organizationId + ", hasCoAuthors="
				+ hasCoAuthors + "]";
	}

}
