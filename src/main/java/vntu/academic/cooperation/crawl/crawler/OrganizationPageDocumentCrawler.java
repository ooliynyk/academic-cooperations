package vntu.academic.cooperation.crawl.crawler;

import java.net.URLDecoder;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import vntu.academic.cooperation.crawl.doc.OrganizationPageDocument;
import vntu.academic.cooperation.crawl.doc.OrganizationPageDocument.OrganizationDetails;

public final class OrganizationPageDocumentCrawler extends DocumentCrawler<OrganizationPageDocument> {

	private static final Pattern NEXT_PAGE_ID_PATTERN = Pattern
			.compile("window.location='\\/citations\\?.*&after_author=([^&'\"]+)");
	
	private static final Pattern ORGANIZATION_ID_PATTERN = Pattern.compile("\\/citations?.*org=([^&]+)");

	private final DocumentCrawler<Collection<String>> authorsIdentifiersCrawler;

	public OrganizationPageDocumentCrawler(Document doc) {
		super(doc);
		this.authorsIdentifiersCrawler = new AuthorsCrawler(doc);
	}

	@Override
	public OrganizationPageDocument crawl() throws DocumentCrawlingException {
		String orgId = crawlOrganizationId();
		String orgName = crawlOrganizationName();

		OrganizationDetails orgDetails = new OrganizationDetails(orgId, orgName);
		Collection<String> authorsIds = authorsIdentifiersCrawler.crawl();
		String nextPageId = crawlNextPageId();

		return new OrganizationPageDocument(orgDetails, authorsIds, nextPageId);
	}

	private String crawlNextPageId() throws DocumentCrawlingException {
		String nextPageId = null;

		try {
			String windowLocationAttribute = doc.select("button.gs_btnPR").attr("onclick");

			String decodedLocation = URLDecoder.decode(windowLocationAttribute.replace("\\x", "%"), "UTF-8");

			Matcher matcher = NEXT_PAGE_ID_PATTERN.matcher(decodedLocation);
			
			nextPageId = matcher.find() ? matcher.group(1) : null;
		} catch (Exception e) {
			throw new DocumentCrawlingException("Document crawling error: " + e.getMessage());
		}

		return nextPageId;
	}

	private String crawlOrganizationId() throws DocumentCrawlingException {
		Matcher matcher = ORGANIZATION_ID_PATTERN.matcher(doc.location());

		String id = matcher.find() ? matcher.group(1) : null;

		if (id == null)
			throw new DocumentCrawlingException("Organization identifier not found");

		return id;
	}

	private String crawlOrganizationName() throws DocumentCrawlingException {
		final Element organizationNameElement = doc.select(".gsc_authors_header").first();

		if (organizationNameElement == null)
			throw new DocumentCrawlingException("Organization name not found");

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
