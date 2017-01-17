package vntu.academic.publications.crawl.parser;

import vntu.academic.publications.crawl.DocumentCrawlingException;

public interface DocumentParserProvider {

	CoAuthorsByAuthorIdDocumentParser getCoAuthorsByAuthorIdDocumentParser(String authorId)
			throws DocumentCrawlingException;

	AuthorsOnPageByOrganizationDocumentParser getAuthorsOnPageByOrganizationIdDocumentParser(String organizationId,
			String pageId) throws DocumentCrawlingException;

	AuthorsOnPageByOrganizationDocumentParser getAuthorsOnPageByOrganizationNameDocumentParser(String organizationName,
			String pageId) throws DocumentCrawlingException;

	AuthorPersonalPageDocumentParser getAuthorPersonalPageByAuthorIdDocumentParser(String authorId)
			throws DocumentCrawlingException;

	SearchAuthorByNameDocumentParser getSearchAuthorByNameDocumentParser(String query)
			throws DocumentCrawlingException;

	SearchOrganizationByNameDocumentParser getSearchOrganizationByNameDocumentParser(
			String organizationName) throws DocumentCrawlingException;

}
