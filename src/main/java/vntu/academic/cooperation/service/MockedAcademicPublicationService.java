package vntu.academic.cooperation.service;

import java.util.Arrays;
import java.util.Date;

import vntu.academic.cooperation.dto.AuthorDTO;
import vntu.academic.cooperation.dto.CooperationNetworkDTO;
import vntu.academic.cooperation.dto.OrganizationDTO;
import vntu.academic.cooperation.model.Author;

public class MockedAcademicPublicationService implements AcademicCooperationService {

	@Override
	public CooperationNetworkDTO fetchCoAuthorsCooperationNetwork(String organizationName) {
		CooperationNetworkDTO publicationNetwork = new CooperationNetworkDTO();

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
		o1.setCooperationValue((Arrays.asList(ad1).size()));

		OrganizationDTO o2 = new OrganizationDTO();
		o2.setName("Kiev");
		o2.setId("o2");
		o2.setCooperationValue(Arrays.asList(ad2, ad3).size());

		OrganizationDTO o3 = new OrganizationDTO();
		o3.setName("University of the aegean");
		o3.setId("o3");
		o3.setCooperationValue(Arrays.asList(ad4).size());

		publicationNetwork.setRootOrganization(o1);
		publicationNetwork.addOrganization(o2);
		publicationNetwork.addOrganization(o3);

		return publicationNetwork;
	}

	@Override
	public CooperationNetworkDTO fetchPublicationsCooperationNetworkInYears(String organizationName, Date fromYear,
			Date toYear) {
		// TODO Auto-generated method stub
		return null;
	}

}
