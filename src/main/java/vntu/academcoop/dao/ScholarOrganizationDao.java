package vntu.academcoop.dao;

import java.net.URLEncoder;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import vntu.academcoop.model.Organization;
import vntu.academcoop.utils.crawling.DocumentProvider;
import vntu.academcoop.utils.crawling.crawler.DocumentCrawler;
import vntu.academcoop.utils.crawling.crawler.InstitutionBlockCrawler;
import vntu.academcoop.utils.crawling.crawler.OrganizationPageDocumentCrawler;
import vntu.academcoop.utils.crawling.doc.OrganizationPageDocument;

@Repository
public class ScholarOrganizationDao implements OrganizationDao {

	private static final Logger logger = LoggerFactory.getLogger(ScholarOrganizationDao.class);

	private final DocumentProvider docProvider;

	@Autowired
	public ScholarOrganizationDao(DocumentProvider docProvider) {
		this.docProvider = docProvider;
	}

	@Override
	@Cacheable("organization-by-name")
	public Organization findOrganizationByName(String organizationName) {
		logger.info("Finding organization by name '{}'", organizationName);
		Organization organization = null;

		try {
			Document doc = docProvider.getSearchOrganizationDocument(URLEncoder.encode(organizationName, "utf-8"));

			DocumentCrawler<Organization> crawler = new InstitutionBlockCrawler(doc);

			organization = crawler.crawl();
		} catch (Exception e) {
			logger.warn("Finding organization by name '{}', error: {}", organizationName, e.getMessage());
		}

		return organization;
	}

	@Override
	@Cacheable(value = "organization-by-id")
	public Organization findOrganizationById(String organizationId) {
		logger.info("Finding organization by id '{}'", organizationId);

		Organization organization = null;
		try {
			Document doc = docProvider.getOrganizationPageDocument(organizationId, null);
			DocumentCrawler<OrganizationPageDocument> crawler = new OrganizationPageDocumentCrawler(doc);

			OrganizationPageDocument organizationPageDoc = crawler.crawl();

			organization = organizationPageDoc.getOrganizationDetails();
		} catch (Exception e) {
			logger.warn("Finding organization by id '{}', error: {}", organizationId, e.getMessage());
		}

		return organization;
	}
}
