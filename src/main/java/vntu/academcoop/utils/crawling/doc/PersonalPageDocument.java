package vntu.academcoop.utils.crawling.doc;

import java.util.Collection;

import vntu.academcoop.model.Author;
import vntu.academcoop.model.Publication;

public class PersonalPageDocument {

	private final Author authorDetails;
	private final Collection<Publication> publications;
	private final boolean hasMorePublications;

	public PersonalPageDocument(Author authorDetails, Collection<Publication> publications,
			boolean hasMorePublications) {
		super();
		this.authorDetails = authorDetails;
		this.publications = publications;
		this.hasMorePublications = hasMorePublications;
	}

	public Author getAuthorDetails() {
		return authorDetails;
	}

	public Collection<Publication> getPublications() {
		return publications;
	}

	public boolean hasMorePublications() {
		return hasMorePublications;
	}

}
