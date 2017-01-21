package vntu.academic.cooperation.crawl;

import org.jsoup.nodes.Document;

public interface DocumentProvider {

	Document getPersonalPageDocument(String authorId, int firstPublicationNumber, int totalPublicationsOnPage)
			throws DocumentParsingException;
	
	Document getCoAuthorsDocument(String authorId) throws DocumentParsingException;
	
	Document getOrganizationPageDocument(String organizationId, String pageId) throws DocumentParsingException;
	
	Document getSearchOrganizationDocument(String organizationName) throws DocumentParsingException;
	
	Document getPublicationDocument(String publicationId) throws DocumentParsingException;
	
}
