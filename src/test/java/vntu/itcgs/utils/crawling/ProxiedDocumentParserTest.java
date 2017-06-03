package vntu.itcgs.utils.crawling;

import static org.junit.Assert.*;

import java.net.SocketTimeoutException;

import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import vntu.itcgs.utils.crawling.ProxiedDocumentParser;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProxiedDocumentParserTest {

	@Autowired
	private ProxiedDocumentParser docParser;

	@Test
	public void testProxiedParsing() throws DocumentParsingException, SocketTimeoutException {
		Document doc = docParser.parseDocument("http://google.com/");
		assertNotNull(doc);
	}

}
