package vntu.academcoop.repository;

import vntu.academcoop.model.Organization;

public interface OrganizationRepository {

	Organization findOrganizationByName(String organizationName);

	Organization findOrganizationById(String organizationId);

}
