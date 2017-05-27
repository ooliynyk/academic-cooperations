package vntu.academcoop.dao;

import java.util.Collection;

import vntu.academcoop.model.Author;

public interface AuthorDao {

	Author findAuthorById(String authorId);

	Collection<String> findCoAuthorsIdentifiersByAuthorId(String authorId);

	Collection<Author> findAllAuthorsByOrganizationId(String organizationId);

}
