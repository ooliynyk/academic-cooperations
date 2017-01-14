package vntu.academic.publications.crawl;

import org.jsoup.nodes.Document;

public interface DocumentCrawler {

	Document fetchDocument(String url) throws DocumentCrawlingException;

}
