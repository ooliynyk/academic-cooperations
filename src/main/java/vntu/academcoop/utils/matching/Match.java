package vntu.academcoop.utils.matching;

import com.google.common.collect.Ordering;
import com.google.common.primitives.Doubles;

import vntu.academcoop.dto.AuthorDetails;

public class Match {

	private final AuthorDetails candidate;
	private final double score; // 0 - definitely not, 1.0 - perfect match

	public static final Ordering<Match> SCORE_ORDER = new Ordering<Match>() {
		@Override
		public int compare(Match left, Match right) {
			return Doubles.compare(left.score, right.score);
		}
	};

	public Match(AuthorDetails candidate, double score) {
		this.candidate = candidate;
		this.score = score;
	}

	public AuthorDetails getCandidate() {
		return candidate;
	}

	public double getScore() {
		return score;
	}

	@Override
	public String toString() {
		return "Match [candidate=" + candidate + ", score=" + score + "]";
	}

}
