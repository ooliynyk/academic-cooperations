package vntu.academic.publications.pageable;

import java.util.Collection;

import vntu.academic.publications.dao.AuthorDao;
import vntu.academic.publications.model.Author;

public class ScholarAuthorsPage implements AuthorsPage {

	private final AuthorDao authorDAO;

	private final String organizationId;

	private final Collection<Author> authors;

	private final String nextPageId;

	public ScholarAuthorsPage(AuthorDao publicationRepository, String organizationId,
			Collection<Author> authors, String nextPageId) {
		this.authorDAO = publicationRepository;
		this.organizationId = organizationId;
		this.authors = authors;
		this.nextPageId = nextPageId;
	}
	
	@Override
	public boolean hasNextPage() {
		return nextPageId != null;
	}

	@Override
	public AuthorsPage nextPage() {
		if (!hasNextPage())
			throw new IllegalStateException("Has not next page");
		return authorDAO.findAuthorsPageByOrganizationId(organizationId, nextPageId);
	}

	@Override
	public Collection<Author> authors() {
		return authors;
	}

}
