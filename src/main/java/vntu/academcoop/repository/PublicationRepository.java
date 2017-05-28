package vntu.academcoop.repository;

import java.util.Collection;

import vntu.academcoop.model.Publication;

public interface PublicationRepository {

	Collection<Publication> findAllPublicationsByAuthorId(String authorId);

	Collection<String> findAllAuthorsNamesByPublicationId(String publicationId);

}
