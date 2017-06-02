package vntu.itcgs.repository;

import java.util.Collection;

import vntu.itcgs.model.Author;

public interface AuthorRepository {

	Author findAuthorById(String authorId);

	Collection<String> findCoAuthorsIdentifiersByAuthorId(String authorId);

	Collection<Author> findAllAuthorsByOrganizationId(String organizationId);

}
