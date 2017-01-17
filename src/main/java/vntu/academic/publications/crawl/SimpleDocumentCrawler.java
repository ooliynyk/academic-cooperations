package vntu.academic.publications.crawl;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SimpleDocumentCrawler implements DocumentCrawler {
	private static final Logger logger = LoggerFactory.getLogger(SimpleDocumentCrawler.class);

	public Document fetchDocument(String url) throws DocumentCrawlingException {
		Document doc = null;
		for (int i = 1; i <= 3; i++) {
			try {
				doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Maemo; Linux armv7l; rv:10.0.1) Gecko/20100101 Firefox/10.0.1 Fennec/10.0.1")
						.timeout(3000 * i)
						.get();
				break;
			} catch (HttpStatusException e) {
				throw new DocumentCrawlingException("Document fetching blocked", e);
			} catch (Exception e) {
				logger.warn("Document fetching from '{}' error", url, e);
				try {
					Thread.sleep(1000*i);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
		if (doc == null)
			throw new DocumentCrawlingException("Document fetching error");

		return doc;
	}

}
