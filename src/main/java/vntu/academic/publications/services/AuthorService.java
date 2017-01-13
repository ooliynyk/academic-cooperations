package vntu.academic.publications.services;

import java.util.Collection;

import vntu.academic.publications.dto.AuthorDTO;
import vntu.academic.publications.dto.OrganizationDTO;

public interface AuthorService {
	//Collection<AuthorDTO> fetchAllAuthorsByOrganizationId(String organizationId);

	//Collection<AuthorDTO> fetchAllCoauthorsByOrganizationId(String organizationId);
	
	Collection<AuthorDTO> fetchAllAuthorsWithCoAuthorsByOrganization(OrganizationDTO organizationDTO);
	
}
