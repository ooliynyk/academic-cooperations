package vntu.itcgs.utils.crawling.crawler;

import org.jsoup.nodes.Document;

public abstract class DocumentCrawler<T> {
	protected final Document doc;

	public DocumentCrawler(Document doc) {
		this.doc = doc;
	}
	
	public abstract T crawl() throws DocumentCrawlingException;
	
}
