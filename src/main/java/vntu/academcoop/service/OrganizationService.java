package vntu.academcoop.service;

import vntu.academcoop.dto.OrganizationDTO;

public interface OrganizationService {

	OrganizationDTO fetchOrganizationByName(String organizationName);

	OrganizationDTO fetchOrganizationById(String organizationId);

}
