package vntu.academcoop.utils.crawling;

import java.util.Collection;

import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vntu.academcoop.model.Organization;
import vntu.academcoop.utils.crawling.crawler.AuthorsCrawler;
import vntu.academcoop.utils.crawling.crawler.DocumentCrawler;
import vntu.academcoop.utils.crawling.crawler.DocumentCrawlingException;
import vntu.academcoop.utils.crawling.crawler.InstitutionBlockCrawler;
import vntu.academcoop.utils.crawling.crawler.OrganizationPageDocumentCrawler;
import vntu.academcoop.utils.crawling.crawler.PersonalPageDocumentCrawler;
import vntu.academcoop.utils.crawling.crawler.PublicationAuthorsCrawler;
import vntu.academcoop.utils.crawling.doc.OrganizationPageDocument;
import vntu.academcoop.utils.crawling.doc.PersonalPageDocument;

@Service
public class ScholarDocumentProvider implements DocumentProvider {

	private static final String AUTHOR_PERSONAL_PAGE_URL_TEMPLATE = "https://scholar.google.com/citations?hl=en&user=%s&cstart=%d&pagesize=%d";
	private static final String CO_AUTHORS_BY_AUTHOR_ID_URL_TEMPLATE = "https://scholar.google.com/citations?view_op=list_colleagues&hl=en&user=%s";
	private static final String ORGANIZATION_PAGE_URL_TEMPLATE = "https://scholar.google.com/citations?view_op=view_org&hl=en&org=%s&after_author=%s";
	private static final String SEARCH_ORGANIZATION_BY_NAME_URL_TEMPLATE = "https://scholar.google.com/citations?mauthors=%s&hl=uk&view_op=search_authors";
	private static final String PUBLICATION_URL_TEMPLATE = "https://scholar.google.com/citations?view_op=view_citation&hl=en&user=%s&citation_for_view=%s";

	private final DocumentParser documentParser;

	@Autowired
	public ScholarDocumentProvider(DocumentParser proxiedDocumentParser) {
		this.documentParser = proxiedDocumentParser;
	}

	@Override
	public Document getPersonalPageDocument(String authorId, int firstPublicationNumber, int totalPublicationsOnPage)
			throws DocumentParsingException {
		return documentParser.parseDocument(String.format(AUTHOR_PERSONAL_PAGE_URL_TEMPLATE, authorId,
				firstPublicationNumber, totalPublicationsOnPage));
	}

	@Override
	public Document getCoAuthorsDocument(String authorId) throws DocumentParsingException {
		return documentParser.parseDocument(String.format(CO_AUTHORS_BY_AUTHOR_ID_URL_TEMPLATE, authorId));
	}

	@Override
	public Document getOrganizationPageDocument(String organizationId, String pageId) throws DocumentParsingException {
		return documentParser.parseDocument(String.format(ORGANIZATION_PAGE_URL_TEMPLATE, organizationId, pageId));
	}

	@Override
	public Document getSearchOrganizationDocument(String organizationName) throws DocumentParsingException {
		return documentParser.parseDocument(String.format(SEARCH_ORGANIZATION_BY_NAME_URL_TEMPLATE, organizationName));
	}

	@Override
	public Document getPublicationDocument(String publicationId) throws DocumentParsingException {
		String authorId = publicationId.split(":")[0];
		return documentParser.parseDocument(String.format(PUBLICATION_URL_TEMPLATE, authorId, publicationId));
	}

	public static void main1(String[] args) throws DocumentParsingException, DocumentCrawlingException {
		DocumentProvider docProvider = new ScholarDocumentProvider(new ProxiedDocumentParser());
		Document doc = docProvider.getPersonalPageDocument("PFhuy-QAAAAJ", 0, 100);
		// LnnWtiwAAAAJ
		// 4POyYXgAAAAJ

		DocumentCrawler<PersonalPageDocument> crawler = new PersonalPageDocumentCrawler(doc);

		PersonalPageDocument page = crawler.crawl();

		System.out.println(page.getAuthorDetails().hasCoAuthors());
		System.out.println(page.getPublications());
		System.out.println(page.getPublications().size());
		System.out.println(page.hasMorePublications());
	}

	public static void main2(String[] args) throws DocumentParsingException, DocumentCrawlingException {
		DocumentProvider docProvider = new ScholarDocumentProvider(new ProxiedDocumentParser());
		Document doc = docProvider.getCoAuthorsDocument("4POyYXgAAAAJ");

		DocumentCrawler<Collection<String>> crawler = new AuthorsCrawler(doc);
		Collection<String> coauthors = crawler.crawl();
		System.out.println(coauthors);
	}

	public static void main3(String[] args) throws DocumentParsingException, DocumentCrawlingException {
		DocumentProvider docProvider = new ScholarDocumentProvider(new ProxiedDocumentParser());
		Document doc = docProvider.getOrganizationPageDocument("2282009988358153816", "ucJUAEL-__8J");

		DocumentCrawler<OrganizationPageDocument> crawler = new OrganizationPageDocumentCrawler(doc);
		OrganizationPageDocument p = crawler.crawl();

		System.out.println(p.getAuthorsIdentifiers());
		System.out.println(p.getOrganizationDetails());
		System.out.println(p.getNextPageId());
	}

	public static void main5(String[] args) throws DocumentParsingException, DocumentCrawlingException {
		DocumentProvider docProvider = new ScholarDocumentProvider(new ProxiedDocumentParser());
		Document doc = docProvider.getSearchOrganizationDocument("Вінницький національний технічний університет");

		DocumentCrawler<Organization> crawler = new InstitutionBlockCrawler(doc);
		Organization details = crawler.crawl();
		System.out.println(details);
	}

	public static void main(String[] args) throws DocumentParsingException, DocumentCrawlingException {
		DocumentProvider docProvider = new ScholarDocumentProvider(new ProxiedDocumentParser());
		Document doc = docProvider.getPublicationDocument("gUMCk_AAAAAJ:u5HHmVD_uO8C");

		System.out.println(doc);
		DocumentCrawler<Collection<String>> crawler = new PublicationAuthorsCrawler(doc);
		System.out.println(crawler.crawl());
	}

}
