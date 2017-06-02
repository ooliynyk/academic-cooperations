package vntu.itcgs.repository;

import vntu.itcgs.model.Organization;
import vntu.itcgs.utils.crawling.DocumentParsingException;

public interface OrganizationRepository {

	Organization findOrganizationByName(String organizationName) throws DocumentParsingException;

	Organization findOrganizationById(String organizationId);

}
