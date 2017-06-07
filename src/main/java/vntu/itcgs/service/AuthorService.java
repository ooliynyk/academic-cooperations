package vntu.itcgs.service;

import java.util.Collection;
import java.util.Date;

import vntu.itcgs.dto.AuthorDetails;
import vntu.itcgs.dto.OrganizationDetails;

public interface AuthorService {

    Collection<AuthorDetails> fetchAllAuthorsWithCoAuthorsFromOrganization(OrganizationDetails organizationDetails,
                                                                           boolean coAuthorsVerification);

    Collection<AuthorDetails> fetchAllAuthorsWithCoAuthorsAndPublicationsBetweenYears(
            OrganizationDetails organizationDetails, Date fromYear, Date toYear, boolean coAuthorsVerification);

    Collection<AuthorDetails> fetchAuthorsByIdentifiers(Collection<String> authorsIdentifiers);

}
