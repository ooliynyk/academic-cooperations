package vntu.academcoop.model;

import java.util.Collection;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Publication {

	private final String id;

	private final String title;

	private final Collection<String> authorsNames;

	private final Date publicationDate;

}
