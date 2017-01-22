package vntu.academcoop.utils.matching;

import com.google.common.collect.Ordering;
import com.google.common.primitives.Doubles;

import vntu.academcoop.dto.AuthorDTO;

public class Match {

	private final AuthorDTO candidate;
	private final double score; // 0 - definitely not, 1.0 - perfect match

	public static final Ordering<Match> SCORE_ORDER = new Ordering<Match>() {
		@Override
		public int compare(Match left, Match right) {
			return Doubles.compare(left.score, right.score);
		}
	};

	public Match(AuthorDTO candidate, double score) {
		this.candidate = candidate;
		this.score = score;
	}

	public AuthorDTO getCandidate() {
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
