package vntu.academic.publications.service;

import java.util.Collection;

import vntu.academic.publications.dto.AuthorDTO;
import vntu.academic.publications.dto.OrganizationDTO;

public interface AuthorService {

	Collection<AuthorDTO> fetchAllAuthorsWithCoAuthorsByOrganization(OrganizationDTO organizationDTO);
	
}
