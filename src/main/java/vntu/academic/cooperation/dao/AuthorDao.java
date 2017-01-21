package vntu.academic.cooperation.dao;

import java.util.Collection;

import vntu.academic.cooperation.model.Author;

public interface AuthorDao {

	Author findAuthorById(String authorId);

	Collection<String> findCoAuthorsIdentifiersByAuthorId(String authorId);

	Collection<Author> findAllAuthorsByOrganizationId(String organizationId);

}
