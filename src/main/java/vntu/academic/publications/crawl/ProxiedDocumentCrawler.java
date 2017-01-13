package vntu.academic.publications.crawl;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.Random;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableList;

import vntu.academic.publications.helpers.NetworkHelper;

@Service
public class ProxiedDocumentCrawler implements DocumentCrawler {
	private static final Logger logger = LoggerFactory.getLogger(ProxiedDocumentCrawler.class);

	private static final List<String> userAgents = ImmutableList.<String>builder()
			.add("Mozilla/5.0 (Macintosh; Intel Mac OS X x.y; rv:10.0) Gecko/20100101 Firefox/10.0")
			.add("Mozilla/5.0 (Macintosh; PPC Mac OS X x.y; rv:10.0) Gecko/20100101 Firefox/10.0")
			.add("Mozilla/5.0 (Windows NT x.y; Win64; x64; rv:10.0) Gecko/20100101 Firefox/10.0")
			.add("Mozilla/5.0 (Android 4.4; Mobile; rv:41.0) Gecko/41.0 Firefox/41.0")
			.add("Mozilla/5.0 (X11; Linux i686; rv:10.0.1) Gecko/20100101 Firefox/10.0.1 SeaMonkey/2.7.1")
			.add("Mozilla/5.0 (Maemo; Linux armv7l; rv:10.0.1) Gecko/20100101 Firefox/10.0.1 Fennec/10.0.1")
			.add("Mozilla/5.0 (Windows NT 5.2; rv:10.0.1) Gecko/20100101 Firefox/10.0.1 SeaMonkey/2.7.1")
			.add("Mozilla/5.0 (Linux; U; Android 4.1.1; en-gb; Build/KLP) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Safari/534.30")
			.add("Mozilla/5.0 (Linux; Android 5.1.1; Nexus 5 Build/LMY48B; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/43.0.2357.65 Mobile Safari/537.36")
			.add("Mozilla/5.0 (Linux; Android 6.0.1; Nexus 6P Build/MMB29P) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.83 Mobile Safari/537.36")
			.add("Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.0.1) Gecko/2008070208 Firefox/3.0.1")
			.add("Mozilla/5.0 (Macintosh; U; PPC Mac OS X; en) AppleWebKit/125.2 (KHTML, like Gecko) Safari/125.8")
			.add("Mozilla/5.0 (compatible; Konqueror/3.5; Linux) KHTML/3.5.10 (like Gecko) (Kubuntu)")
			.add("Konqueror/3.0-rc4; (Konqueror/3.0-rc4; i686 Linux;;datecode)")
			.add("Opera/9.52 (X11; Linux i686; U; en)")
			.build();
	
	private static final Random rand = new Random();
	
	public Document fetchDocument(String url) throws DocumentCrawlingException {
		Document doc = null;
		for (int i = 1; i <= 3; i++) {
			try {
				Proxy proxy = new Proxy(Proxy.Type.HTTP, 
						new InetSocketAddress(NetworkHelper.getLocalHostLANAddress(), 8118));
				doc = Jsoup.connect(url).userAgent(getUserAgent())
						.timeout(3000 * i).proxy(proxy)
						.get();
				break;
			} catch (HttpStatusException e) {
				throw new DocumentCrawlingException("Document fetching blocked", e);
			} catch (Exception e) {
				logger.warn("Document fetching from '{}' error", url, e);
				try {
					Thread.sleep(1000*i);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
		if (doc == null)
			throw new DocumentCrawlingException("Document fetching error");

		return doc;
	}
	
	private static String getUserAgent() {
		return userAgents.get(rand.nextInt(userAgents.size()));
	}
	
}
