package vntu.academic.publications.dto;

import java.util.LinkedHashMap;
import java.util.Map;


public class PublicationNetworkDTO {
	private Map<String, OrganizationDTO> organizations = new LinkedHashMap<>();
	private String rootOrganizationId;

	public void setRootOrganization(OrganizationDTO organization) {
		addOrganization(organization);
		rootOrganizationId = organization.getId();
	}

	public void addOrganization(OrganizationDTO organization) {
		final String orgId = organization.getId();

		if (organizations.containsKey(orgId)) {
			OrganizationDTO org = organizations.get(orgId);
			organization.getAuthors().forEach((a) -> org.addAuthor(a));
		} else {
			organizations.put(orgId, organization);
		}
	}

	public Map<String, OrganizationDTO> getOrganizations() {
		return organizations;
	}

	public void setOrganizations(Map<String, OrganizationDTO> organizations) {
		this.organizations = organizations;
	}

	public String getRootOrganizationId() {
		return rootOrganizationId;
	}

}
