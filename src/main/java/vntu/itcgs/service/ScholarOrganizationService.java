package vntu.itcgs.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vntu.itcgs.dto.OrganizationDetails;
import vntu.itcgs.model.Organization;
import vntu.itcgs.repository.OrganizationRepository;

@Service
public class ScholarOrganizationService implements OrganizationService {
	private static Logger logger = LoggerFactory.getLogger(ScholarOrganizationService.class);

	private final OrganizationRepository orgRepository;

	@Autowired
	public ScholarOrganizationService(OrganizationRepository orgDAO) {
		this.orgRepository = orgDAO;
	}

	@Override
	public OrganizationDetails fetchOrganizationByName(final String organizationName) {
		logger.info("Fetching organization by name: '{}'", organizationName);
		
		Organization organization = orgRepository.findOrganizationByName(organizationName);
		if (organization == null)
			throw new RuntimeException("Organizatin not found!");

		return new OrganizationDetails(organization);
	}

	@Override
	public OrganizationDetails fetchOrganizationById(final String organizationId) {
		logger.info("Fetching organization by id: '{}'", organizationId);
		
		Organization organization = orgRepository.findOrganizationById(organizationId);
		if (organization == null)
			throw new RuntimeException("Organization not found!");

		return new OrganizationDetails(organization);
	}

}
