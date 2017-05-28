package vntu.academcoop.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import vntu.academcoop.model.Publication;
import vntu.academcoop.utils.crawling.DocumentParsingException;
import vntu.academcoop.utils.crawling.DocumentProvider;
import vntu.academcoop.utils.crawling.ProxiedDocumentParser;
import vntu.academcoop.utils.crawling.ScholarDocumentProvider;
import vntu.academcoop.utils.crawling.crawler.DocumentCrawler;
import vntu.academcoop.utils.crawling.crawler.DocumentCrawlingException;
import vntu.academcoop.utils.crawling.crawler.PersonalPageDocumentCrawler;
import vntu.academcoop.utils.crawling.crawler.PublicationAuthorsCrawler;
import vntu.academcoop.utils.crawling.doc.PersonalPageDocument;

@Repository
public class ScholarPublicationRepository implements PublicationRepository {

	private static final Logger logger = LoggerFactory.getLogger(ScholarPublicationRepository.class);

	private final DocumentProvider docProvider;

	@Autowired
	public ScholarPublicationRepository(DocumentProvider docProvider) {
		this.docProvider = docProvider;
	}

	@Override
	@Cacheable("publications")
	public Collection<Publication> findAllPublicationsByAuthorId(String authorId) {
		logger.info("Finding publications by author id '{}'", authorId);
		
		Collection<Publication> publications = new ArrayList<>();

		final int PAGE_SIZE = 100;
		try {
			int pageNumber = 0;
			while (true) {
				int startFrom = pageNumber * PAGE_SIZE;
				logger.debug("Finding publications by author id '{}' start from {}p", authorId, startFrom);
				
				Document doc = docProvider.getPersonalPageDocument(authorId, startFrom, PAGE_SIZE);
				DocumentCrawler<PersonalPageDocument> crawler = new PersonalPageDocumentCrawler(doc);

				PersonalPageDocument personalPageDoc = crawler.crawl();

				for (Publication publication : personalPageDoc.getPublications()) {
					String id = publication.getId();
					String title = publication.getTitle();
					Collection<String> authorsNames = findAuthorsNames(publication);

					Date publicationDate = publication.getPublicationDate();
					publications.add(new Publication(id, title, authorsNames, publicationDate));
				}

				if (!personalPageDoc.isHasMorePublications()) {
					break;
				}

				pageNumber++;
			}
		} catch (Exception e) {
			logger.warn("Finding publications by author id '{}' error: '{}'", authorId, e.getMessage());
		}

		return publications;
	}

	@Override
	@Cacheable("authors-names-by-publicationId")
	public Collection<String> findAllAuthorsNamesByPublicationId(String publicationId) {
		logger.info("Finding authors by publication id '{}'", publicationId);
		Collection<String> authorsNames = null;
		try {
			Document doc = docProvider.getPublicationDocument(publicationId);
			DocumentCrawler<Collection<String>> crawler = new PublicationAuthorsCrawler(doc);
			authorsNames = crawler.crawl();
		} catch (Exception e) {
			logger.warn("Finding authors by publication id '{}' error: '{}'", publicationId, e.getMessage());
		}

		return authorsNames;
	}

	private Collection<String> findAuthorsNames(Publication publicationDetails)
			throws DocumentParsingException, DocumentCrawlingException {
		List<String> authorsNames = new ArrayList<>(publicationDetails.getAuthorsNames());
		if (authorsNames.isEmpty())
			throw new IllegalArgumentException("Publication authors not specified");

		int lastAuthor = authorsNames.size() - 1;
		if (authorsNames.get(lastAuthor).equals("...")) {
			authorsNames = new ArrayList<>(findAllAuthorsNamesByPublicationId(publicationDetails.getId()));
		}

		return authorsNames;
	}

	public static void main(String[] args) {
		PublicationRepository dao = new ScholarPublicationRepository(new ScholarDocumentProvider(new ProxiedDocumentParser()));
		Collection<Publication> publications = dao.findAllPublicationsByAuthorId("4POyYXgAAAAJ");
		System.out.println(publications);
		System.out.println(publications.size());
	}

}
