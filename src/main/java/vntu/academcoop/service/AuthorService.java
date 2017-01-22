package vntu.academcoop.service;

import java.util.Collection;
import java.util.Date;

import vntu.academcoop.dto.AuthorDTO;
import vntu.academcoop.dto.OrganizationDTO;

public interface AuthorService {

	Collection<AuthorDTO> fetchAllAuthorsWithCoAuthorsFromOrganization(OrganizationDTO organizationDTO);

	Collection<AuthorDTO> fetchAllAuthorsWithCoAuthorsAndPublicationsBetweenYears(OrganizationDTO organizationDTO,
			Date fromYear, Date toYear);

	Collection<AuthorDTO> fetchAuthorsByIdentifiers(Collection<String> authorsIdentifiers);

}
