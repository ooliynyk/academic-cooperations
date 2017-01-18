package vntu.academic.publications.dao;

import java.util.Collection;

import vntu.academic.publications.model.Author;

public interface AuthorDao {

	Author findAuthorById(String authorId);

	Author findAuthorByName(String authorName);

	Collection<Author> findAuthorsOnFirstPageByOrganizationName(String organizationName);

	Collection<String> findCoAuthorsIdentifiersByAuthorId(String authorId);

	Collection<Author> findAllAuthorsByOrganizationId(String organizationId);

}
