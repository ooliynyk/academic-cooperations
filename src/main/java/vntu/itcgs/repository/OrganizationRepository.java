package vntu.itcgs.repository;

import vntu.itcgs.model.Organization;

public interface OrganizationRepository {

	Organization findOrganizationByName(String organizationName);

	Organization findOrganizationById(String organizationId);

}
