package vntu.academcoop.service;

import java.util.Collection;
import java.util.Date;

import vntu.academcoop.dto.AuthorDTO;
import vntu.academcoop.model.Publication;

public interface PublicationService {

	Collection<Publication> fetchAllPublicationsByAuthor(AuthorDTO author);
	
	Collection<Publication> fetchAllPublicationsByAuthorBetweenYears(AuthorDTO author, Date fromYear, Date toYear);

}
