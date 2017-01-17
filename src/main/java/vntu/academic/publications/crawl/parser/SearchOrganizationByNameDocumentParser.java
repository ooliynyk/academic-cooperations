package vntu.academic.publications.crawl.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public final class SearchOrganizationByNameDocumentParser extends DocumentParser {

	private static final Pattern ORGANIZATION_PATTERN = Pattern.compile("\\/citations\\?.*org=([^&]+)");

	SearchOrganizationByNameDocumentParser(Document doc) {
		super(doc);
	}

	public String parseOrganizationId() throws DocumentParsingException {
		final Element organizationLink = selectOrganizationLinkElement();

		if (organizationLink == null)
			throw new DocumentParsingException("Organization link not specified");

		String organizationHref = organizationLink.attr("href");

		return fetchOrganizationIdFromHref(organizationHref);
	}

	public String parseOrganizationName() throws DocumentParsingException {
		final Element organizationLink = selectOrganizationLinkElement();

		if (organizationLink == null)
			throw new DocumentParsingException("Organization link not specified");

		return organizationLink.text();
	}

	public String parseOrganizationSite() throws DocumentParsingException {
		String site = null;

		final Element organizationElement = doc.select("div.gsc_instbox_sec").select(".gsc_inst_res").first();
		final Element organizationSpan = organizationElement.select("span.gs_gray").first();

		if (organizationSpan != null) {
			site = organizationSpan.text().trim().replaceFirst("-", "").trim();
		}

		return site;
	}

	private static String fetchOrganizationIdFromHref(String organizationHref) throws DocumentParsingException {
		String id = null;

		Matcher matcher = ORGANIZATION_PATTERN.matcher(organizationHref);
		if (matcher.find()) {
			id = matcher.group(1);
		} else {
			throw new DocumentParsingException("Organization pattern not found in: " + organizationHref);
		}

		return id;
	}

	private Element selectOrganizationLinkElement() throws DocumentParsingException {
		final Element organizationElement = doc.select("div.gsc_instbox_sec").select(".gsc_inst_res").first();

		if (organizationElement == null)
			throw new DocumentParsingException("Organization element not found");

		return organizationElement.select("a").first();
	}

}
