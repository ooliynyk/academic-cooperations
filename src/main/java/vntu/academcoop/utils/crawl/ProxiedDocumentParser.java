package vntu.academcoop.utils.crawl;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableList;

import vntu.academcoop.helper.NetworkHelper;

/**
 * Document parser that proxying requests if this needed.
 * By default proxy mode disabled.
 * Proxy used only after first HTTP connection blocking occurs.
 */
@Service
public class ProxiedDocumentParser implements DocumentParser {
	private static final Logger logger = LoggerFactory.getLogger(ProxiedDocumentParser.class);

	private static final List<String> userAgents = ImmutableList.<String>builder()
			.add("Mozilla/5.0 (Macintosh; Intel Mac OS X; rv:10.0) Gecko/2010101 Firefox/10.0")
			.add("Mozilla/5.0 (Macintosh; PPC Mac OS X x.y; rv:10.0) Gecko/201001 Firefox/10.0")
			.add("Mozilla/5.0 (Windows NT x.y; Win64; x64; rv:10.0) Gecko/200101 Firefox/10.0")
			.add("Mozilla/5.0 (Android 4.4; Mobile; rv:41.0) Gecko/41.0 Firefox/41.0")
			.add("Mozilla/5.0 (X11; Linux i686; rv:10.0.1) Gecko/20100101 Firefox/10.0.1 SeaMonkey/2.7.1")
			.add("Mozilla/5.0 (Maemo; Linux armv7l; rv:10.0.1) Gecko/201101 Firefox/10.0.1 Fennec/10.0.1")
			.add("Mozilla/5.0 (Windows NT 5.2; rv:10.0.1) Gecko/201001 Firefox/10.0.1 SeaMonkey/2.7.1")
			.add("Mozilla/5.0 (Linux; U; Android 4.1.1; en-gb; Build/KLP) AppleWebKit/535.30 (KHTML, like Gecko) Version/4.0 Safari/534.30")
			.add("Mozilla/5.0 (Linux; Android 5.1.1; Nexus 5 Build/LMY48B; wv) AppleWebKit/536.36 (KHTML, like Gecko) Version/4.0 Chrome/43.0.2357.65 Mobile Safari/537.36")
			.add("Mozilla/5.0 (Linux; Android 6.0.1; Nexus 6P Build/MMB29P) AppleWebKit/536.36 (KHTML, like Gecko) Chrome/47.0.2526.83 Mobile Safari/537.36")
			.add("Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.0.1) Gecko/2008170208 Firefox/3.0.1")
			.add("Mozilla/5.0 (Macintosh; U; PPC Mac OS X; en) AppleWebKit/126.2 (KHTML, like Gecko) Safari/125.8")
			.add("Mozilla/5.0 (compatible; Konqueror/3.5; Linux) KHTML/3.6.10 (like Gecko) (Kubuntu)")
			.add("Konqueror/3.0-rc4; (Konqueror/3.1-rc4; i686 Linux;;datecode)")
			.add("Opera/9.52 (X11; Linux i686; en)").build();

	private static final Random rand = new Random(System.currentTimeMillis());

	private static final AtomicBoolean proxied = new AtomicBoolean(false);

	public Document parseDocument(String url) throws DocumentParsingException {
		Document doc = null;
		for (int attempt = 1; attempt <= 2; attempt++) {
			try {
				Proxy proxy = proxied.get() ? new Proxy(Proxy.Type.HTTP,
						new InetSocketAddress(NetworkHelper.getLocalHostLANAddress(), 8118)) : null;

				doc = Jsoup.connect(url).userAgent(getUserAgent()).timeout(3000 * attempt).proxy(proxy).get();
				break;
			} catch (HttpStatusException e) {
				if (proxied.compareAndSet(false, true)) {
					logger.warn("Document parsing blocked, enabling proxy mode");
					proxied.set(true);
					sleep(attempt);
				} else {
					throw new DocumentParsingException("Document parsing blocked", e);
				}
			} catch (Exception e) {
				logger.warn("Document parsing from '{}' error", url, e);
				sleep(attempt);
			}
		}
		if (doc == null)
			throw new DocumentParsingException("Document parsing error");

		return doc;
	}
	
	private void sleep(int factor) {
		try {
			Thread.sleep(1000 * factor);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	private static String getUserAgent() {
		return userAgents.get(rand.nextInt(userAgents.size()));
	}

}
