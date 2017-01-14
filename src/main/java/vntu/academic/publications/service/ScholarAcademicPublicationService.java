package vntu.academic.publications.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vntu.academic.publications.dto.AuthorDTO;
import vntu.academic.publications.dto.OrganizationDTO;
import vntu.academic.publications.dto.PublicationNetworkDTO;
import vntu.academic.publications.model.Author;

@Service
public class ScholarAcademicPublicationService implements AcademicPublicationService {
	
	@Autowired
	private AuthorService authorService;

	@Autowired
	private OrganizationService organizationService;

	@Override
	public PublicationNetworkDTO fetchPublicationNetworkByOrganizationName(String organizationName) {
		PublicationNetworkDTO publicationNetwork = new PublicationNetworkDTO();

		final OrganizationDTO organization = organizationService.fetchOrganizationByName(organizationName);
		final Collection<AuthorDTO> authors = authorService.fetchAllAuthorsWithCoAuthorsByOrganization(organization);
		organization.setAuthors(authors);
		
		publicationNetwork.setRootOrganization(organization);
		
		final Collection<Author> flatCoauthors = new ArrayList<>();
		for (AuthorDTO author : authors) {
			flatCoauthors.addAll(author.getCoauthors());
		}
		
		Set<OrganizationDTO> coorgs = mapAuthorsToOrganizationSet(flatCoauthors);
		for (OrganizationDTO coorg : coorgs) {
			coorg.setAuthors(filterAuthorsFromOrganization(coorg.getId(), flatCoauthors));
			publicationNetwork.addOrganization(coorg);
		}
		
		return publicationNetwork;
	}
	
	private static Collection<AuthorDTO> filterAuthorsFromOrganization(String organizationId, Collection<Author> authors) {
		return authors.stream()
				.filter(a -> a.getOrganizationId().equals(organizationId))
				.map(a -> new AuthorDTO(a))
				.collect(Collectors.toList());
	}
	
	private Set<OrganizationDTO> mapAuthorsToOrganizationSet(Collection<Author> authors) {
		Set<String> orgIdentifiers = authors.stream()
				.map(a -> a.getOrganizationId())
				.distinct()
				.collect(Collectors.toSet());
		
		return orgIdentifiers.parallelStream()
				.map(id -> organizationService.fetchOrganizationById(id))
				.collect(Collectors.toSet());
	}

}
