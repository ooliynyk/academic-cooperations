package vntu.academcoop.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vntu.academcoop.dto.AuthorDetails;
import vntu.academcoop.dto.OrganizationDetails;
import vntu.academcoop.model.Author;
import vntu.academcoop.model.Publication;
import vntu.academcoop.repository.AuthorRepository;

@Service
public class ScholarAuthorService implements AuthorService {
	private static Logger logger = LoggerFactory.getLogger(ScholarAuthorService.class);

	private final AuthorRepository authorRepository;
	private final PublicationService publicationService;
	
	@Autowired
	public ScholarAuthorService(AuthorRepository authorRepository, PublicationService publicationService) {
		this.authorRepository = authorRepository;
		this.publicationService = publicationService;
	}

	@Override
	public Collection<AuthorDetails> fetchAllAuthorsWithCoAuthorsFromOrganization(OrganizationDetails organizationDetails) {
		logger.info("Fetching authors with co-authors");

		Collection<Author> authors = authorRepository.findAllAuthorsByOrganizationId(organizationDetails.getId());

		return findCoAuthorsAndMapToDTOs(authors);
	}
	
	@Override
	public Collection<AuthorDetails> fetchAllAuthorsWithCoAuthorsAndPublicationsBetweenYears(
			OrganizationDetails organizationDetails, Date fromYear, Date toYear) {
		logger.info("Fetching authors with publications");

		Collection<AuthorDetails> authorsDetails = fetchAllAuthorsWithCoAuthorsFromOrganization(organizationDetails);

		authorsDetails.parallelStream()
			.filter(a -> a != null)
			.forEach((AuthorDetails authorDetails) -> {
				Collection<Publication> publications = publicationService
						.fetchAllPublicationsByAuthorBetweenYears(authorDetails, fromYear, toYear);
	
				authorDetails.setPublications(publications);
			});

		return authorsDetails;
	}

	@Override
	public Collection<AuthorDetails> fetchAuthorsByIdentifiers(Collection<String> authorsIdentifiers) {
		Collection<AuthorDetails> authors = new ArrayList<>(authorsIdentifiers.size());
		for (String id : authorsIdentifiers) {
			Author author = authorRepository.findAuthorById(id);
			if (author != null) {
				AuthorDetails authorDetails = new AuthorDetails(author);
				authors.add(authorDetails);
			}
		}

		return authors;
	}
	
	private Collection<AuthorDetails> findCoAuthorsAndMapToDTOs(Collection<Author> authors) {
		Collection<AuthorDetails> authorsDetails = new ArrayList<>();
		authors.parallelStream()
			.forEach((Author author) -> {
				AuthorDetails authorDetails = new AuthorDetails(author);

				if (author.isHasCoAuthors()) {
					Collection<String> coAuthorsIdentifiers = authorRepository.findCoAuthorsIdentifiersByAuthorId(author.getId());
					Set<Author> coAuthors = coAuthorsIdentifiers.parallelStream()
							.filter(coAuthorId -> hasCommunication(author.getId(), coAuthorId))
							.map(coAuthorId -> authorRepository.findAuthorById(coAuthorId))
							.filter(coAuthor -> coAuthor != null)
							.collect(Collectors.toSet());
					authorDetails.setCoAuthors(coAuthors);
				}

				authorsDetails.add(authorDetails);
			});
		
		return authorsDetails;
	}

	private boolean hasCommunication(String authorId, String coAuthorId) {
		Collection<String> coAuthorsIdentifiers = authorRepository.findCoAuthorsIdentifiersByAuthorId(coAuthorId);

		return coAuthorsIdentifiers.contains(authorId);
	}

}
