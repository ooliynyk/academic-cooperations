package vntu.academcoop.crawl;

import org.jsoup.nodes.Document;

public interface DocumentParser {

	Document parseDocument(String url) throws DocumentParsingException;

}
