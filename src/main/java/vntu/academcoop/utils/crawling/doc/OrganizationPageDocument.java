package vntu.academcoop.utils.crawling.doc;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import vntu.academcoop.model.Organization;

@Data
@AllArgsConstructor
public class OrganizationPageDocument {

	private final Organization organizationDetails;

	private final Collection<String> authorsIdentifiers;

	private final String nextPageId;

}
