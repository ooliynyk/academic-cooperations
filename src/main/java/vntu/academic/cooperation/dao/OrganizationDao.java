package vntu.academic.cooperation.dao;

import vntu.academic.cooperation.model.Organization;

public interface OrganizationDao {

	Organization findOrganizationByName(String organizationName);

	Organization findOrganizationById(String organizationId);

}
