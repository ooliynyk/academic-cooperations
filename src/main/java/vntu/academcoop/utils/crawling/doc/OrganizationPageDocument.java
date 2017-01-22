package vntu.academcoop.utils.crawling.doc;

import java.util.Collection;

import vntu.academcoop.model.Organization;

public class OrganizationPageDocument {

	private final Organization organizationDetails;
	private final Collection<String> authorsIdentifiers;
	private final String nextPageId;

	public OrganizationPageDocument(Organization organizationDetails, Collection<String> authorsIdentifiers,
			String nextPageId) {
		this.organizationDetails = organizationDetails;
		this.authorsIdentifiers = authorsIdentifiers;
		this.nextPageId = nextPageId;
	}

	public Organization getOrganizationDetails() {
		return organizationDetails;
	}

	public Collection<String> getAuthorsIdentifiers() {
		return authorsIdentifiers;
	}

	public String getNextPageId() {
		return nextPageId;
	}

	@Override
	public String toString() {
		return "OrganizationPageDocument [organizationDetails=" + organizationDetails + ", authorsIdentifiers="
				+ authorsIdentifiers + ", nextPageId=" + nextPageId + "]";
	}

}
