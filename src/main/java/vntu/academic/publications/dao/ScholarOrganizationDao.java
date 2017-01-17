package vntu.academic.publications.dao;

import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import vntu.academic.publications.crawl.parser.AuthorsOnPageByOrganizationDocumentParser;
import vntu.academic.publications.crawl.parser.DocumentParserProvider;
import vntu.academic.publications.crawl.parser.SearchOrganizationByNameDocumentParser;
import vntu.academic.publications.model.Organization;

@Repository
public class ScholarOrganizationDao implements OrganizationDao {

	private static final Logger logger = LoggerFactory.getLogger(ScholarOrganizationDao.class);

	@Autowired
	private DocumentParserProvider docProvider;

	@Override
	@Cacheable("organization-by-name")
	public Organization findOrganizationByName(String organizationName) {
		logger.info("Finding organization by name '{}'", organizationName);
		Organization organization = null;

		try {
			SearchOrganizationByNameDocumentParser parser = docProvider
					.getSearchOrganizationByNameDocumentParser(URLEncoder.encode(organizationName, "utf-8"));

			String parsedOrganizationName = parser.parseOrganizationName();
			String organizationId = parser.parseOrganizationId();
			String organizationSite = parser.parseOrganizationSite();

			organization = new Organization(organizationId, parsedOrganizationName, organizationSite);
		} catch (Exception e) {
			logger.warn("Finding organization by name '{}', error: {}", organizationName, e.getMessage());
		}

		return organization;
	}

	@Override
	@Cacheable("organization-by-id")
	public Organization findOrganizationById(String organizationId) {
		logger.info("Finding organization by id '{}'", organizationId);

		Organization organization = null;

		try {
			AuthorsOnPageByOrganizationDocumentParser parser = docProvider
					.getAuthorsOnPageByOrganizationIdDocumentParser(organizationId, null);
			String organizationName = parser.parseOrganizationName();

			organization = findOrganizationByName(organizationName);
		} catch (Exception e) {
			logger.warn("Finding organization by id '{}', error: {}", organizationId, e.getMessage());
		}

		return organization;
	}
}
