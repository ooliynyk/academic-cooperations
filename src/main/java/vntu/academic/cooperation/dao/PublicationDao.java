package vntu.academic.cooperation.dao;

import java.util.Collection;

import vntu.academic.cooperation.model.Publication;

public interface PublicationDao {

	Collection<Publication> findAllPublicationsByAuthorId(String authorId);

}
