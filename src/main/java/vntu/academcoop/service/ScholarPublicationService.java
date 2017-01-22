package vntu.academcoop.service;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vntu.academcoop.dao.PublicationDao;
import vntu.academcoop.dto.AuthorDTO;
import vntu.academcoop.model.Publication;

@Service
public class ScholarPublicationService implements PublicationService {

	private final PublicationDao publicationDao;

	@Autowired
	public ScholarPublicationService(PublicationDao publicationDao) {
		this.publicationDao = publicationDao;
	}

	@Override
	public Collection<Publication> fetchAllPublicationsByAuthor(AuthorDTO author) {
		return publicationDao.findAllPublicationsByAuthorId(author.getId());
	}

	@Override
	public Collection<Publication> fetchAllPublicationsByAuthorBetweenYears(AuthorDTO author, Date fromYear,
			Date toYear) {
		Collection<Publication> publications = publicationDao.findAllPublicationsByAuthorId(author.getId());

		if (fromYear != null || toYear != null) {
			publications = publications.stream().filter(p -> p.getPublicationDate() != null)
					.filter(p -> afterOrInYear(p.getPublicationDate(), fromYear))
					.filter(p -> beforeOrInYear(p.getPublicationDate(), toYear)).collect(Collectors.toList());
		}

		return publications;

	}

	private static boolean afterOrInYear(Date year, Date fromYear) {
		return fromYear != null ? year.compareTo(fromYear) >= 0 : true;
	}

	private static boolean beforeOrInYear(Date year, Date toYear) {
		return toYear != null ? year.compareTo(toYear) <= 0 : true;
	}

}
