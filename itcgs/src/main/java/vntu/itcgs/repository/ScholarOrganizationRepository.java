package vntu.itcgs.repository;

import java.net.URLEncoder;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import vntu.itcgs.model.Organization;
import vntu.itcgs.utils.crawling.DocumentParsingException;
import vntu.itcgs.utils.crawling.DocumentProvider;
import vntu.itcgs.utils.crawling.crawler.DocumentCrawler;
import vntu.itcgs.utils.crawling.crawler.InstitutionBlockCrawler;
import vntu.itcgs.utils.crawling.crawler.OrganizationPageDocumentCrawler;
import vntu.itcgs.utils.crawling.doc.OrganizationPageDocument;

@Repository
public class ScholarOrganizationRepository implements OrganizationRepository {

	private static final Logger logger = LoggerFactory.getLogger(ScholarOrganizationRepository.class);

	private final DocumentProvider docProvider;

	@Autowired
	public ScholarOrganizationRepository(DocumentProvider docProvider) {
		this.docProvider = docProvider;
	}

	@Override
	@Cacheable(value = "organization-by-name", key = "#organizationName", unless = "#result == null")
	public Organization findOrganizationByName(String organizationName) throws DocumentParsingException {
		logger.info("Finding organization by name '{}'", organizationName);
		Organization organization = null;

		try {
			Document doc = docProvider.getSearchOrganizationDocument(URLEncoder.encode(organizationName, "utf-8"));

			DocumentCrawler<Organization> crawler = new InstitutionBlockCrawler(doc);

			organization = crawler.crawl();
		} catch (DocumentParsingException e) {
			throw e;
		} catch (Exception e) {
			logger.warn("Finding organization by name '{}', error: {}", organizationName, e.getMessage());
		}

		return organization;
	}

	@Override
	@Cacheable(value = "organization-by-id", key = "#organizationId", unless = "#result == null")
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
