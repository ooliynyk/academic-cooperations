package vntu.academic.cooperation.service;

import vntu.academic.cooperation.dto.OrganizationDTO;

public interface OrganizationService {

	OrganizationDTO fetchOrganizationByName(String organizationName);

	OrganizationDTO fetchOrganizationById(String organizationId);

}
