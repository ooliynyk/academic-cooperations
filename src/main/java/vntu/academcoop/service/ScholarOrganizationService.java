package vntu.academcoop.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vntu.academcoop.dto.OrganizationDetails;
import vntu.academcoop.model.Organization;
import vntu.academcoop.repository.OrganizationRepository;

@Service
public class ScholarOrganizationService implements OrganizationService {
	private static Logger logger = LoggerFactory.getLogger(ScholarOrganizationService.class);

	private final OrganizationRepository orgDAO;

	@Autowired
	public ScholarOrganizationService(OrganizationRepository orgDAO) {
		this.orgDAO = orgDAO;
	}

	@Override
	public OrganizationDetails fetchOrganizationByName(final String organizationName) {
		logger.info("Fetching organization by name: '{}'", organizationName);
		
		Organization organization = orgDAO.findOrganizationByName(organizationName);
		if (organization == null)
			throw new RuntimeException("Organizatin not found!");

		return new OrganizationDetails(organization);
	}

	@Override
	public OrganizationDetails fetchOrganizationById(final String organizationId) {
		logger.info("Fetching organization by id: '{}'", organizationId);
		
		Organization organization = orgDAO.findOrganizationById(organizationId);
		if (organization == null)
			throw new RuntimeException("Organization not found!");

		return new OrganizationDetails(organization);
	}

}
