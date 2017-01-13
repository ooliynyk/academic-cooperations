package vntu.academic.publications.dao;

import vntu.academic.publications.entities.Organization;

public interface OrganizationDao {
	Organization findOrganizationByName(String organizationName);

	Organization findOrganizationById(String organizationId);
}
