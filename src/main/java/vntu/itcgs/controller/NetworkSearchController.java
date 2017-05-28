package vntu.itcgs.controller;

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

import vntu.itcgs.dto.CooperationNetwork;
import vntu.itcgs.service.AcademicCooperationService;

@RestController
@RequestMapping("/network")
public class NetworkSearchController {
	private static Logger logger = LoggerFactory.getLogger(NetworkSearchController.class);

	@Autowired
	private AcademicCooperationService cooperationService;

	@GetMapping("/by-coauthors")
	public CooperationNetwork searchByCoAtuhors(@RequestParam String university)
			throws InterruptedException, ExecutionException {
		logger.info("Searching cooperation by co-authors for organization '{}'", university);

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		CooperationNetwork publicationNetwork = cooperationService.fetchCoAuthorsCooperationNetwork(university);

		stopWatch.stop();
		logger.info("Searching was finished, elapsed time: {}s", stopWatch.getTotalTimeSeconds());

		return publicationNetwork;
	}

	@GetMapping("/by-publications")
	public CooperationNetwork searchByPublications(@RequestParam String university,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy") Date fromYear,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy") Date toYear)
			throws InterruptedException, ExecutionException {
		logger.info("Searching cooperation by publications for organization '{}'", university);

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		CooperationNetwork publicationNetwork = cooperationService
				.fetchPublicationsCooperationNetwork(university, fromYear, toYear);

		stopWatch.stop();
		logger.info("Searching was finished, elapsed time: {}s", stopWatch.getTotalTimeSeconds());

		return publicationNetwork;
	}

}
