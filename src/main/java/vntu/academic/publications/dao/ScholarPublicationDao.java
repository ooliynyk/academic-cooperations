package vntu.academic.publications.dao;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import vntu.academic.publications.crawl.DocumentCrawlingException;
import vntu.academic.publications.crawl.parser.DocumentParserProvider;
import vntu.academic.publications.crawl.parser.PublicationsOnAuthorPageDocumentParser;
import vntu.academic.publications.model.Publication;

@Repository
public class ScholarPublicationDao implements PublicationDao {

	private static final Logger logger = LoggerFactory.getLogger(ScholarPublicationDao.class);

	@Autowired
	private DocumentParserProvider docParserProvider;

	@Override
	@Cacheable("publications")
	public Collection<Publication> findAllPublicationsByAuthorId(String authorId) {
		logger.info("Finding publications by author id '{}'", authorId);

		Collection<Publication> publications = new ArrayList<>();

		try {
			PublicationsOnAuthorPageDocumentParser parser = null;

			final int pageSize = 100;
			int pageNumber = 0;
			do {
				int startFrom = pageNumber * pageSize;
				pageNumber++;

				parser = docParserProvider.getPublicationsOnAuthorPageDocumentParser(authorId, startFrom, pageSize);

				publications.addAll(parser.parsePublications());
			} while (parser.hasMorePublications());

		} catch (DocumentCrawlingException e) {
			logger.warn("Finding publications by author id '{}' error: '{}'", authorId, e.getMessage());
		}

		return publications;
	}

	@Override
	@Cacheable("publication-by-title")
	public Publication findPublicationByTitle(String publicationTitle) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args) {
		PublicationDao dao = new ScholarPublicationDao();
		Collection<Publication> publications = dao.findAllPublicationsByAuthorId("4POyYXgAAAAJ");
		System.out.println(publications);
		System.out.println(publications.size());
	}

}
