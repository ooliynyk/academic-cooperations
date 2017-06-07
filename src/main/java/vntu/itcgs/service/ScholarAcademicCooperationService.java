package vntu.itcgs.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vntu.itcgs.dto.AuthorDetails;
import vntu.itcgs.dto.CooperationNetwork;
import vntu.itcgs.dto.OrganizationDetails;
import vntu.itcgs.dto.PublicationDetails;
import vntu.itcgs.model.Publication;
import vntu.itcgs.utils.matching.FuzzyNameMatcher;

@Service
public class ScholarAcademicCooperationService implements AcademicCooperationService {

	private static final Logger logger = LoggerFactory.getLogger(ScholarAcademicCooperationService.class);

	private final AuthorService authorService;
	private final OrganizationService organizationService;

	@Autowired
	public ScholarAcademicCooperationService(AuthorService authorService, OrganizationService organizationService) {
		this.authorService = authorService;
		this.organizationService = organizationService;
	}

	@Override
	public CooperationNetwork fetchCoAuthorsCooperationNetwork(String organizationName, boolean coAuthorsVerification) {
		CooperationNetwork cooperationNetwork = new CooperationNetwork();

		OrganizationDetails organization = organizationService.fetchOrganizationByName(organizationName);
		Collection<AuthorDetails> authors = authorService.fetchAllAuthorsWithCoAuthorsFromOrganization(organization, coAuthorsVerification);

		cooperationNetwork.setRootOrganization(organization);

		Collection<AuthorDetails> flatCoAuthors = mapAuthorsToFlatCoAuthorsSet(authors);

		Set<OrganizationDetails> coorgs = mapAuthorsToOrganizationsSet(flatCoAuthors);
		for (OrganizationDetails coorg : coorgs) {
			Collection<AuthorDetails> coorgAuthors = filterAuthorsFromOrganization(coorg.getId(), flatCoAuthors);
			coorg.setCooperationValue(coorgAuthors.size());

			cooperationNetwork.addOrganization(coorg);
		}

		return cooperationNetwork;
	}

	@Override
	public CooperationNetwork fetchPublicationsCooperationNetwork(String organizationName, Date fromYear, Date toYear,
																  boolean coAuthorsVerification) {
		CooperationNetwork cooperationNetwork = new CooperationNetwork();

		OrganizationDetails organization = organizationService.fetchOrganizationByName(organizationName);
		Collection<AuthorDetails> authors = authorService
				.fetchAllAuthorsWithCoAuthorsAndPublicationsBetweenYears(organization, fromYear, toYear, coAuthorsVerification);

		cooperationNetwork.setRootOrganization(organization);

		Collection<PublicationDetails> allPublications = mapAuthorsToFlatPublications(authors);

		Set<OrganizationDetails> coorgs = mapPublicationsToOrganizationsSet(allPublications);
		for (OrganizationDetails coorg : coorgs) {
			Collection<PublicationDetails> publicationsFromOrg = filterPublicationsFromOrganization(coorg.getId(),
					allPublications);
			coorg.setCooperationValue(publicationsFromOrg.size());

			cooperationNetwork.addOrganization(coorg);
		}

		return cooperationNetwork;
	}

	private static Collection<AuthorDetails> filterAuthorsFromOrganization(String organizationId,
			Collection<AuthorDetails> authors) {
		return authors.stream().filter(a -> a.getOrganizationId().equals(organizationId)).collect(Collectors.toList());
	}

	private static Collection<PublicationDetails> filterPublicationsFromOrganization(String targetOrganizationId,
			Collection<PublicationDetails> publications) {
		Collection<PublicationDetails> publicationsFromOrg = new ArrayList<>();

		for (PublicationDetails publication : publications) {
			for (AuthorDetails author : publication.getAuthors()) {
				if (author.getOrganizationId().equals(targetOrganizationId)) {
					publicationsFromOrg.add(publication);
					break;
				}
			}
		}

		return publicationsFromOrg;
	}

	private static Collection<AuthorDetails> findPublicationAuthors(Publication publication,
			FuzzyNameMatcher nameMatcher) {
		Collection<AuthorDetails> publicationAuthors = new ArrayList<>();
		for (String authorName : publication.getAuthorsNames()) {
			AuthorDetails author = nameMatcher.findClosestMatchAuthor(authorName);

			if (author != null)
				publicationAuthors.add(author);
		}

		return publicationAuthors;
	}

	private Collection<PublicationDetails> mapAuthorsToFlatPublications(Collection<AuthorDetails> authors) {
		Collection<AuthorDetails> authorsAndCoauthors = new ArrayList<AuthorDetails>(authors);
		authorsAndCoauthors.addAll(mapAuthorsToFlatCoAuthorsSet(authors));

		FuzzyNameMatcher nameMatcher = new FuzzyNameMatcher(authorsAndCoauthors);
		Collection<PublicationDetails> flatPublications = new ArrayList<>();
		for (AuthorDetails author : authors) {
			Collection<Publication> publications = author.getPublications();

			for (Publication publication : publications) {
				Collection<String> publicationAuthorsNames = publication.getAuthorsNames();
				if (publicationAuthorsNames.size() < 2)
					continue;

				logger.debug("Publication: {}", publication);

				PublicationDetails publicationDetails = new PublicationDetails();
				publicationDetails.setTitle(publication.getTitle());
				publicationDetails.setPublicationId(publication.getId());
				publicationDetails.setAuthors(findPublicationAuthors(publication, nameMatcher));
				publicationDetails.setPublicationDate(publication.getPublicationDate());

				logger.debug("Publication authors: {}", publicationDetails.getAuthors());

				flatPublications.add(publicationDetails);
			}
		}

		return flatPublications;
	}

	private static Collection<AuthorDetails> mapAuthorsToFlatCoAuthorsSet(Collection<AuthorDetails> authors) {
		Collection<AuthorDetails> flatCoAuthors = new ArrayList<>();
		for (AuthorDetails author : authors) {
			if (author != null && author.getCoAuthors() != null) {
				Collection<AuthorDetails> coAuthors = author.getCoAuthors().stream().distinct()
						.map(a -> new AuthorDetails(a)).collect(Collectors.toSet());

				flatCoAuthors.addAll(coAuthors);
			}
		}
		return flatCoAuthors;
	}

	private Set<OrganizationDetails> mapAuthorsToOrganizationsSet(Collection<AuthorDetails> authors) {
		Set<String> orgIdentifiers = authors.stream().map(a -> a.getOrganizationId()).distinct()
				.collect(Collectors.toSet());

		return orgIdentifiers.parallelStream().map(id -> organizationService.fetchOrganizationById(id))
				.filter(org -> org != null).collect(Collectors.toSet());
	}

	private Set<OrganizationDetails> mapPublicationsToOrganizationsSet(Collection<PublicationDetails> publications) {
		Set<AuthorDetails> authors = publications.parallelStream().map(PublicationDetails::getAuthors)
				.flatMap(Collection::stream).distinct().collect(Collectors.toSet());

		return mapAuthorsToOrganizationsSet(authors);
	}

}
