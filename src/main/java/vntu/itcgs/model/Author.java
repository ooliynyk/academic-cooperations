package vntu.itcgs.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Author {

	private final String id;

	private final String name;

	private final String organizationId;

	private final boolean hasCoAuthors;

}
