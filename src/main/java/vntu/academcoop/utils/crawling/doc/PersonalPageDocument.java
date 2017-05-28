package vntu.academcoop.utils.crawling.doc;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import vntu.academcoop.model.Author;
import vntu.academcoop.model.Publication;

@Data
@AllArgsConstructor
public class PersonalPageDocument {

	private final Author authorDetails;

	private final Collection<Publication> publications;

	private final boolean hasMorePublications;

}
