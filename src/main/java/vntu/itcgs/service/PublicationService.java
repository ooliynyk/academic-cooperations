package vntu.itcgs.service;

import java.util.Collection;
import java.util.Date;

import vntu.itcgs.dto.AuthorDetails;
import vntu.itcgs.model.Publication;

public interface PublicationService {

	Collection<Publication> fetchAllPublicationsByAuthor(AuthorDetails author);
	
	Collection<Publication> fetchAllPublicationsByAuthorBetweenYears(AuthorDetails author, Date fromYear, Date toYear);

}
