package vntu.academic.publications.dao;

import java.util.ArrayList;
import java.util.Collection;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import vntu.academic.publications.crawl.AcademicPublicationDocumentProvider;
import vntu.academic.publications.crawl.parser.AcademicPublicationDocumentParser;
import vntu.academic.publications.model.Author;
import vntu.academic.publications.pageable.AuthorsPage;
import vntu.academic.publications.pageable.ScholarAuthorsPage;

@Repository
public class ScholarAuthorDao implements AuthorDao {

	private static Logger logger = LoggerFactory.getLogger(ScholarAuthorDao.class);

	@Autowired
	private AcademicPublicationDocumentProvider docProvider;
	
	@Autowired
	private AcademicPublicationDocumentParser docParser;

	@Override
	@Cacheable("authors-page")
	public AuthorsPage findAuthorsPageByOrganizationId(final String organizationId, final String currentPageId) {
		logger.info("Finding authors by organizationId '{}', pageId '{}'", organizationId, currentPageId);

		Collection<Author> authors = new ArrayList<>();
		String nextPageId = null;

		try {
			Document doc = docProvider.getAuthorsPageDocumentByOrganizationId(organizationId, currentPageId);

			authors = docParser.parseAuthors(doc);

			nextPageId = docParser.parseNextPageId(doc);
		} catch (Exception e) {
			logger.warn("Finding authors by organizationId '{}', pageId '{}' error: {}", organizationId, currentPageId,
					e.getMessage());
		}

		if (currentPageId != null && currentPageId.equals(nextPageId))
			nextPageId = null;
		
		return new ScholarAuthorsPage(this, organizationId, authors, nextPageId);
	}

	@Override
	public Collection<Author> findAuthorsOnFirstPageByOrganizationName(final String organizationName) {
		logger.info("Finding authors by organization name '{}', on first page", organizationName);
		Collection<Author> authors = new ArrayList<>();

		try {
			Document doc = docProvider.getAuthorsDocumentByOrganizationName(organizationName, null);

			authors = docParser.parseAuthors(doc);
		} catch (Exception e) {
			logger.warn("Finding authors by organization name '{}', on first page error: {}", organizationName, e.getMessage());
		}

		return authors;
	}

	@Override
	@Cacheable("co-authors")
	public Collection<Author> findCoauthorsByAuthorId(final String authorId) {
		logger.info("Finding co-authors by authorId '{}'", authorId);
		Collection<Author> authors = new ArrayList<>();
		try {
			Document coauthorsDoc = docProvider.getCouathorsDocumentByAuthorId(authorId);

			authors = docParser.parseAuthors(coauthorsDoc);
		} catch (Exception e) {
			logger.warn("Finding co-author details by authorId '{}' error: {}", authorId, e.getMessage());
		}
		return authors;
	}

	@Override
	@Cacheable("author")
	public Author findAuthorDetailsByAuthorId(final String authorId) {
		logger.info("Finding author details by id '{}'", authorId);
		Author author = null;
		try {
			Document authorPageDoc = docProvider.getAuthorPersonalPageDocumentByAuthorId(authorId);

			author = docParser.parseAuthorWithOrganizationId(authorPageDoc, authorId);
		} catch (Exception e) {
			logger.warn("Finding author details by id '{}' error: {}", authorId, e.getMessage());
		}

		return author;
	}

}
