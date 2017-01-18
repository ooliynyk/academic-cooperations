package vntu.academic.publications.service;

import java.util.Collection;
import java.util.Date;

import vntu.academic.publications.dto.AuthorDTO;
import vntu.academic.publications.model.Publication;

public interface PublicationService {

	Collection<Publication> fetchAllPublicationsByAuthor(AuthorDTO author);
	
	Collection<Publication> fetchAllPublicationsByAuthorBetweenYears(AuthorDTO author, Date fromYear, Date toYear);

}
