package vntu.academic.cooperation.crawl.crawler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import vntu.academic.cooperation.crawl.doc.OrganizationPageDocument.OrganizationDetails;

public class InstitutionBlockCrawler extends DocumentCrawler<OrganizationDetails> {

	private static final Pattern ORGANIZATION_ID_PATTERN = Pattern.compile("\\/citations?.*org=([^&]+)");

	public InstitutionBlockCrawler(Document doc) {
		super(doc);
	}

	@Override
	public OrganizationDetails crawl() throws DocumentCrawlingException {
		Element organizationLink = doc.select("div.gsc_instbox_sec").select("h3.gsc_inst_res").select("a").first();
		if (organizationLink == null) 
			throw new DocumentCrawlingException("Organization details not specified");

		String href = organizationLink.attr("href");

		Matcher matcher = ORGANIZATION_ID_PATTERN.matcher(href);
		String organizationId = matcher.find() ? matcher.group(1) : null;
		if (organizationId == null)
			throw new DocumentCrawlingException("Organization identifier not found");
		
		String organizationName = organizationLink.text();

		return new OrganizationDetails(organizationId, organizationName);
	}

}
