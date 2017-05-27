package vntu.academcoop.dto;

import java.util.Collection;
import java.util.Date;

public class PublicationDetails {
	
	private String publicationId;
	private String title;
	private Collection<AuthorDetails> authors;
	private Date publicationDate;

	public String getPublicationId() {
		return publicationId;
	}

	public void setPublicationId(String publicationId) {
		this.publicationId = publicationId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Collection<AuthorDetails> getAuthors() {
		return authors;
	}

	public void setAuthors(Collection<AuthorDetails> authors) {
		this.authors = authors;
	}

	public Date getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(Date publicationDate) {
		this.publicationDate = publicationDate;
	}

}
