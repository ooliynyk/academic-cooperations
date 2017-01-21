package vntu.academic.cooperation.dao;

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

import vntu.academic.cooperation.crawl.DocumentParsingException;
import vntu.academic.cooperation.crawl.DocumentProvider;
import vntu.academic.cooperation.crawl.ProxiedDocumentParser;
import vntu.academic.cooperation.crawl.ScholarDocumentProvider;
import vntu.academic.cooperation.crawl.crawler.DocumentCrawler;
import vntu.academic.cooperation.crawl.crawler.DocumentCrawlingException;
import vntu.academic.cooperation.crawl.crawler.PersonalPageDocumentCrawler;
import vntu.academic.cooperation.crawl.crawler.PublicationAuthorsCrawler;
import vntu.academic.cooperation.crawl.doc.PersonalPageDocument;
import vntu.academic.cooperation.crawl.doc.PersonalPageDocument.PublicationDetails;
import vntu.academic.cooperation.model.Publication;

@Repository
public class ScholarPublicationDao implements PublicationDao {

	private static final Logger logger = LoggerFactory.getLogger(ScholarPublicationDao.class);

	private final DocumentProvider docProvider;

	@Autowired
	public ScholarPublicationDao(DocumentProvider docProvider) {
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

				Document doc = docProvider.getPersonalPageDocument(authorId, startFrom, PAGE_SIZE);
				DocumentCrawler<PersonalPageDocument> crawler = new PersonalPageDocumentCrawler(doc);

				PersonalPageDocument personalPageDoc = crawler.crawl();

				Collection<PublicationDetails> publicationsDetails = personalPageDoc.getPublications();

				for (PublicationDetails publicationDetails : publicationsDetails) {
					String id = publicationDetails.getId();
					String title = publicationDetails.getTitle();
					Collection<String> authorsNames = findAuthorsNames(publicationDetails);
					
					Date publicationDate = publicationDetails.getPublicationDate();
					publications.add(new Publication(id, title, authorsNames, publicationDate));
				}

				if (!personalPageDoc.hasMorePublications()) {
					break;
				}

				pageNumber++;
			}
		} catch (Exception e) {
			logger.warn("Finding publications by author id '{}' error: '{}'", authorId, e.getMessage());
		}

		return publications;
	}
	
	private Collection<String> findAuthorsNames(PublicationDetails publicationDetails) throws DocumentParsingException, DocumentCrawlingException {
		List<String> authorsNames = new ArrayList<>(publicationDetails.getAuthorNames());
		if (authorsNames.isEmpty())
			throw new IllegalArgumentException("Publication authors not specified");
		
		int lastAuthor = authorsNames.size() - 1;
		if (authorsNames.get(lastAuthor).equals("...")) {
			Document doc = docProvider.getPublicationDocument(publicationDetails.getId());
			DocumentCrawler<Collection<String>> crawler = new PublicationAuthorsCrawler(doc);
			authorsNames = new ArrayList<>(crawler.crawl());
		}
		
		return authorsNames;
	}

	public static void main(String[] args) {
		PublicationDao dao = new ScholarPublicationDao(new ScholarDocumentProvider(new ProxiedDocumentParser()));
		Collection<Publication> publications = dao.findAllPublicationsByAuthorId("4POyYXgAAAAJ");
		System.out.println(publications);
		System.out.println(publications.size());
	}

}
