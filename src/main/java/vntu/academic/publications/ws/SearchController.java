package vntu.academic.publications.ws;

import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vntu.academic.publications.dto.PublicationNetworkDTO;
import vntu.academic.publications.service.AcademicPublicationService;
import vntu.academic.publications.service.MockedAcademicPublicationService;

@RestController
@RequestMapping("/publication-network")
public class SearchController {
	private static Logger logger = LoggerFactory.getLogger(SearchController.class);

	@Autowired
	private AcademicPublicationService publicationService;

	@GetMapping("/search")
	public PublicationNetworkDTO search(@RequestParam String university)
			throws InterruptedException, ExecutionException {
		logger.info("Searching organization with authors by name '{}'", university);
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		PublicationNetworkDTO publicationNetwork = publicationService
				.fetchPublicationNetworkByOrganizationName(university);

		stopWatch.stop();
		logger.info("Searching was finished, elapsed time: {}s", stopWatch.getTotalTimeSeconds());

		return publicationNetwork;
	}

	@GetMapping("/search-mock")
	public PublicationNetworkDTO searchTest(@RequestParam String university)
			throws InterruptedException, ExecutionException {
		logger.info("Searching mock", university);
		
		return new MockedAcademicPublicationService().fetchPublicationNetworkByOrganizationName(university);
	}

}
