package vntu.academcoop.service;

import java.util.Collection;
import java.util.Date;

import vntu.academcoop.dto.AuthorDetails;
import vntu.academcoop.model.Publication;

public interface PublicationService {

	Collection<Publication> fetchAllPublicationsByAuthor(AuthorDetails author);
	
	Collection<Publication> fetchAllPublicationsByAuthorBetweenYears(AuthorDetails author, Date fromYear, Date toYear);

}
