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

import vntu.academcoop.dto.AuthorDTO;
import vntu.academcoop.dto.CooperationNetworkDTO;
import vntu.academcoop.dto.OrganizationDTO;
import vntu.academcoop.dto.PublicationDTO;
import vntu.academcoop.model.Publication;
import vntu.academcoop.utils.matching.FuzzyNameMatcher;

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
	public CooperationNetworkDTO fetchCoAuthorsCooperationNetwork(String organizationName) {
		CooperationNetworkDTO cooperationNetwork = new CooperationNetworkDTO();

		OrganizationDTO organization = organizationService.fetchOrganizationByName(organizationName);
		Collection<AuthorDTO> authors = authorService.fetchAllAuthorsWithCoAuthorsFromOrganization(organization);

		cooperationNetwork.setRootOrganization(organization);

		Collection<AuthorDTO> flatCoAuthors = mapAuthorsToFlatCoAuthorsSet(authors);

		Set<OrganizationDTO> coorgs = mapAuthorsToOrganizationsSet(flatCoAuthors);
		for (OrganizationDTO coorg : coorgs) {
			Collection<AuthorDTO> coorgAuthors = filterAuthorsFromOrganization(coorg.getId(), flatCoAuthors);
			coorg.setCooperationValue(coorgAuthors.size());

			cooperationNetwork.addOrganization(coorg);
		}

		return cooperationNetwork;
	}

	@Override
	public CooperationNetworkDTO fetchPublicationsCooperationNetworkInYears(String organizationName, Date fromYear,
			Date toYear) {
		CooperationNetworkDTO cooperationNetwork = new CooperationNetworkDTO();

		OrganizationDTO organization = organizationService.fetchOrganizationByName(organizationName);
		Collection<AuthorDTO> authors = authorService
				.fetchAllAuthorsWithCoAuthorsAndPublicationsBetweenYears(organization, fromYear, toYear);

		cooperationNetwork.setRootOrganization(organization);

		Collection<PublicationDTO> allPublications = mapAuthorsToFlatPublications(authors);

		Set<OrganizationDTO> coorgs = mapPublicationsToOrganizationsSet(allPublications);
		for (OrganizationDTO coorg : coorgs) {
			Collection<PublicationDTO> publicationsFromOrg = filterPublicationsFromOrganization(coorg.getId(),
					allPublications);
			coorg.setCooperationValue(publicationsFromOrg.size());

			cooperationNetwork.addOrganization(coorg);
		}

		return cooperationNetwork;
	}

	private static Collection<AuthorDTO> filterAuthorsFromOrganization(String organizationId,
			Collection<AuthorDTO> authors) {
		return authors.stream().filter(a -> a.getOrganizationId().equals(organizationId)).collect(Collectors.toList());
	}

	private static Collection<PublicationDTO> filterPublicationsFromOrganization(String targetOrganizationId,
			Collection<PublicationDTO> publications) {
		Collection<PublicationDTO> publicationsFromOrg = new ArrayList<>();

		for (PublicationDTO publication : publications) {
			for (AuthorDTO author : publication.getAuthors()) {
				if (author.getOrganizationId().equals(targetOrganizationId)) {
					publicationsFromOrg.add(publication);
					break;
				}
			}
		}

		return publicationsFromOrg;
	}

	private static Collection<AuthorDTO> findPublicationAuthors(Publication publication, FuzzyNameMatcher nameMatcher) {
		Collection<AuthorDTO> publicationAuthors = new ArrayList<>();
		for (String authorName : publication.getAuthorsNames()) {
			AuthorDTO author = nameMatcher.findClosestMatchAuthor(authorName);

			if (author != null)
				publicationAuthors.add(author);
		}

		return publicationAuthors;
	}

	private Collection<PublicationDTO> mapAuthorsToFlatPublications(Collection<AuthorDTO> authors) {
		Collection<AuthorDTO> authorsAndCoauthors = new ArrayList<AuthorDTO>(authors);
		authorsAndCoauthors.addAll(mapAuthorsToFlatCoAuthorsSet(authors));

		FuzzyNameMatcher nameMatcher = new FuzzyNameMatcher(authorsAndCoauthors);
		Collection<PublicationDTO> flatPublications = new ArrayList<>();
		for (AuthorDTO author : authors) {
			Collection<Publication> publications = author.getPublications();

			for (Publication publication : publications) {
				Collection<String> publicationAuthorsNames = publication.getAuthorsNames();
				if (publicationAuthorsNames.size() < 2)
					continue;

				logger.debug("Publication: {}", publication);

				PublicationDTO publicationDTO = new PublicationDTO();
				publicationDTO.setTitle(publication.getTitle());
				publicationDTO.setPublicationId(publication.getId());
				publicationDTO.setAuthors(findPublicationAuthors(publication, nameMatcher));
				publicationDTO.setPublicationDate(publication.getPublicationDate());

				logger.debug("Publication authors: {}", publicationDTO.getAuthors());

				flatPublications.add(publicationDTO);
			}
		}

		return flatPublications;
	}

	private static Collection<AuthorDTO> mapAuthorsToFlatCoAuthorsSet(Collection<AuthorDTO> authors) {
		Collection<AuthorDTO> flatCoAuthors = new ArrayList<>();
		for (AuthorDTO author : authors) {
			Collection<AuthorDTO> coAuthors = author.getCoAuthors().stream().distinct().map(a -> new AuthorDTO(a))
					.collect(Collectors.toSet());

			flatCoAuthors.addAll(coAuthors);
		}
		return flatCoAuthors;
	}

	private Set<OrganizationDTO> mapAuthorsToOrganizationsSet(Collection<AuthorDTO> authors) {
		Set<String> orgIdentifiers = authors.stream().map(a -> a.getOrganizationId()).distinct()
				.collect(Collectors.toSet());

		return orgIdentifiers.parallelStream().map(id -> organizationService.fetchOrganizationById(id))
				.filter(org -> org != null).collect(Collectors.toSet());
	}

	private Set<OrganizationDTO> mapPublicationsToOrganizationsSet(Collection<PublicationDTO> publications) {
		Set<AuthorDTO> authors = publications.parallelStream().map(PublicationDTO::getAuthors)
				.flatMap(Collection::stream).distinct().collect(Collectors.toSet());

		return mapAuthorsToOrganizationsSet(authors);
	}

}
