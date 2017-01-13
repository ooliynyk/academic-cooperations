package vntu.academic.publications.crawl.parser;

import java.util.Collection;

import org.jsoup.nodes.Document;

import vntu.academic.publications.entities.Author;
import vntu.academic.publications.entities.Organization;

public interface AcademicPublicationDocumentParser {
	
	Author parseAuthorWithOrganizationId(Document doc, String authorId) throws DocumentParsingException;

	Collection<Author> parseAuthors(Document doc) throws DocumentParsingException;

	String parseNextPageId(Document doc) throws DocumentParsingException;

	Organization parseOrganization(Document doc) throws DocumentParsingException;

	String parseOrganizationName(Document doc) throws DocumentParsingException;

}
