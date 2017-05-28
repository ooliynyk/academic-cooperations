package vntu.academcoop.utils.crawling.crawler;

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

import vntu.academcoop.model.Author;
import vntu.academcoop.model.Publication;
import vntu.academcoop.utils.crawling.doc.PersonalPageDocument;

public final class PersonalPageDocumentCrawler extends DocumentCrawler<PersonalPageDocument> {

	private final AuthorDetailsCrawler authorCrawler;
	private final PublicationsCrawler publicationsCrawler;

	public PersonalPageDocumentCrawler(Document doc) {
		super(doc);
		this.authorCrawler = new AuthorDetailsCrawler();
		this.publicationsCrawler = new PublicationsCrawler();
	}

	public AuthorDetailsCrawler getAuthorCrawler() {
		return authorCrawler;
	}

	public PublicationsCrawler getPublicationsCrawler() {
		return publicationsCrawler;
	}

	@Override
	public PersonalPageDocument crawl() throws DocumentCrawlingException {
		Author authorDetails = authorCrawler.crawlAuthorDetials();
		Collection<Publication> publications = publicationsCrawler.crawlPublicationsDetails();
		boolean hasMorePublications = crawlHasMorePublications();

		return new PersonalPageDocument(authorDetails, publications, hasMorePublications);
	}

	public boolean crawlHasMorePublications() {
		Element showMoreButton = doc.select("button#gsc_bpf_more.gs_btn_smd").first();

		return !showMoreButton.hasAttr("disabled");
	}

	private class AuthorDetailsCrawler {

		private final Pattern USER_ID_PATTERN = Pattern.compile("\\/citations\\?.*user=([^&]+)");
		private final Pattern ORGANIZATION_PATTERN = Pattern.compile("\\/citations\\?.*org=([^&]+)");

		public Author crawlAuthorDetials() throws DocumentCrawlingException {
			String authorId = crawlAuthorId();
			String authorName = crawlAuthorName();
			String organizationId = crawlOrganizationId();
			boolean hasCoAuthors = crawlHasCoAuthors();
			return new Author(authorId, authorName, organizationId, hasCoAuthors);
		}

		private String crawlAuthorId() throws DocumentCrawlingException {
			String url = doc.location();

			Matcher matcher = USER_ID_PATTERN.matcher(url);
			String authorId = matcher.find() ? matcher.group(1) : null;

			if (authorId == null)
				throw new DocumentCrawlingException("Author identifier not found");

			return authorId;
		}

		private String crawlAuthorName() throws DocumentCrawlingException {
			final Element userNameElement = doc.select("div#gsc_prf_in").first();

			if (userNameElement == null)
				throw new DocumentCrawlingException("Author details not specified");

			return userNameElement.text();
		}

		private String crawlOrganizationId() throws DocumentCrawlingException {
			String organizationId = null;

			final Element userStaffElement = doc.select("div.gsc_prf_il a").first();

			if (userStaffElement == null)
				throw new DocumentCrawlingException("Organization link not specified");

			String userStaffAttribute = userStaffElement.attr("href");

			Matcher userOgranizationMatcher = ORGANIZATION_PATTERN.matcher(userStaffAttribute);
			if (userOgranizationMatcher.find()) {
				organizationId = userOgranizationMatcher.group(1);
			} else {
				throw new DocumentCrawlingException("Organization not specified");
			}

			return organizationId;
		}

		private boolean crawlHasCoAuthors() {
			return !doc.select("div#gsc_rsb_co").isEmpty();
		}
	}

	private class PublicationsCrawler {

		private final SimpleDateFormat PUBLICATION_DATE_FORMAT = new SimpleDateFormat("yyyy");

		private final Pattern PUBLICATION_ID_PATTERN = Pattern.compile("\\/citations\\?.*citation_for_view=([^&]+)");

		public Collection<Publication> crawlPublicationsDetails() throws DocumentCrawlingException {
			Element publicationsTableElement = doc.select("table#gsc_a_t").first();
			Elements publicationElements = publicationsTableElement.select("tbody#gsc_a_b").select("tr.gsc_a_tr");

			Collection<Publication> publications = new ArrayList<>();
			for (Element publicationElement : publicationElements) {
				Collection<String> authorsNames = crawlAuthorsNames(publicationElement);
				if (authorsNames.size() < 2)
					continue; // ignoring publications without any cooperation

				String publicationTitle = crawlPublicationTitle(publicationElement);
				String publicationId = crawlPublicationId(publicationElement);
				Date publicationDate = crawlPublicationYear(publicationElement);

				publications.add(new Publication(publicationId, publicationTitle, authorsNames, publicationDate));
			}

			return publications;
		}

		private String crawlPublicationTitle(Element publicationElement) {
			Element descriptionElement = publicationElement.select("td.gsc_a_t").first();
			Element publicationLinkElement = descriptionElement.select("a.gsc_a_at").first();

			return publicationLinkElement.text();
		}

		private String crawlPublicationId(Element publicationElement) throws DocumentCrawlingException {

			Element descriptionElement = publicationElement.select("td.gsc_a_t").first();
			Element publicationLinkElement = descriptionElement.select("a.gsc_a_at").first();
			String publicationHref = publicationLinkElement.attr("href");

			Matcher matcher = PUBLICATION_ID_PATTERN.matcher(publicationHref);

			String publicationId = matcher.find() ? matcher.group(1) : null;

			if (publicationId == null)
				throw new DocumentCrawlingException("Publication identifier not found");

			return publicationId;
		}

		private Collection<String> crawlAuthorsNames(Element publicationBlockElement) {
			Element descriptionElement = publicationBlockElement.select("td.gsc_a_t").first();
			Element authorsElement = descriptionElement.select("div.gs_gray").first();

			String[] authorNames = authorsElement.text().split(",");
			for (int i = 0; i < authorNames.length; i++) {
				authorNames[i] = authorNames[i].trim();
			}

			return Arrays.asList(authorNames);
		}

		private Date crawlPublicationYear(Element publicationBlockElement) {
			Date year = null;

			Element yearElement = publicationBlockElement.select("td.gsc_a_y").select("span.gsc_a_h").first();

			String yearText = yearElement.text();
			if (!yearText.isEmpty()) {
				try {
					year = PUBLICATION_DATE_FORMAT.parse(yearText);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			return year;
		}
	}

}
