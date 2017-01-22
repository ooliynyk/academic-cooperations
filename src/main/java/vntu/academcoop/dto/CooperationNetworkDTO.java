package vntu.academcoop.dto;

import java.util.LinkedHashMap;
import java.util.Map;

public class CooperationNetworkDTO {
	private Map<String, OrganizationDTO> organizations = new LinkedHashMap<>();
	private String rootOrganizationId;

	public void setRootOrganization(OrganizationDTO organization) {
		addOrganization(organization);
		rootOrganizationId = organization.getId();
	}

	public void addOrganization(OrganizationDTO organization) {
		final String orgId = organization.getId();

		if (!organizations.containsKey(orgId)) {
			organizations.put(orgId, organization);
		} else {
			OrganizationDTO storedOrganization = organizations.get(orgId);

			int totalCooperationValue = storedOrganization.getCooperationValue() + organization.getCooperationValue();

			storedOrganization.setCooperationValue(totalCooperationValue);
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
