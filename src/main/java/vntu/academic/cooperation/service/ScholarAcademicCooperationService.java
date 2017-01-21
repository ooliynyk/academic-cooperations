package vntu.academic.cooperation.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vntu.academic.cooperation.dto.AuthorDTO;
import vntu.academic.cooperation.dto.CooperationNetworkDTO;
import vntu.academic.cooperation.dto.OrganizationDTO;
import vntu.academic.cooperation.dto.PublicationDTO;

@Service
public class ScholarAcademicCooperationService implements AcademicCooperationService {

	private final AuthorService authorService;
	private final OrganizationService organizationService;
	
	@Autowired
	public ScholarAcademicCooperationService(AuthorService authorService, OrganizationService organizationService) {
		super();
		this.authorService = authorService;
		this.organizationService = organizationService;
	}

	@Override
	public CooperationNetworkDTO fetchCoAuthorsCooperationNetwork(String organizationName) {
		CooperationNetworkDTO cooperationNetwork = new CooperationNetworkDTO();

		OrganizationDTO organization = organizationService.fetchOrganizationByName(organizationName);
		Collection<AuthorDTO> authors = authorService.fetchAllAuthorsWithCoAuthorsByOrganization(organization);
		organization.setCooperationValue(authors.size());

		cooperationNetwork.setRootOrganization(organization);

		Collection<AuthorDTO> flatCoAuthors = new ArrayList<>();
		for (AuthorDTO author : authors) {
			Collection<AuthorDTO> coAuthors = author.getCoAuthors().stream().map(a -> new AuthorDTO(a))
					.collect(Collectors.toList());
			flatCoAuthors.addAll(coAuthors);
		}

		Set<OrganizationDTO> coorgs = mapAuthorsToOrganizationSet(flatCoAuthors);
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

		/*OrganizationDTO organization = organizationService.fetchOrganizationByName(organizationName);
		Collection<AuthorDTO> authors = authorService
				.fetchAllAuthorsWithPublicationsByOrganizationBetweenYears(organization, fromYear, toYear);
		
		cooperationNetwork.setRootOrganization(organization);

		Collection<PublicationDTO> allPublications = new ArrayList<>();
		for (AuthorDTO author : authors) {
			Collection<Publication> publications = author.getPublications();

			for (Publication publication : publications) {
				if (publication.getAuthorNames().size() < 2)
					continue;

				PublicationDTO publicationDTO = new PublicationDTO();
				publicationDTO.setTitle(publication.getTitle());
				publicationDTO.setPublicationId(publication.getPublicationId());
				publicationDTO.setPublicationDate(publication.getPublicationDate());

				Collection<String> publicationAuthorsNames = publication.getAuthorNames();
				Collection<AuthorDTO> publicationAuthors = authorService.fetchAuthorsByIdentifiers(publicationAuthorsNames);
				
				// if not all authors found by names, need to check again.
				if (publicationAuthors.size() < 2) 
					continue;
				
				publicationDTO.setAuthors(publicationAuthors);

				allPublications.add(publicationDTO);
			}
		}

		Set<OrganizationDTO> coorgs = mapPublicationsToOrganizationSet(allPublications);
		for (OrganizationDTO coorg : coorgs) {
			Collection<PublicationDTO> publicationsFromOrg = filterPublicationsFromOrganization(coorg.getId(),
					allPublications);

			coorg.setCooperationValue(publicationsFromOrg.size());

			cooperationNetwork.addOrganization(coorg);
		}*/

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

	private Set<OrganizationDTO> mapAuthorsToOrganizationSet(Collection<AuthorDTO> authors) {
		Set<String> orgIdentifiers = authors.stream().map(a -> a.getOrganizationId()).distinct()
				.collect(Collectors.toSet());

		return orgIdentifiers.parallelStream().map(id -> organizationService.fetchOrganizationById(id))
				.collect(Collectors.toSet());
	}

	private Set<OrganizationDTO> mapPublicationsToOrganizationSet(Collection<PublicationDTO> publications) {
		Set<AuthorDTO> authors = publications.parallelStream().map(PublicationDTO::getAuthors)
				.flatMap(Collection::stream).distinct().collect(Collectors.toSet());

		return mapAuthorsToOrganizationSet(authors);
	}

}
