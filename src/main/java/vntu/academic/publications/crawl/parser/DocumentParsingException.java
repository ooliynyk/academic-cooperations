package vntu.academic.publications.crawl.parser;

public class DocumentParsingException extends Exception {

	private static final long serialVersionUID = -2577888730770169054L;

	public DocumentParsingException(String message, Throwable cause) {
		super(message, cause);
	}

	public DocumentParsingException(String message) {
		super(message);
	}

}
