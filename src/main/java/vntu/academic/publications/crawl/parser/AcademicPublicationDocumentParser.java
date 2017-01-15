package vntu.academic.publications.crawl.parser;

import java.util.Collection;

import org.jsoup.nodes.Document;

import vntu.academic.publications.model.Author;
import vntu.academic.publications.model.Organization;

public interface AcademicPublicationDocumentParser {

	Author parseAuthorWithOrganizationId(Document doc, String authorId) throws DocumentParsingException;
	
	Author parseAuthorFromSearchResults(Document doc) throws DocumentParsingException;
	
	Organization parseOrganizationFromSearchResults(Document doc) throws DocumentParsingException;

	Collection<Author> parseAuthors(Document doc) throws DocumentParsingException;

	String parseNextPageId(Document doc) throws DocumentParsingException;

	Organization parseOrganization(Document doc) throws DocumentParsingException;

	String parseOrganizationName(Document doc) throws DocumentParsingException;

}
