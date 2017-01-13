package vntu.academic.publications.dao;

import java.net.URLEncoder;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import vntu.academic.publications.crawl.DocumentCrawler;
import vntu.academic.publications.crawl.parser.AcademicPublicationDocumentParser;
import vntu.academic.publications.entities.Organization;

@Repository
public class ScholarOrganizationDao implements OrganizationDao {
	private static final Logger logger = LoggerFactory.getLogger(ScholarOrganizationDao.class);
	
	private static final String SEARCH_ORGANIZATION_URL = "https://scholar.google.com.ua/citations?mauthors=%s&hl=uk&view_op=search_authors";
	private static final String SEARCH_AUTHORS_BY_ORGANIZATION_URL = "https://scholar.google.com/citations?view_op=view_org&hl=en&org=%s&after_author=%s";
	
	@Autowired
	private DocumentCrawler docProvider;
	
	@Autowired
	private AcademicPublicationDocumentParser docParser;
	
	@Override
	@Cacheable("organization-by-name")
	public Organization findOrganizationByName(String organizationName) {
		logger.info("Finding organization by name '{}'", organizationName);
		Organization organization = null;

		try {
			Document doc = docProvider.fetchDocument(String.format(SEARCH_ORGANIZATION_URL, URLEncoder.encode(organizationName, "utf-8")));
			organization = docParser.parseOrganization(doc);
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
			Document doc = docProvider.fetchDocument(String.format(SEARCH_AUTHORS_BY_ORGANIZATION_URL, organizationId, null));
			String orgName = docParser.parseOrganizationName(doc);
			organization = findOrganizationByName(orgName);
		} catch (Exception e) {
			logger.warn("Finding organization by id '{}', error: {}", organizationId, e.getMessage());
		}

		return organization;
	}
}
