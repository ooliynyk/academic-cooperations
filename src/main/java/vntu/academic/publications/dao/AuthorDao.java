package vntu.academic.publications.dao;

import java.util.Collection;

import vntu.academic.publications.entities.Author;
import vntu.academic.publications.pageable.AuthorsPage;

public interface AuthorDao {

	Author findAuthorDetailsByAuthorId(String authorId);
	
	Collection<Author> findAuthorsOnFirstPageByOrganizationName(String organizationName);

	Collection<Author> findCoauthorsByAuthorId(String authorId);

	AuthorsPage findAuthorsPageByOrganizationId(String organizationId, String pageId);
	
}
