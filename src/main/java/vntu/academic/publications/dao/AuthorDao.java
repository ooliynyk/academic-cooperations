package vntu.academic.publications.dao;

import java.util.Collection;

import vntu.academic.publications.model.Author;
import vntu.academic.publications.pageable.AuthorsPage;

public interface AuthorDao {

	Author findAuthorDetailsByAuthorId(String authorId);

	Collection<Author> findAuthorsOnFirstPageByOrganizationName(String organizationName);

	Collection<Author> findCoauthorsByAuthorId(String authorId);

	AuthorsPage findAuthorsPageByOrganizationId(String organizationId, String pageId);

}
