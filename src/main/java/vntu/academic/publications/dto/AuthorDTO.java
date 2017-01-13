package vntu.academic.publications.dto;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import vntu.academic.publications.entities.Author;

public class AuthorDTO {
	private String id;
	private String name;
	private String organizationId;

	private Set<Author> coauthors = new LinkedHashSet<>();
	
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

	public Collection<Author> getCoauthors() {
		return coauthors;
	}

	public void setCoauthors(Collection<Author> coauthors) {
		this.coauthors = new LinkedHashSet<>(coauthors);
	}

}