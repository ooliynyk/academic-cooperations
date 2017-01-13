package vntu.academic.publications.crawl;

public class DocumentCrawlingException extends Exception {

	private static final long serialVersionUID = 360409954198916579L;

	public DocumentCrawlingException(String message) {
		super(message);
	}

	public DocumentCrawlingException(String message, Throwable cause) {
		super(message, cause);
	}

}
