package vntu.academic.cooperation.model;

import java.util.Collection;
import java.util.Date;

public class Publication {

	private final String id;
	private final String title;
	private final Collection<String> authorsNames;
	private final Date publicationDate;

	public Publication(String id, String title, Collection<String> authorsNames, Date publicationDate) {
		this.id = id;
		this.title = title;
		this.authorsNames = authorsNames;
		this.publicationDate = publicationDate;
	}

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public Collection<String> getAuthorsNames() {
		return authorsNames;
	}

	public Date getPublicationDate() {
		return publicationDate;
	}

	@Override
	public String toString() {
		return "Publication [id=" + id + ", title=" + title + ", authorsNames=" + authorsNames + ", publicationDate="
				+ publicationDate + "]";
	}

}
