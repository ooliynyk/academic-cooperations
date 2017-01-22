package vntu.academcoop.dao;

import vntu.academcoop.model.Organization;

public interface OrganizationDao {

	Organization findOrganizationByName(String organizationName);

	Organization findOrganizationById(String organizationId);

}
