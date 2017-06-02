package vntu.itcgs.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Publication implements Serializable {

	private static final long serialVersionUID = -497474200500578592L;

	private final String id;

	private final String title;

	private final Collection<String> authorsNames;

	private final Date publicationDate;

}
