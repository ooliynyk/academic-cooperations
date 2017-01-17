package vntu.academic.publications.crawl.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import vntu.academic.publications.model.Author;

public abstract class AuthorsDocumentParser extends DocumentParser {

	protected static final Pattern USER_ID_PATTERN = Pattern.compile("\\/citations\\?user=([^&]+)");

	AuthorsDocumentParser(Document doc) {
		super(doc);
	}

	public Collection<Author> parseAuthors() throws DocumentParsingException {
		final Elements userElements = doc.select("div.gsc_1usr");

		Collection<Author> authors = new ArrayList<>();
		for (Element user : userElements) {
			final Element userCitationElement = user.select("h3.gsc_1usr_name a").first();

			// for check whether author specify his organization
			final Element userStaffElement = user.select("div.gsc_1usr_aff").first();

			if (userCitationElement == null || userStaffElement == null)
				continue;

			String userCitationAttribute = userCitationElement.attr("href");

			String authorId = fetchUserIdFromUserCitationAttribute(userCitationAttribute);
			String authorName = userCitationElement.text();
			Author author = new Author(authorId, authorName, null);

			authors.add(author);
		}

		return authors;
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
