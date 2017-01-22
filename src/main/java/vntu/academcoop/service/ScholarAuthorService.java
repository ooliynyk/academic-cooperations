package vntu.academcoop.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vntu.academcoop.dao.AuthorDao;
import vntu.academcoop.dto.AuthorDTO;
import vntu.academcoop.dto.OrganizationDTO;
import vntu.academcoop.model.Author;
import vntu.academcoop.model.Publication;

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
	public Collection<AuthorDTO> fetchAllAuthorsWithCoAuthorsFromOrganization(OrganizationDTO organizationDTO) {
		logger.info("Fetching authors with co-authors");

		Collection<Author> authors = authorDao.findAllAuthorsByOrganizationId(organizationDTO.getId());

		return findCoAuthorsAndMapToDTOs(authors);
	}
	
	@Override
	public Collection<AuthorDTO> fetchAllAuthorsWithCoAuthorsAndPublicationsBetweenYears(
			OrganizationDTO organizationDTO, Date fromYear, Date toYear) {
		logger.info("Fetching authors with publications");

		Collection<AuthorDTO> authorDTOs = fetchAllAuthorsWithCoAuthorsFromOrganization(organizationDTO);

		authorDTOs.parallelStream().forEach((AuthorDTO authorDTO) -> {
			Collection<Publication> publications = publicationService
					.fetchAllPublicationsByAuthorBetweenYears(authorDTO, fromYear, toYear);

			authorDTO.setPublications(publications);
		});

		return authorDTOs;
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
	
	private Collection<AuthorDTO> findCoAuthorsAndMapToDTOs(Collection<Author> authors) {
		Collection<AuthorDTO> authorDTOs = new ArrayList<>();
		authors.parallelStream()
			.forEach((Author author) -> {
				AuthorDTO authorDTO = new AuthorDTO(author);

				if (author.hasCoAuthors()) {
					Collection<String> coAuthorsIdentifiers = authorDao.findCoAuthorsIdentifiersByAuthorId(author.getId());
					Collection<Author> coAuthors = coAuthorsIdentifiers.parallelStream()
							.filter(coAuthorId -> hasCommunication(author.getId(), coAuthorId))
							.map(coAuthorId -> authorDao.findAuthorById(coAuthorId))
							.filter(coAuthor -> coAuthor != null)
							.collect(Collectors.toList());
					authorDTO.setCoAuthors(coAuthors);
				}

				authorDTOs.add(authorDTO);
			});
		
		return authorDTOs;
	}

	private boolean hasCommunication(String authorId, String coAuthorId) {
		Collection<String> coAuthorsIdentifiers = authorDao.findCoAuthorsIdentifiersByAuthorId(coAuthorId);

		return coAuthorsIdentifiers.contains(authorId);
	}

}
