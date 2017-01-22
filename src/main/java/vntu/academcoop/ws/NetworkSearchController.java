package vntu.academcoop.ws;

import java.util.Date;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vntu.academcoop.dto.CooperationNetworkDTO;
import vntu.academcoop.service.AcademicCooperationService;
import vntu.academcoop.service.MockedAcademicPublicationService;

@RestController
@RequestMapping("/network")
public class NetworkSearchController {
	private static Logger logger = LoggerFactory.getLogger(NetworkSearchController.class);

	@Autowired
	private AcademicCooperationService cooperationService;

	@GetMapping("/by-coauthors")
	public CooperationNetworkDTO searchByCoAtuhors(@RequestParam String university)
			throws InterruptedException, ExecutionException {
		logger.info("Searching cooperation by co-authors for organization '{}'", university);

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		CooperationNetworkDTO publicationNetwork = cooperationService.fetchCoAuthorsCooperationNetwork(university);

		stopWatch.stop();
		logger.info("Searching was finished, elapsed time: {}s", stopWatch.getTotalTimeSeconds());

		return publicationNetwork;
	}

	@GetMapping("/by-publications")
	public CooperationNetworkDTO searchByPublications(@RequestParam String university,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy") Date fromYear,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy") Date toYear)
			throws InterruptedException, ExecutionException {
		logger.info("Searching cooperation by publications for organization '{}'", university);

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		CooperationNetworkDTO publicationNetwork = cooperationService
				.fetchPublicationsCooperationNetworkInYears(university, fromYear, toYear);

		stopWatch.stop();
		logger.info("Searching was finished, elapsed time: {}s", stopWatch.getTotalTimeSeconds());

		return publicationNetwork;
	}

	@GetMapping("/search-mock")
	public CooperationNetworkDTO searchTest(@RequestParam String university)
			throws InterruptedException, ExecutionException {
		logger.info("Searching mock", university);

		return new MockedAcademicPublicationService().fetchCoAuthorsCooperationNetwork(university);
	}

}
