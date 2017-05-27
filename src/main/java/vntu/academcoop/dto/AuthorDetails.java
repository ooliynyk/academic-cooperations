package vntu.academcoop.dto;

import java.util.Collection;
import java.util.Collections;

import lombok.Data;
import vntu.academcoop.model.Author;
import vntu.academcoop.model.Publication;

@Data
public class AuthorDetails {

	private String id;
	private String name;
	private String organizationId;

	private Collection<Author> coAuthors = Collections.emptySet();
	private Collection<Publication> publications = Collections.emptySet();

	public AuthorDetails(Author author) {
		this.id = author.getId();
		this.name = author.getName();
		this.organizationId = author.getOrganizationId();
	}

}
