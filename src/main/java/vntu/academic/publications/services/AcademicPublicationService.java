package vntu.academic.publications.services;

import vntu.academic.publications.dto.PublicationNetworkDTO;

public interface AcademicPublicationService {
	PublicationNetworkDTO fetchPublicationNetworkByOrganizationName(String organizationName);
	
}
