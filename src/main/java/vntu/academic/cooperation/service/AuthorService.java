package vntu.academic.cooperation.service;

import java.util.Collection;
import java.util.Date;

import vntu.academic.cooperation.dto.AuthorDTO;
import vntu.academic.cooperation.dto.OrganizationDTO;

public interface AuthorService {

	Collection<AuthorDTO> fetchAllAuthorsWithCoAuthorsByOrganization(OrganizationDTO organizationDTO);

	Collection<AuthorDTO> fetchAllAuthorsWithPublicationsByOrganizationBetweenYears(OrganizationDTO organizationDTO,
			Date fromYear, Date toYear);

	Collection<AuthorDTO> fetchAllAuthorsByOrganization(OrganizationDTO organizationDTO);

	Collection<AuthorDTO> fetchAuthorsByIdentifiers(Collection<String> authorsIdentifiers);

}
