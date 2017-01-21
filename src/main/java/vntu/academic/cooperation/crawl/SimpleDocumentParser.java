package vntu.academic.cooperation.crawl;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleDocumentParser implements DocumentParser {
	
	private static final Logger logger = LoggerFactory.getLogger(SimpleDocumentParser.class);

	public Document parseDocument(String url) throws DocumentParsingException {
		Document doc = null;
		for (int i = 1; i <= 3; i++) {
			try {
				doc = Jsoup.connect(url)
						.userAgent(
								"Mozilla/5.0 (Maemo; Linux armv7l; rv:10.0.1) Gecko/20100101 Firefox/10.0.1 Fennec/10.0.1")
						.timeout(3000 * i).get();
				break;
			} catch (HttpStatusException e) {
				throw new DocumentParsingException("Document parsing blocked", e);
			} catch (Exception e) {
				logger.warn("Document fetching from '{}' error", url, e);
				try {
					Thread.sleep(1000 * i);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
		if (doc == null)
			throw new DocumentParsingException("Document parsing error");

		return doc;
	}

}
