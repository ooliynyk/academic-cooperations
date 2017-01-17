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
		String authorId = null;

		final Elements searchResultElements = doc.select("div.gs_r");

		if (!searchResultElements.isEmpty()) {
			final Element authorsElement = searchResultElements.select("div.gs_a").first();
			final Elements authorLinkElements = authorsElement.select("a");

			for (Element authorLinkElement : authorLinkElements) {
				final Elements childrenElements = authorLinkElement.children();
				final Element targetAuthorElement = childrenElements.select("b").first();

				if (targetAuthorElement != null) {
					String authorHrefAttribute = authorLinkElement.attr("href");
					authorId = fetchUserIdFromUserCitationAttribute(authorHrefAttribute);

					break;
				}
			}
		}

		return authorId;
	}

	private static String fetchUserIdFromUserCitationAttribute(String userCitationAttribute)
			throws DocumentParsingException {
		String userId = null;

		Matcher userIdMatcher = USER_ID_PATTERN.matcher(userCitationAttribute);
		if (userIdMatcher.find()) {
			userId = userIdMatcher.group(1);
		} else {
			throw new DocumentParsingException("User id pattern not found in: " + userCitationAttribute);
		}

		return userId;
	}

}
