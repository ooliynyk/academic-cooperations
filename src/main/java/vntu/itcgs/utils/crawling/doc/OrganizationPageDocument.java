package vntu.itcgs.utils.crawling.doc;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import vntu.itcgs.model.Organization;

@Data
@AllArgsConstructor
public class OrganizationPageDocument {

	private final Organization organizationDetails;

	private final Collection<String> authorsIdentifiers;

	private final String nextPageId;

}
