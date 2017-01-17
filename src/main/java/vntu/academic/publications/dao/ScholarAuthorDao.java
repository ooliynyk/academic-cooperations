package vntu.academic.publications.dao;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import vntu.academic.publications.crawl.parser.DocumentParserProvider;
import vntu.academic.publications.crawl.parser.AuthorPersonalPageDocumentParser;
import vntu.academic.publications.crawl.parser.AuthorsOnPageByOrganizationDocumentParser;
import vntu.academic.publications.crawl.parser.CoAuthorsByAuthorIdDocumentParser;
import vntu.academic.publications.crawl.parser.SearchAuthorByNameDocumentParser;
import vntu.academic.publications.model.Author;
import vntu.academic.publications.pageable.AuthorsPage;
import vntu.academic.publications.pageable.ScholarAuthorsPage;

@Repository
public class ScholarAuthorDao implements AuthorDao {

	private static Logger logger = LoggerFactory.getLogger(ScholarAuthorDao.class);

	@Autowired
	private DocumentParserProvider docProvider;

	@Override
	@Cacheable("authors-page")
	public AuthorsPage findAuthorsPageByOrganizationId(final String organizationId, final String currentPageId) {
		logger.info("Finding authors by organizationId '{}', pageId '{}'", organizationId, currentPageId);

		Collection<Author> authors = new ArrayList<>();
		String nextPageId = null;

		try {
			AuthorsOnPageByOrganizationDocumentParser parser = docProvider
					.getAuthorsOnPageByOrganizationIdDocumentParser(organizationId, currentPageId);

			authors = parser.parseAuthors();

			nextPageId = parser.parseNextPageId();
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
			AuthorsOnPageByOrganizationDocumentParser parser = docProvider
					.getAuthorsOnPageByOrganizationNameDocumentParser(organizationName, null);

			authors = parser.parseAuthors();
		} catch (Exception e) {
			logger.warn("Finding authors by organization name '{}', on first page error: {}", organizationName,
					e.getMessage());
		}

		return authors;
	}

	@Override
	@Cacheable("co-authors")
	public Collection<Author> findCoauthorsByAuthorId(final String authorId) {
		logger.info("Finding co-authors by authorId '{}'", authorId);
		Collection<Author> authors = new ArrayList<>();
		try {
			CoAuthorsByAuthorIdDocumentParser parser = docProvider.getCoAuthorsByAuthorIdDocumentParser(authorId);

			authors = parser.parseAuthors();
		} catch (Exception e) {
			logger.warn("Finding co-author details by authorId '{}' error: {}", authorId, e.getMessage());
		}

		return authors;
	}

	@Override
	@Cacheable("author")
	public Author findAuthorById(final String authorId) {
		logger.info("Finding author details by id '{}'", authorId);
		Author author = null;
		try {
			AuthorPersonalPageDocumentParser parser = docProvider
					.getAuthorPersonalPageByAuthorIdDocumentParser(authorId);

			String authorName = parser.parseAuthorName();
			String organizationId = parser.parseOrganizationId();

			author = new Author(authorId, authorName, organizationId);
		} catch (Exception e) {
			logger.warn("Finding author details by id '{}' error: {}", authorId, e.getMessage());
		}

		return author;
	}

	@Override
	public Author findAuthorByName(String authorName) {
		logger.info("Finding author by name '{}'", authorName);
		Author author = null;
		try {
			// TODO : URLEncoder.encode
			SearchAuthorByNameDocumentParser parser = docProvider
					.getSearchAuthorByNameDocumentParser(authorName);

			String authorId = parser.parseAuthorId();

			// maybe is better to use parsed authorName
			author = new Author(authorId, authorName, null);
		} catch (Exception e) {
			logger.warn("Finding author by name '{}' error: {}", authorName, e.getMessage());
		}

		return author;
	}

}
