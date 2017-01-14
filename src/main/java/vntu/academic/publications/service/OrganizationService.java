package vntu.academic.publications.service;

import vntu.academic.publications.dto.OrganizationDTO;

public interface OrganizationService {

	OrganizationDTO fetchOrganizationByName(String organizationName);

	OrganizationDTO fetchOrganizationById(String organizationId);

}
