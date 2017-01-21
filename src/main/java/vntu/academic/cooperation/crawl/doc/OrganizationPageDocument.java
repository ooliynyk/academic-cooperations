package vntu.academic.cooperation.crawl.doc;

import java.util.Collection;

public class OrganizationPageDocument {

	private final OrganizationDetails organizationDetails;
	private final Collection<String> authorsIdentifiers;
	private final String nextPageId;

	public OrganizationPageDocument(OrganizationDetails organizationDetails, Collection<String> authorsIdentifiers,
			String nextPageId) {
		super();
		this.organizationDetails = organizationDetails;
		this.authorsIdentifiers = authorsIdentifiers;
		this.nextPageId = nextPageId;
	}

	public OrganizationDetails getOrganizationDetails() {
		return organizationDetails;
	}

	public Collection<String> getAuthorsIdentifiers() {
		return authorsIdentifiers;
	}

	public String getNextPageId() {
		return nextPageId;
	}

	public static class OrganizationDetails {

		private final String id;
		private final String name;

		public OrganizationDetails(String id, String name) {
			super();
			this.id = id;
			this.name = name;
		}

		public String getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		@Override
		public String toString() {
			return "OrganizationDetails [id=" + id + ", name=" + name + "]";
		}

	}
}
