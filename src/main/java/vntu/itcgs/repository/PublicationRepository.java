package vntu.itcgs.repository;

import java.util.Collection;

import vntu.itcgs.model.Publication;

public interface PublicationRepository {

	Collection<Publication> findAllPublicationsByAuthorId(String authorId);

	Collection<String> findAllAuthorsNamesByPublicationId(String publicationId);

}
