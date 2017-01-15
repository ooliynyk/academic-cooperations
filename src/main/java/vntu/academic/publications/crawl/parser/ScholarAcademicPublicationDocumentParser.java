package vntu.academic.publications.crawl.parser;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import vntu.academic.publications.crawl.DocumentCrawlingException;
import vntu.academic.publications.crawl.ScholarDocumentProvider;
import vntu.academic.publications.model.Author;
import vntu.academic.publications.model.Organization;

@Service
public class ScholarAcademicPublicationDocumentParser implements AcademicPublicationDocumentParser {

	private static final Pattern USER_ID_PATTERN = Pattern.compile("\\/citations\\?user=([^&]+)");
	private static final Pattern NEXT_PAGE_ID_PATTERN = Pattern
			.compile("window.location='\\/citations\\?.*&after_author=([^&'\"]+)");
	private static final Pattern ORGANIZATION_PATTERN = Pattern.compile("\\/citations\\?.*org=([^&]+)");

	@Override
	public Author parseAuthorWithOrganizationId(Document doc, String authorId) throws DocumentParsingException {
		Author author = null;
		final Element userNameElement = doc.select("div#gsc_prf_in").first();
		final Element userStaffElement = doc.select("div.gsc_prf_il a").first();

		if (userNameElement == null || userStaffElement == null)
			throw new DocumentParsingException("Author details not specified");

		String userStaffAttribute = userStaffElement.attr("href");
		Matcher userOgranizationMatcher = ORGANIZATION_PATTERN.matcher(userStaffAttribute);
		if (userOgranizationMatcher.find()) {
			String ogranizationId = userOgranizationMatcher.group(1);
			String authorName = userNameElement.text();

			author = new Author(authorId, authorName, ogranizationId);
		} else {
			throw new DocumentParsingException("Organization pattern not found in: " + userStaffAttribute);
		}

		return author;
	}

	@Override
	public Collection<Author> parseAuthors(Document doc) throws DocumentParsingException {
		final Elements userElements = doc.select("div.gsc_1usr");

		Collection<Author> authors = new ArrayList<>();
		for (Element user : userElements) {
			final Element userCitationElement = user.select("h3.gsc_1usr_name a").first();

			// for check whether author specify his organization
			final Element userStaffElement = user.select("div.gsc_1usr_aff").first();

			if (userCitationElement == null || userStaffElement == null)
				continue;

			String userCitationAttribute = userCitationElement.attr("href");

			String authorId = parseUserIdFromUserCitationAttribute(userCitationAttribute);
			String authorName = userCitationElement.text();
			Author author = new Author(authorId, authorName, null);

			authors.add(author);
		}

		return authors;
	}

	@Override
	public String parseNextPageId(Document doc) throws DocumentParsingException {
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

	@Override
	public Organization parseOrganization(Document doc) throws DocumentParsingException {
		Organization organization = null;

		final Element organizationElement = doc.select("div.gsc_instbox_sec").select(".gsc_inst_res").first();

		if (organizationElement == null)
			throw new DocumentParsingException("Organization element not found");

		final Element organizationLink = organizationElement.select("a").first();
		final Element organizationSpan = organizationElement.select("span.gs_gray").first();

		if (organizationLink == null)
			throw new DocumentParsingException("Organization link not specified");

		String organizationHref = organizationLink.attr("href");
		Matcher matcher = ORGANIZATION_PATTERN.matcher(organizationHref);
		if (matcher.find()) {
			String id = matcher.group(1);
			String name = organizationLink.text();
			String site = null;

			if (organizationSpan != null) {
				site = organizationSpan.text().trim().replaceFirst("-", "").trim();
			}

			organization = new Organization(id, name, site);
		}

		return organization;
	}

	@Override
	public String parseOrganizationName(Document doc) throws DocumentParsingException {
		final Element organizationNameElement = doc.select(".gsc_authors_header").first();

		if (organizationNameElement == null)
			throw new DocumentParsingException("Organization name not found");

		final Element helpLabelElement = organizationNameElement.select("a.gsc_org_help_label").first();

		String name = organizationNameElement.text();
		if (helpLabelElement != null) {
			String helpLabel = helpLabelElement.text();
			name = name.replace(helpLabel.trim(), "").trim();
		}

		return name;
	}

	@Override
	public Author parseAuthorFromSearchResults(Document doc) throws DocumentParsingException {
		Author author = null;
		System.out.println(doc);

		final Elements searchResultElements = doc.select("div.gs_r");

		if (!searchResultElements.isEmpty()) {
			final Elements authorLinkElements = searchResultElements.first().select("div.gs_a").select("a");

			for (Element authorLinkElement : authorLinkElements) {
				final Elements childrenElements = authorLinkElement.children();
				final Element targetAuthorElement = childrenElements.select("b").first();

				System.out.println(authorLinkElement);
				if (targetAuthorElement != null) {
					System.out.println(targetAuthorElement);

					String authorHrefAttribute = authorLinkElement.attr("href");
					String authorId = parseUserIdFromUserCitationAttribute(authorHrefAttribute);
					String authorName = authorLinkElement.text();

					author = new Author(authorId, authorName, null);
					break;
				}
			}
		}

		return author;
	}

	@Override
	public Organization parseOrganizationFromSearchResults(Document doc) throws DocumentParsingException {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args) throws DocumentParsingException, DocumentCrawlingException {
		AcademicPublicationDocumentParser parser = new ScholarAcademicPublicationDocumentParser();
		Author author = parser.parseAuthorFromSearchResults(
				new ScholarDocumentProvider().getSearchResultsDocumentOnFirstPage("AP Rotshtein"));
		System.out.println(author);
	}

	private static String parseUserIdFromUserCitationAttribute(String userCitationAttribute)
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

	private static String parseOrganizationIdFrom(String userCitationAttribute) throws DocumentParsingException {
		return null;
	}

}
