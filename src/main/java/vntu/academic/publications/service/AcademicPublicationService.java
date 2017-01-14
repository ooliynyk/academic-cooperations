package vntu.academic.publications.service;

import vntu.academic.publications.dto.PublicationNetworkDTO;

public interface AcademicPublicationService {
	
	PublicationNetworkDTO fetchPublicationNetworkByOrganizationName(String organizationName);
	
}
