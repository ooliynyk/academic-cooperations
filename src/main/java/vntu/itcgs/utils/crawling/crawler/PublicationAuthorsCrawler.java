package vntu.itcgs.utils.crawling.crawler;

import java.util.ArrayList;
import java.util.Collection;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class PublicationAuthorsCrawler extends DocumentCrawler<Collection<String>> {

	public PublicationAuthorsCrawler(Document doc) {
		super(doc);
	}

	@Override
	public Collection<String> crawl() throws DocumentCrawlingException {
		Element authorsDiv = null;
		for (Element div : doc.select("div.gs_scl")) {
			String fieldName = div.select("div.gsc_field").text();
			if (fieldName.equals("Authors") || fieldName.equals("Inventors")) {
				authorsDiv = div;
			}
		}

		if (authorsDiv == null)
			throw new DocumentCrawlingException("Authors not specified");

		Collection<String> authors = new ArrayList<>();
		String authorsText = authorsDiv.select("div.gsc_value").text();
		for (String author : authorsText.split(",")) {
			authors.add(author.trim());
		}

		return authors;
	}

}
