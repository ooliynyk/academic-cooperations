package vntu.academcoop.service;

import vntu.academcoop.dto.OrganizationDetails;

public interface OrganizationService {

	OrganizationDetails fetchOrganizationByName(String organizationName);

	OrganizationDetails fetchOrganizationById(String organizationId);

}
