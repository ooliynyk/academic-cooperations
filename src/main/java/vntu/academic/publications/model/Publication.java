package vntu.academic.publications.model;

import java.util.Date;
import java.util.List;

public class Publication {

	private final String publicationId;
	private final String title;
	private final List<String> authorNames;
	private final Date publicationDate;

	public Publication(String publicationId, String title, List<String> authorNames, Date publicationDate) {
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

	public List<String> getAuthorNames() {
		return authorNames;
	}

	public Date getPublicationDate() {
		return publicationDate;
	}

}
