package vntu.academic.publications.dto;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import vntu.academic.publications.model.Organization;

public class OrganizationDTO {
	private String id;
	private String name;
	private String site;

	private Set<AuthorDTO> authors = new LinkedHashSet<>();
	
	public OrganizationDTO(Organization organization) {
		this.id = organization.getId();
		this.name = organization.getName();
		this.site = organization.getSite();
	}

	public OrganizationDTO() {
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

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public Collection<AuthorDTO> getAuthors() {
		return authors;
	}

	public void setAuthors(Collection<AuthorDTO> authors) {
		this.authors = new LinkedHashSet<AuthorDTO>(authors);
	}

	public void addAuthor(AuthorDTO author) {
		authors.add(author);
	}

}
