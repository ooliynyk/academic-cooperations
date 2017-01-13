package vntu.academic.publications.pageable;

import java.util.Collection;

import vntu.academic.publications.entities.Author;

public interface AuthorsPage {
	Collection<Author> authors();

	boolean hasNextPage();

	AuthorsPage nextPage();

}
