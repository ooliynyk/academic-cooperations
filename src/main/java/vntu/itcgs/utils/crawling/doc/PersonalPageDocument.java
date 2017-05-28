package vntu.itcgs.utils.crawling.doc;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import vntu.itcgs.model.Author;
import vntu.itcgs.model.Publication;

@Data
@AllArgsConstructor
public class PersonalPageDocument {

	private final Author authorDetails;

	private final Collection<Publication> publications;

	private final boolean hasMorePublications;

}
