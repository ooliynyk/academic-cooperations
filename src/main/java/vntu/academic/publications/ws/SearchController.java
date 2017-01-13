package vntu.academic.publications.ws;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import vntu.academic.publications.dto.AuthorDTO;
import vntu.academic.publications.dto.OrganizationDTO;
import vntu.academic.publications.dto.PublicationNetworkDTO;
import vntu.academic.publications.entities.Author;
import vntu.academic.publications.services.AcademicPublicationService;

@RestController
@RequestMapping("/publication-network")
public class SearchController {
	private static Logger logger = LoggerFactory.getLogger(SearchController.class);

	@Autowired
	private AcademicPublicationService publicationService;

	@RequestMapping("/search")
	public @ResponseBody PublicationNetworkDTO search(@RequestParam String university)
			throws InterruptedException, ExecutionException {
		logger.info("Searching organization with authors by name '{}'", university);
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		PublicationNetworkDTO publicationNetwork = publicationService.fetchPublicationNetworkByOrganizationName(university);
		
		stopWatch.stop();
		logger.info("Searching was finished, elapsed time: {}s", stopWatch.getTotalTimeSeconds());
		
		return publicationNetwork;
	}
	
	@RequestMapping("/searcht")
	public @ResponseBody PublicationNetworkDTO searchTest(@RequestParam String university)
			throws InterruptedException, ExecutionException {
		logger.info("Searching organization with authors by name '{}'", university);
		
		PublicationNetworkDTO pn = new PublicationNetworkDTO();
		

		Author a2 = new Author("a2", "A2", "o2");
		
		Author a3 = new Author("a3", "A3", "o2");
		
		Author a4 = new Author("a4", "A4", "o3");
		
		Author a1 = new Author("a1", "A1", "o1");
		
		AuthorDTO ad1 = new AuthorDTO(a1);
		AuthorDTO ad2 = new AuthorDTO(a2);
		AuthorDTO ad3 = new AuthorDTO(a3);
		AuthorDTO ad4 = new AuthorDTO(a4);
		
		ad1.setCoauthors(Arrays.asList(a2, a3, a4));

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
		
		pn.setRootOrganization(o1);
		pn.addOrganization(o2);
		pn.addOrganization(o3);
		
		return pn;
	}

}
