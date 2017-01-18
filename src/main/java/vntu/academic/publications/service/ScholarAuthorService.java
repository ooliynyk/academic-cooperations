package vntu.academic.publications.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vntu.academic.publications.dao.AuthorDao;
import vntu.academic.publications.dto.AuthorDTO;
import vntu.academic.publications.dto.OrganizationDTO;
import vntu.academic.publications.model.Author;
import vntu.academic.publications.pageable.AuthorsPage;

@Service
public class ScholarAuthorService implements AuthorService {
	private static Logger logger = LoggerFactory.getLogger(ScholarAuthorService.class);

	@Autowired
	private AuthorDao authorDAO;

	@Override
	public Collection<AuthorDTO> fetchAllAuthorsWithCoAuthorsByOrganization(OrganizationDTO organizationDTO) {
		Collection<AuthorDTO> authorDTOs = new ArrayList<>();

		Collection<Author> authors = new HashSet<>();
		AuthorsPage page = authorDAO.findAuthorsPageByOrganizationId(organizationDTO.getId(), null);
		for (int i = 1; page.hasNextPage(); i++, page = page.nextPage()) {
			logger.info("Fetching on page {}", i);
			authors.addAll(page.authors());
		}

		authors.parallelStream().map((Author author) -> new AuthorDTO(author)).forEach((AuthorDTO authorDTO) -> {
			Collection<String> coAuthorsIdentifiers = authorDAO.findCoAuthorsIdentifiersByAuthorId(authorDTO.getId());

			Collection<Author> coAuthors = coAuthorsIdentifiers.parallelStream()
					.filter(coAuthorId -> hasCommunication(authorDTO.getId(), coAuthorId))
					.map(coAuthorId -> authorDAO.findAuthorById(coAuthorId))
					.filter(coAuthor -> coAuthor != null)
					.collect(Collectors.toList());

			authorDTO.setCoAuthors(coAuthors);
			authorDTOs.add(authorDTO);
		});

		return authorDTOs;
	}

	private boolean hasCommunication(String authorId, String coAuthorId) {
		Collection<String> coAuthorsIdentifiers = authorDAO.findCoAuthorsIdentifiersByAuthorId(coAuthorId);

		return coAuthorsIdentifiers.contains(authorId);
	}

}
