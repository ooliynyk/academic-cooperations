package vntu.academic.publications.service;

import java.util.Collection;
import java.util.Date;

import vntu.academic.publications.dto.AuthorDTO;
import vntu.academic.publications.dto.OrganizationDTO;

public interface AuthorService {

	Collection<AuthorDTO> fetchAllAuthorsWithCoAuthorsByOrganization(OrganizationDTO organizationDTO);

	Collection<AuthorDTO> fetchAllAuthorsWithPublicationsByOrganizationBetweenYears(OrganizationDTO organizationDTO,
			Date fromYear, Date toYear);

	Collection<AuthorDTO> fetchAllAuthorsByOrganization(OrganizationDTO organizationDTO);

	Collection<AuthorDTO> fetchAuthorsByNames(Collection<String> authorsNames);

}
