package vntu.academic.publications.crawl.parser;

import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public final class AuthorsOnPageByOrganizationDocumentParser extends AuthorsDocumentParser {

	private static final Pattern NEXT_PAGE_ID_PATTERN = Pattern
			.compile("window.location='\\/citations\\?.*&after_author=([^&'\"]+)");

	AuthorsOnPageByOrganizationDocumentParser(Document doc) {
		super(doc);
	}

	public String parseNextPageId() throws DocumentParsingException {
		String nextPageLocation = null;

		try {
			String windowLocationAttribute = doc.select("button.gs_btnPR").first().attr("onclick");

			String decodedLocation = URLDecoder.decode(windowLocationAttribute.replace("\\x", "%"), "UTF-8");

			Matcher matcher = NEXT_PAGE_ID_PATTERN.matcher(decodedLocation);
			if (matcher.find()) {
				nextPageLocation = matcher.group(1);
			} else {
				throw new DocumentParsingException("Next pageId pattern not found in: " + decodedLocation);
			}
		} catch (Exception e) {
			throw new DocumentParsingException("Document parsing error: " + e.getMessage());
		}

		return nextPageLocation;
	}
	
	public String parseOrganizationName() throws DocumentParsingException {
		final Element organizationNameElement = doc.select(".gsc_authors_header").first();

		if (organizationNameElement == null)
			throw new DocumentParsingException("Organization name not found in document");

		return fetchOrganizationNameFromAuthorsHeader(organizationNameElement);
	}
	
	private static String fetchOrganizationNameFromAuthorsHeader(Element organizationNameElement) {
		final Element helpLabelElement = organizationNameElement.select("a.gsc_org_help_label").first();

		String name = organizationNameElement.text();
		if (helpLabelElement != null) {
			String helpLabel = helpLabelElement.text();
			name = name.replace(helpLabel.trim(), "").trim();
		}
		
		return name;
	}

}
