package vntu.itcgs.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Author implements Serializable {

	private static final long serialVersionUID = 6387798346325177220L;

	private final String id;

	private final String name;

	private final String organizationId;

	private final boolean hasCoAuthors;

}
