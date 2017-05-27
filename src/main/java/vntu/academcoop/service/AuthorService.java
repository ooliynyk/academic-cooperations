package vntu.academcoop.service;

import java.util.Collection;
import java.util.Date;

import vntu.academcoop.dto.AuthorDetails;
import vntu.academcoop.dto.OrganizationDetails;

public interface AuthorService {

	Collection<AuthorDetails> fetchAllAuthorsWithCoAuthorsFromOrganization(OrganizationDetails organizationDetails);

	Collection<AuthorDetails> fetchAllAuthorsWithCoAuthorsAndPublicationsBetweenYears(OrganizationDetails organizationDetails,
			Date fromYear, Date toYear);

	Collection<AuthorDetails> fetchAuthorsByIdentifiers(Collection<String> authorsIdentifiers);

}
