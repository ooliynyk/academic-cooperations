package vntu.itcgs.service;

import vntu.itcgs.dto.OrganizationDetails;

public interface OrganizationService {

	OrganizationDetails fetchOrganizationByName(String organizationName);

	OrganizationDetails fetchOrganizationById(String organizationId);

}
