package vntu.academic.cooperation.service;

import java.util.Collection;
import java.util.Date;

import vntu.academic.cooperation.dto.AuthorDTO;
import vntu.academic.cooperation.model.Publication;

public interface PublicationService {

	Collection<Publication> fetchAllPublicationsByAuthor(AuthorDTO author);
	
	Collection<Publication> fetchAllPublicationsByAuthorBetweenYears(AuthorDTO author, Date fromYear, Date toYear);

}
