package vntu.academcoop.dao;

import java.util.Collection;

import vntu.academcoop.model.Publication;

public interface PublicationDao {

	Collection<Publication> findAllPublicationsByAuthorId(String authorId);

}
