package vntu.academcoop.utils.matching;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

import vntu.academcoop.dto.AuthorDTO;
import vntu.academcoop.model.Author;

public class FuzzyNameMatcher {

	private static final Logger logger = LoggerFactory.getLogger(FuzzyNameMatcher.class);

	public static final double MIN_SCORE = 0.62;

	private final Collection<AuthorDTO> authors;

	public FuzzyNameMatcher(Collection<AuthorDTO> authors) {
		this.authors = authors;
	}

	public AuthorDTO findClosestMatchAuthor(String searchName) {
		Match match = findClosestMatch(searchName);
		return match != null ? match.getCandidate() : null;
	}

	private Match findClosestMatch(String searchName) {
		List<Match> matches = findMatches(searchName);
		logger.debug("Name: '{}' matches: '{}'", searchName, matches);

		return !matches.isEmpty() ? Match.SCORE_ORDER.max(matches) : null;
	}

	private List<Match> findMatches(String searchName) {
		List<Match> results = new ArrayList<Match>();

		for (AuthorDTO author : authors) {
			double score = scoreName(searchName, author.getName());

			if (score > MIN_SCORE) {
				results.add(new Match(author, score));
			}
		}

		return ImmutableList.copyOf(results);
	}

	private static double scoreName(String searchName, String candidateName) {
		if (searchName.equals(candidateName))
			return 1.0;

		double score = tanimoto(searchName, candidateName);

		return Math.max(0.0, Math.min(score, 1.0));
	}

	private static double tanimoto(String a, String b) {
		Set<Character> aSet = setOfChars(a);
		Set<Character> bSet = setOfChars(b);

		int aLen = aSet.size();
		int bLen = bSet.size();
		aSet.retainAll(bSet);
		int cLen = aSet.size();

		return cLen / (double) (aLen + bLen - cLen);
	}

	private static Set<Character> setOfChars(String str) {
		Set<Character> set = new HashSet<>();
		for (int i = 0; i < str.length(); i++) {
			set.add(str.charAt(i));
		}
		return set;
	}

	public static void main(String[] args) {
		AuthorDTO a1 = new AuthorDTO(new Author(null, "Сергей Д Штовба", null, false));
		AuthorDTO a2 = new AuthorDTO(new Author(null, "Сергей Д Штовба", null, false));
		AuthorDTO a3 = new AuthorDTO(new Author(null, "СД Штовба", null, false));
		AuthorDTO a4 = new AuthorDTO(new Author(null, "Ротштейн", null, false));

		FuzzyNameMatcher matcher = new FuzzyNameMatcher(Arrays.asList(a1, a2, a3, a4));
		System.out.println(matcher.findClosestMatchAuthor("АП Штовба"));
	}

}
