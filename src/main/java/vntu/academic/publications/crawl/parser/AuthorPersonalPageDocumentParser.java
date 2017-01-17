package vntu.academic.publications.crawl.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public final class AuthorPersonalPageDocumentParser extends DocumentParser {

	private static final Pattern ORGANIZATION_PATTERN = Pattern.compile("\\/citations\\?.*org=([^&]+)");

	AuthorPersonalPageDocumentParser(Document doc) {
		super(doc);
	}

	public String parseAuthorName() throws DocumentParsingException {
		final Element userNameElement = doc.select("div#gsc_prf_in").first();

		if (userNameElement == null)
			throw new DocumentParsingException("Author details not specified");

		return userNameElement.text();
	}

	public String parseOrganizationId() throws DocumentParsingException {
		final Element userStaffElement = doc.select("div.gsc_prf_il a").first();

		if (userStaffElement == null)
			throw new DocumentParsingException("Author details not specified");

		String userStaffAttribute = userStaffElement.attr("href");

		return fetchOrganizationIdFromUserStaffAttribute(userStaffAttribute);
	}

	private static String fetchOrganizationIdFromUserStaffAttribute(String userStaffAttribute)
			throws DocumentParsingException {
		String organizationId = null;

		Matcher userOgranizationMatcher = ORGANIZATION_PATTERN.matcher(userStaffAttribute);
		if (userOgranizationMatcher.find()) {
			organizationId = userOgranizationMatcher.group(1);
		} else {
			throw new DocumentParsingException("Organization pattern not found in: " + userStaffAttribute);
		}

		return organizationId;
	}

}
