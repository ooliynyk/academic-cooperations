package vntu.academcoop.dto;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import vntu.academcoop.model.Author;
import vntu.academcoop.model.Publication;

public class AuthorDTO {
	private String id;
	private String name;
	private String organizationId;

	private Set<Author> coAuthors = new LinkedHashSet<>();
	private Set<Publication> publications = new LinkedHashSet<>();

	public AuthorDTO(Author author) {
		this.id = author.getId();
		this.name = author.getName();
		this.organizationId = author.getOrganizationId();
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

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public Collection<Author> getCoAuthors() {
		return coAuthors;
	}

	public void setCoAuthors(Collection<Author> coAuthors) {
		this.coAuthors = new LinkedHashSet<>(coAuthors);
	}

	public Set<Publication> getPublications() {
		return publications;
	}

	public void setPublications(Collection<Publication> publications) {
		this.publications = new LinkedHashSet<>(publications);
	}

}
