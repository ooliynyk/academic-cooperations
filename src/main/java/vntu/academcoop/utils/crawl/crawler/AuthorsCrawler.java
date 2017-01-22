package vntu.academcoop.utils.crawl.crawler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public final class AuthorsCrawler extends DocumentCrawler<Collection<String>> {

	private static final Pattern USER_ID_PATTERN = Pattern.compile("\\/citations\\?.*user=([^&]+)");

	public AuthorsCrawler(Document doc) {
		super(doc);
	}

	@Override
	public Collection<String> crawl() throws DocumentCrawlingException {
		final Elements userElements = doc.select("div.gsc_1usr");

		Collection<String> authorsIdentifiers = new ArrayList<>();
		for (Element user : userElements) {
			final Element userCitationElement = user.select("h3.gsc_1usr_name a").first();

			// for check whether author specify his organization
			final Element userStaffElement = user.select("div.gsc_1usr_aff").first();

			if (userCitationElement == null || userStaffElement == null)
				continue;

			String userCitationAttribute = userCitationElement.attr("href");

			String authorId = fetchUserIdFromUserCitationAttribute(userCitationAttribute);

			authorsIdentifiers.add(authorId);
		}

		return authorsIdentifiers;
	}

	private static String fetchUserIdFromUserCitationAttribute(String userCitationAttribute)
			throws DocumentCrawlingException {
		Matcher matcher = USER_ID_PATTERN.matcher(userCitationAttribute);
		String userId = matcher.find() ? matcher.group(1) : null;

		if (userId == null)
			throw new DocumentCrawlingException("Author identifier not found");

		return userId;
	}

}
