package vntu.academcoop.dto;

import java.util.Collection;
import java.util.Date;

import lombok.Data;

@Data
public class PublicationDetails {

	private String publicationId;

	private String title;

	private Collection<AuthorDetails> authors;

	private Date publicationDate;

}
