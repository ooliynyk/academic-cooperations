package vntu.itcgs.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vntu.itcgs.dto.AuthorDetails;
import vntu.itcgs.dto.OrganizationDetails;
import vntu.itcgs.model.Author;
import vntu.itcgs.model.Publication;
import vntu.itcgs.repository.AuthorRepository;

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
	public Collection<AuthorDetails> fetchAllAuthorsWithCoAuthorsFromOrganization(OrganizationDetails organizationDetails,
																				  boolean coAuthorsVerification) {
		logger.info("Fetching authors with co-authors");

		Collection<Author> authors = authorRepository.findAllAuthorsByOrganizationId(organizationDetails.getId());

		return findCoAuthorsAndMapToDTOs(authors, coAuthorsVerification);
	}
	
	@Override
	public Collection<AuthorDetails> fetchAllAuthorsWithCoAuthorsAndPublicationsBetweenYears(
			OrganizationDetails organizationDetails, Date fromYear, Date toYear, boolean coAuthorsVerification) {
		logger.info("Fetching authors with publications");

		Collection<AuthorDetails> authorsDetails =
				fetchAllAuthorsWithCoAuthorsFromOrganization(organizationDetails, coAuthorsVerification);

		authorsDetails.parallelStream()
			.filter(Objects::nonNull)
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
	
	private Collection<AuthorDetails> findCoAuthorsAndMapToDTOs(Collection<Author> authors, boolean coAuthorsVerification) {
		Collection<AuthorDetails> authorsDetails = new ArrayList<>();
		authors.parallelStream()
			.filter(Objects::nonNull)
			.forEach((Author author) -> {
				AuthorDetails authorDetails = new AuthorDetails(author);

				if (author.isHasCoAuthors()) {
					Collection<String> coAuthorsIdentifiers = authorRepository.findCoAuthorsIdentifiersByAuthorId(author.getId());
					Set<Author> coAuthors = coAuthorsIdentifiers.parallelStream()
							.filter(coAuthorId -> coAuthorsVerification ? hasCommunication(author.getId(), coAuthorId) : true)
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
