package vntu.academic.cooperation.dao;

import java.net.URLEncoder;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import vntu.academic.cooperation.crawl.DocumentProvider;
import vntu.academic.cooperation.crawl.crawler.DocumentCrawler;
import vntu.academic.cooperation.crawl.crawler.InstitutionBlockCrawler;
import vntu.academic.cooperation.crawl.crawler.OrganizationPageDocumentCrawler;
import vntu.academic.cooperation.crawl.doc.OrganizationPageDocument;
import vntu.academic.cooperation.crawl.doc.OrganizationPageDocument.OrganizationDetails;
import vntu.academic.cooperation.model.Organization;

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

			DocumentCrawler<OrganizationDetails> crawler = new InstitutionBlockCrawler(doc);
			OrganizationDetails orgDetails = crawler.crawl();

			String crawledOrganizationName = orgDetails.getName();
			String organizationId = orgDetails.getId();

			organization = new Organization(organizationId, crawledOrganizationName);
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
			Document doc = docProvider.getOrganizationPageDocument(organizationId, null);
			DocumentCrawler<OrganizationPageDocument> crawler = new OrganizationPageDocumentCrawler(doc);

			OrganizationPageDocument organizationPageDoc = crawler.crawl();
			OrganizationDetails orgDetails = organizationPageDoc.getOrganizationDetails();

			organization = new Organization(orgDetails.getId(), orgDetails.getName());
		} catch (Exception e) {
			logger.warn("Finding organization by id '{}', error: {}", organizationId, e.getMessage());
		}

		return organization;
	}
}
