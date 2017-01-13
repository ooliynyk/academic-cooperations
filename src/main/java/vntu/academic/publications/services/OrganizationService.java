package vntu.academic.publications.services;

import vntu.academic.publications.dto.OrganizationDTO;

public interface OrganizationService {
	OrganizationDTO fetchOrganizationByName(String organizationName);

	OrganizationDTO fetchOrganizationById(String organizationId);

}
