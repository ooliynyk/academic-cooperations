package vntu.academic.publications.crawl.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import vntu.academic.publications.model.Publication;

public class PublicationsOnAuthorPageDocumentParser extends DocumentParser {

	private static final SimpleDateFormat PUBLICATION_DATE_FORMAT = new SimpleDateFormat("yyyy");

	private static final Pattern PUBLICATION_ID_PATTERN = Pattern.compile("\\/citations\\?.*citation_for_view=([^&]+)");

	PublicationsOnAuthorPageDocumentParser(Document doc) {
		super(doc);
	}

	public Collection<Publication> parsePublications() {
		Element publicationsTableElement = doc.select("table#gsc_a_t").first();
		Elements publicationElements = publicationsTableElement.select("tbody#gsc_a_b").select("tr.gsc_a_tr");

		Collection<Publication> publications = new ArrayList<>();
		for (Element publicationBlockElement : publicationElements) {
			Publication publication = fetchPublication(publicationBlockElement);
			publications.add(publication);
		}

		return publications;
	}
	
	public boolean hasMorePublications() {
		Element showMoreButton = doc.select("button#gsc_bpf_more.gs_btn_smd").first();
		
		return !showMoreButton.hasAttr("disabled");
	}

	private static Publication fetchPublication(Element publicationBlockElement) {
		String publicationTitle = fetchPublicationTitle(publicationBlockElement);
		String publicationId = fetchPublicationId(publicationBlockElement);
		Collection<String> authorNames = fetchAuthors(publicationBlockElement);
		Date publicationDate = fetchPublicationYear(publicationBlockElement);

		return new Publication(publicationId, publicationTitle, authorNames, publicationDate);
	}

	private static String fetchPublicationTitle(Element publicationBlockElement) {
		Element descriptionElement = publicationBlockElement.select("td.gsc_a_t").first();
		Element publicationLinkElement = descriptionElement.select("a.gsc_a_at").first();

		return publicationLinkElement.text();
	}

	private static String fetchPublicationId(Element publicationBlockElement) {
		String publicationId = null;

		Element descriptionElement = publicationBlockElement.select("td.gsc_a_t").first();
		Element publicationLinkElement = descriptionElement.select("a.gsc_a_at").first();
		String publicationHref = publicationLinkElement.attr("href");

		Matcher matcher = PUBLICATION_ID_PATTERN.matcher(publicationHref);
		if (matcher.find()) {
			publicationId = matcher.group(1);
		}

		return publicationId;
	}

	private static Collection<String> fetchAuthors(Element publicationBlockElement) {
		Element descriptionElement = publicationBlockElement.select("td.gsc_a_t").first();
		Element authorsElement = descriptionElement.select("div.gs_gray").first();
		
		String[] authors = authorsElement.text().split(",");
		for (int i = 0; i < authors.length; i++) {
			authors[i] = authors[i].trim();
		}

		return Arrays.asList(authors);
	}

	private static Date fetchPublicationYear(Element publicationBlockElement) {
		Date year = null;

		Element yearElement = publicationBlockElement.select("td.gsc_a_y").select("span.gsc_a_h").first();

		String yearText = yearElement.text();
		if (!yearText.isEmpty()) {
			try {
				year = PUBLICATION_DATE_FORMAT.parse(yearText);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		return year;
	}

}
