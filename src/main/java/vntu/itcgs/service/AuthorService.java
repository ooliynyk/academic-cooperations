package vntu.itcgs.service;

import java.util.Collection;
import java.util.Date;

import vntu.itcgs.dto.AuthorDetails;
import vntu.itcgs.dto.OrganizationDetails;

public interface AuthorService {

	Collection<AuthorDetails> fetchAllAuthorsWithCoAuthorsFromOrganization(OrganizationDetails organizationDetails);

	Collection<AuthorDetails> fetchAllAuthorsWithCoAuthorsAndPublicationsBetweenYears(OrganizationDetails organizationDetails,
			Date fromYear, Date toYear);

	Collection<AuthorDetails> fetchAuthorsByIdentifiers(Collection<String> authorsIdentifiers);

}
