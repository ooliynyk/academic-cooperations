package vntu.itcgs.dto;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Data;

@Data
public class CooperationNetwork {

	private Map<String, OrganizationDetails> organizations = new LinkedHashMap<>();
	private String rootOrganizationId;

	public void setRootOrganization(OrganizationDetails organization) {
		addOrganization(organization);
		rootOrganizationId = organization.getId();
	}

	public void addOrganization(OrganizationDetails organization) {
		final String orgId = organization.getId();

		if (!organizations.containsKey(orgId)) {
			organizations.put(orgId, organization);
		} else {
			OrganizationDetails storedOrganization = organizations.get(orgId);
			Integer totalCooperationValue = storedOrganization.getCooperationValue()
					+ organization.getCooperationValue();

			storedOrganization.setCooperationValue(totalCooperationValue);
		}

	}

}
