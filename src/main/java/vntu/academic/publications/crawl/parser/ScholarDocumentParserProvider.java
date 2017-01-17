package vntu.academic.publications.crawl.parser;

import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vntu.academic.publications.crawl.DocumentCrawler;
import vntu.academic.publications.crawl.DocumentCrawlingException;
import vntu.academic.publications.crawl.ProxiedDocumentCrawler;

@Component
public class ScholarDocumentParserProvider implements DocumentParserProvider {

	private static final String SEARCH_AUTHORS_BY_ORGANIZATION_URL_TEMPLATE = "https://scholar.google.com/citations?view_op=search_authors&hl=en&num=20&mauthors=%s&after_author=%s";
	private static final String CO_AUTHORS_BY_AUTHOR_ID_URL_TEMPLATE = "https://scholar.google.com/citations?view_op=list_colleagues&hl=en&user=%s";
	private static final String AUTHOR_PERSONAL_PAGE_URL_TEMPLATE = "https://scholar.google.com/citations?user=%s&hl=en";
	private static final String SEARCH_AUTHORS_BY_ORGANIZATION_ON_PAGE_URL_TEMPLATE = "https://scholar.google.com/citations?view_op=view_org&hl=en&org=%s&after_author=%s";
	private static final String SEARCH_BY_QUERY_URL_TEMPLATE = "https://scholar.google.com/scholar?hl=en&q=%s";
	private static final String SEARCH_ORGANIZATION_BY_NAME_URL_TEMPLATE = "https://scholar.google.com.ua/citations?mauthors=%s&hl=uk&view_op=search_authors";

	@Autowired
	private DocumentCrawler crawler = new ProxiedDocumentCrawler();

	@Override
	public AuthorsOnPageByOrganizationDocumentParser getAuthorsOnPageByOrganizationIdDocumentParser(
			String organizationId, String pageId) throws DocumentCrawlingException {
		Document doc = crawler.fetchDocument(
				String.format(SEARCH_AUTHORS_BY_ORGANIZATION_ON_PAGE_URL_TEMPLATE, organizationId, pageId));

		return new AuthorsOnPageByOrganizationDocumentParser(doc);
	}

	@Override
	public CoAuthorsByAuthorIdDocumentParser getCoAuthorsByAuthorIdDocumentParser(String authorId)
			throws DocumentCrawlingException {
		Document doc = crawler.fetchDocument(String.format(CO_AUTHORS_BY_AUTHOR_ID_URL_TEMPLATE, authorId));

		return new CoAuthorsByAuthorIdDocumentParser(doc);
	}

	@Override
	public AuthorPersonalPageDocumentParser getAuthorPersonalPageByAuthorIdDocumentParser(String authorId)
			throws DocumentCrawlingException {
		Document doc = crawler.fetchDocument(String.format(AUTHOR_PERSONAL_PAGE_URL_TEMPLATE, authorId));

		return new AuthorPersonalPageDocumentParser(doc);
	}

	@Override
	public AuthorsOnPageByOrganizationDocumentParser getAuthorsOnPageByOrganizationNameDocumentParser(
			String organizationName, String pageId) throws DocumentCrawlingException {
		Document doc = crawler
				.fetchDocument(String.format(SEARCH_AUTHORS_BY_ORGANIZATION_URL_TEMPLATE, organizationName, pageId));

		return new AuthorsOnPageByOrganizationDocumentParser(doc);
	}

	@Override
	public SearchAuthorByNameDocumentParser getSearchAuthorByNameDocumentParser(String query)
			throws DocumentCrawlingException {
		Document doc = crawler.fetchDocument(String.format(SEARCH_BY_QUERY_URL_TEMPLATE, query));

		return new SearchAuthorByNameDocumentParser(doc);
	}

	@Override
	public SearchOrganizationByNameDocumentParser getSearchOrganizationByNameDocumentParser(
			String organizationName) throws DocumentCrawlingException {
		Document doc = crawler.fetchDocument(String.format(SEARCH_ORGANIZATION_BY_NAME_URL_TEMPLATE, organizationName));

		return new SearchOrganizationByNameDocumentParser(doc);
	}

}
