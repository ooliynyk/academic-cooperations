package vntu.academic.publications.crawl.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public final class SearchAuthorByNameDocumentParser extends DocumentParser {

	private static final Pattern USER_ID_PATTERN = Pattern.compile("\\/citations\\?user=([^&]+)");

	SearchAuthorByNameDocumentParser(Document doc) {
		super(doc);
	}

	public String parseAuthorId() throws DocumentParsingException {
		final Elements searchResultElements = doc.select("div.gs_r");

		for (Element searchResultElement : searchResultElements) {
			final Element authorsElement = searchResultElement.select("div.gs_a").first();
			final Elements authorLinkElements = authorsElement.select("a");

			for (Element authorLinkElement : authorLinkElements) {
				final Elements childrenElements = authorLinkElement.children();
				final Element targetAuthorElement = childrenElements.select("b").first();

				String authorHrefAttribute = authorLinkElement.attr("href");
				if (targetAuthorElement != null && !authorHrefAttribute.isEmpty()) {
					return fetchUserIdFromUserCitationAttribute(authorHrefAttribute);
				}
			}
		}

		return null;
	}

	private static String fetchUserIdFromUserCitationAttribute(String userCitationAttribute)
			throws DocumentParsingException {
		String userId = null;

		Matcher userIdMatcher = USER_ID_PATTERN.matcher(userCitationAttribute);
		if (userIdMatcher.find()) {
			userId = userIdMatcher.group(1);
		}

		if (userId == null) {
			throw new DocumentParsingException("User id not found in: " + userCitationAttribute);
		}

		return userId;
	}

}
