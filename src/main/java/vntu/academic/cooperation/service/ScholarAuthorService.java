package vntu.academic.cooperation.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vntu.academic.cooperation.dao.AuthorDao;
import vntu.academic.cooperation.dto.AuthorDTO;
import vntu.academic.cooperation.dto.OrganizationDTO;
import vntu.academic.cooperation.model.Author;
import vntu.academic.cooperation.model.Publication;

@Service
public class ScholarAuthorService implements AuthorService {
	private static Logger logger = LoggerFactory.getLogger(ScholarAuthorService.class);

	private final AuthorDao authorDao;
	private final PublicationService publicationService;
	
	@Autowired
	public ScholarAuthorService(AuthorDao authorDao, PublicationService publicationService) {
		this.authorDao = authorDao;
		this.publicationService = publicationService;
	}

	@Override
	public Collection<AuthorDTO> fetchAllAuthorsWithCoAuthorsByOrganization(OrganizationDTO organizationDTO) {
		logger.info("Fetching authors with co-authors");

		Collection<AuthorDTO> authorDTOs = fetchAllAuthorsByOrganization(organizationDTO);

		authorDTOs.parallelStream().forEach((AuthorDTO authorDTO) -> {
			Collection<String> coAuthorsIdentifiers = authorDao.findCoAuthorsIdentifiersByAuthorId(authorDTO.getId());

			Collection<Author> coAuthors = coAuthorsIdentifiers.parallelStream()
					.filter(coAuthorId -> hasCommunication(authorDTO.getId(), coAuthorId))
					.map(coAuthorId -> authorDao.findAuthorById(coAuthorId)).filter(coAuthor -> coAuthor != null)
					.collect(Collectors.toList());

			authorDTO.setCoAuthors(coAuthors);
		});

		return authorDTOs;
	}

	@Override
	public Collection<AuthorDTO> fetchAllAuthorsWithPublicationsByOrganizationBetweenYears(
			OrganizationDTO organizationDTO, Date fromYear, Date toYear) {
		logger.info("Fetching authors with publications");

		Collection<AuthorDTO> authorDTOs = fetchAllAuthorsByOrganization(organizationDTO);

		authorDTOs.parallelStream().forEach((AuthorDTO authorDTO) -> {
			Collection<Publication> publications = publicationService
					.fetchAllPublicationsByAuthorBetweenYears(authorDTO, fromYear, toYear);

			authorDTO.setPublications(publications);
		});

		return authorDTOs;
	}

	@Override
	public Collection<AuthorDTO> fetchAllAuthorsByOrganization(OrganizationDTO organizationDTO) {
		logger.info("Fetching authors by organization '{}'", organizationDTO);

		Collection<Author> authors = authorDao.findAllAuthorsByOrganizationId(organizationDTO.getId());

		return authors.parallelStream().map((Author author) -> new AuthorDTO(author)).collect(Collectors.toList());
	}

	private boolean hasCommunication(String authorId, String coAuthorId) {
		Collection<String> coAuthorsIdentifiers = authorDao.findCoAuthorsIdentifiersByAuthorId(coAuthorId);

		return coAuthorsIdentifiers.contains(authorId);
	}

	@Override
	public Collection<AuthorDTO> fetchAuthorsByIdentifiers(Collection<String> authorsIdentifiers) {
		Collection<AuthorDTO> authors = new ArrayList<>(authorsIdentifiers.size());
		for (String id : authorsIdentifiers) {
			Author author = authorDao.findAuthorById(id);
			if (author != null) {
				AuthorDTO authorDTO = new AuthorDTO(author);
				authors.add(authorDTO);
			}
		}

		return authors;
	}

}
