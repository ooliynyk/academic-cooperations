package vntu.academic.publications.dao;

import java.util.Collection;

import vntu.academic.publications.model.Publication;

public interface PublicationDao {

	Collection<Publication> findAllPublicationsByAuthorId(String authorId);

	Publication findPublicationByTitle(String publicationTitle);

}
