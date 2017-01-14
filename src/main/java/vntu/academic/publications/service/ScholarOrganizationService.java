package vntu.academic.publications.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vntu.academic.publications.dao.AuthorDao;
import vntu.academic.publications.dao.OrganizationDao;
import vntu.academic.publications.dto.OrganizationDTO;
import vntu.academic.publications.helper.CollectionHelper;
import vntu.academic.publications.model.Author;
import vntu.academic.publications.model.Organization;

@Service
public class ScholarOrganizationService implements OrganizationService {
	private static Logger logger = LoggerFactory.getLogger(ScholarOrganizationService.class);

	@Autowired
	private AuthorDao authorDAO;

	@Autowired
	private OrganizationDao orgDAO;

	@Override
	public OrganizationDTO fetchOrganizationByName(final String organizationName) {
		Organization organization = orgDAO.findOrganizationByName(organizationName);

		// find most common organizationId for authors on first page
		if (organization == null) {
			logger.info(
					"Organization '{}' not foud. Trying to choose most common organization for authors from first page",
					organizationName);

			Collection<Author> firstPageAuthors = authorDAO.findAuthorsOnFirstPageByOrganizationName(organizationName);

			firstPageAuthors = findAuthorsDetails(firstPageAuthors);

			String organizationId = mostCommonOrganizationIdentifier(firstPageAuthors);
			
			organization = orgDAO.findOrganizationById(organizationId);

			if (organization == null)
				throw new RuntimeException("Organizatin not found!");
		}
		return new OrganizationDTO(organization);
	}
	
	@Override
	public OrganizationDTO fetchOrganizationById(final String organizationId) {
		Organization org = null;
		for (int i = 0; i < 2; i++) {
			org = orgDAO.findOrganizationById(organizationId);
			if (org != null)
				break;
		}
		if (org == null)
			throw new RuntimeException("Organization not found by id: " + organizationId);

		return new OrganizationDTO(org);
	}
	
	private Collection<Author> findAuthorsDetails(final Collection<Author> authors) {
		return authors.parallelStream()
				.map((Author author) -> authorDAO.findAuthorDetailsByAuthorId(author.getId()))
				.filter((Author author) -> author != null)
				.collect(Collectors.toList());
	}
	
	private List<String> mapAuthorsToOrganizationIdentifiers(final Collection<Author> authors) {
		return authors.stream()
				.filter((Author author) -> author.getOrganizationId() != null)
				.map((Author author) -> author.getOrganizationId())
				.collect(Collectors.toList());
	}
	
	private String mostCommonOrganizationIdentifier(final Collection<Author> authors) {
		List<String> organizationIdentifiers = mapAuthorsToOrganizationIdentifiers(authors);
		return CollectionHelper.mostCommon(organizationIdentifiers, 0.55);
	}

}
