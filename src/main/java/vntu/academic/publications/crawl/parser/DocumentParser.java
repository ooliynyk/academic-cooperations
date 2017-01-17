package vntu.academic.publications.crawl.parser;

import org.jsoup.nodes.Document;

public abstract class DocumentParser {
	
	protected final Document doc;

	DocumentParser(Document doc) {
		this.doc = doc;
	}

}
