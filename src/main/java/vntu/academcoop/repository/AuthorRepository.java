package vntu.academcoop.repository;

import java.util.Collection;

import vntu.academcoop.model.Author;

public interface AuthorRepository {

	Author findAuthorById(String authorId);

	Collection<String> findCoAuthorsIdentifiersByAuthorId(String authorId);

	Collection<Author> findAllAuthorsByOrganizationId(String organizationId);

}
