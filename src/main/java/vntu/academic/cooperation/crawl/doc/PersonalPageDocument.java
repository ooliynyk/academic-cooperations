package vntu.academic.cooperation.crawl.doc;

import java.util.Collection;
import java.util.Date;

public class PersonalPageDocument {

	private final AuthorDetails authorDetails;
	private final Collection<PublicationDetails> publications;
	private final boolean hasMorePublications;

	public PersonalPageDocument(AuthorDetails authorDetails, Collection<PublicationDetails> publications,
			boolean hasMorePublications) {
		super();
		this.authorDetails = authorDetails;
		this.publications = publications;
		this.hasMorePublications = hasMorePublications;
	}

	public AuthorDetails getAuthorDetails() {
		return authorDetails;
	}

	public Collection<PublicationDetails> getPublications() {
		return publications;
	}

	public boolean hasMorePublications() {
		return hasMorePublications;
	}

	public static class AuthorDetails {

		private final String id;
		private final String name;
		private final String organizationId;

		public AuthorDetails(String id, String name, String organizationId) {
			super();
			this.id = id;
			this.name = name;
			this.organizationId = organizationId;
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

		@Override
		public String toString() {
			return "AuthorDetails [id=" + id + ", name=" + name + ", organizationId=" + organizationId + "]";
		}

	}

	public static class PublicationDetails {

		private final String id;
		private final String title;
		private final Collection<String> authorNames;
		private final Date publicationDate;

		public PublicationDetails(String id, String title, Collection<String> authorNames, Date publicationDate) {
			super();
			this.id = id;
			this.title = title;
			this.authorNames = authorNames;
			this.publicationDate = publicationDate;
		}

		public String getId() {
			return id;
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
			return "PublicationDetails [id=" + id + ", title=" + title + ", authorNames=" + authorNames
					+ ", publicationDate=" + publicationDate + "]";
		}

	}

}
