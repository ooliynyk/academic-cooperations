package vntu.academic.publications.model;

import java.util.Collection;
import java.util.Date;

public class Publication {

	private final String publicationId;
	private final String title;
	private final Collection<String> authorNames;
	private final Date publicationDate;

	public Publication(String publicationId, String title, Collection<String> authorNames, Date publicationDate) {
		this.publicationId = publicationId;
		this.title = title;
		this.authorNames = authorNames;
		this.publicationDate = publicationDate;
	}

	public String getPublicationId() {
		return publicationId;
	}

	public String getTitle() {
		return title;
	}

	public Collection<String> getAuthorNames() {
		return authorNames;
	}

	public Date getPublicationDate() {
		return publicationDate;
	}

	@Override
	public String toString() {
		return "Publication [publicationId=" + publicationId + ", title=" + title + ", authorNames=" + authorNames
				+ ", publicationDate=" + publicationDate + "]";
	}

}
