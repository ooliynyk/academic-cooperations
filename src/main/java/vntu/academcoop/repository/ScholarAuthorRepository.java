package vntu.academcoop.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import vntu.academcoop.model.Author;
import vntu.academcoop.utils.crawling.DocumentProvider;
import vntu.academcoop.utils.crawling.crawler.AuthorsCrawler;
import vntu.academcoop.utils.crawling.crawler.DocumentCrawler;
import vntu.academcoop.utils.crawling.crawler.OrganizationPageDocumentCrawler;
import vntu.academcoop.utils.crawling.crawler.PersonalPageDocumentCrawler;
import vntu.academcoop.utils.crawling.doc.OrganizationPageDocument;
import vntu.academcoop.utils.crawling.doc.PersonalPageDocument;

@Repository
public class ScholarAuthorRepository implements AuthorRepository {

	private static Logger logger = LoggerFactory.getLogger(ScholarAuthorRepository.class);

	private final DocumentProvider docProvider;

	@Autowired
	public ScholarAuthorRepository(DocumentProvider docProvider) {
		this.docProvider = docProvider;
	}

	@Override
	@Cacheable("authors")
	public Collection<Author> findAllAuthorsByOrganizationId(String organizationId) {
		logger.info("Finding authors by organization id '{}'", organizationId);

		Collection<Author> authors = new ArrayList<>();

		try {
			String currentPageId = null;
			String nextPageId = null;
			do {
				currentPageId = nextPageId;

				Document doc = docProvider.getOrganizationPageDocument(organizationId, currentPageId);
				DocumentCrawler<OrganizationPageDocument> crawler = new OrganizationPageDocumentCrawler(doc);

				OrganizationPageDocument organizationPageDoc = crawler.crawl();

				Collection<String> authorsIdentifiers = organizationPageDoc.getAuthorsIdentifiers();
				Collection<Author> authorsOnPage = authorsIdentifiers.parallelStream().map(id -> findAuthorById(id))
						.collect(Collectors.toList());

				authors.addAll(authorsOnPage);

				nextPageId = organizationPageDoc.getNextPageId();
			} while (nextPageId != null && !nextPageId.equals(currentPageId));
		} catch (Exception e) {
			logger.warn("Finding authors by organization id '{}' error: {}", organizationId, e.getMessage());
		}

		return authors;
	}

	@Override
	@Cacheable(value = "author-by-id")
	public Author findAuthorById(final String authorId) {
		logger.info("Finding author by id '{}'", authorId);

		Author author = null;
		try {
			int firstPublicationNumber = 0;
			int totalPublicationsOnPage = 1;

			Document doc = docProvider.getPersonalPageDocument(authorId, firstPublicationNumber,
					totalPublicationsOnPage);
			DocumentCrawler<PersonalPageDocument> crawler = new PersonalPageDocumentCrawler(doc);

			PersonalPageDocument personalPageDoc = crawler.crawl();
			author = personalPageDoc.getAuthorDetails();
		} catch (Exception e) {
			logger.warn("Finding author by id '{}' error: {}", authorId, e.getMessage());
		}

		return author;
	}

	@Override
	@Cacheable("coauthors-by-authorid")
	public Collection<String> findCoAuthorsIdentifiersByAuthorId(final String authorId) {
		logger.info("Finding co-authors identifiers by authorId '{}'", authorId);

		Collection<String> authorsIdentifiers = Collections.emptyList();
		try {
			Document doc = docProvider.getCoAuthorsDocument(authorId);
			DocumentCrawler<Collection<String>> crawler = new AuthorsCrawler(doc);

			authorsIdentifiers = crawler.crawl();
		} catch (Exception e) {
			logger.warn("Finding co-author identifiers by authorId '{}' error: {}", authorId, e.getMessage());
		}

		return authorsIdentifiers;
	}

}
