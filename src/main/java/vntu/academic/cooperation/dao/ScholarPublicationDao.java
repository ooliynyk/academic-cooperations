package vntu.academic.cooperation.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import vntu.academic.cooperation.crawl.DocumentProvider;
import vntu.academic.cooperation.crawl.ProxiedDocumentParser;
import vntu.academic.cooperation.crawl.ScholarDocumentProvider;
import vntu.academic.cooperation.crawl.crawler.DocumentCrawler;
import vntu.academic.cooperation.crawl.crawler.PersonalPageDocumentCrawler;
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
					Collection<String> authorsNames = publicationDetails.getAuthorNames();
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

	public static void main(String[] args) {
		PublicationDao dao = new ScholarPublicationDao(new ScholarDocumentProvider(new ProxiedDocumentParser()));
		Collection<Publication> publications = dao.findAllPublicationsByAuthorId("4POyYXgAAAAJ");
		System.out.println(publications);
		System.out.println(publications.size());
	}

}
