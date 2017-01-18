package vntu.academic.publications.service;

import java.util.Arrays;

import vntu.academic.publications.dto.AuthorDTO;
import vntu.academic.publications.dto.OrganizationDTO;
import vntu.academic.publications.dto.PublicationNetworkDTO;
import vntu.academic.publications.model.Author;

public class MockedAcademicPublicationService implements AcademicPublicationService {

	@Override
	public PublicationNetworkDTO fetchPublicationNetworkByOrganizationName(String organizationName) {
		PublicationNetworkDTO publicationNetwork = new PublicationNetworkDTO();

		Author a2 = new Author("a2", "A2", "o2");

		Author a3 = new Author("a3", "A3", "o2");

		Author a4 = new Author("a4", "A4", "o3");

		Author a1 = new Author("a1", "A1", "o1");

		AuthorDTO ad1 = new AuthorDTO(a1);
		AuthorDTO ad2 = new AuthorDTO(a2);
		AuthorDTO ad3 = new AuthorDTO(a3);
		AuthorDTO ad4 = new AuthorDTO(a4);

		ad1.setCoAuthors(Arrays.asList(a2, a3, a4));

		OrganizationDTO o1 = new OrganizationDTO();
		o1.setId("o1");
		o1.setName("Вінницький Національний технічний університет");
		o1.setAuthors(Arrays.asList(ad1));

		OrganizationDTO o2 = new OrganizationDTO();
		o2.setName("Kiev");
		o2.setId("o2");
		o2.setAuthors(Arrays.asList(ad2, ad3));

		OrganizationDTO o3 = new OrganizationDTO();
		o3.setName("University of the aegean");
		o3.setId("o3");
		o3.setAuthors(Arrays.asList(ad4));

		publicationNetwork.setRootOrganization(o1);
		publicationNetwork.addOrganization(o2);
		publicationNetwork.addOrganization(o3);

		return publicationNetwork;
	}

}
