package vntu.academic.publications.crawl;

import org.jsoup.nodes.Document;

public interface AcademicPublicationDocumentProvider {

	Document getAuthorsPageDocumentByOrganizationId(String organizationId, String pageId)
			throws DocumentCrawlingException;

	Document getCouathorsDocumentByAuthorId(String authorId) throws DocumentCrawlingException;

	Document getAuthorPersonalPageDocumentByAuthorId(String authorId) throws DocumentCrawlingException;

	Document getAuthorsDocumentByOrganizationName(String organizationId, String pageId)
			throws DocumentCrawlingException;

}
