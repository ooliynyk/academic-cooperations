package vntu.academic.cooperation.service;

import org.apache.commons.lang3.StringUtils;

public class NameMatcher {
	
	public double match(String name, String otherName) {
		name = parsing(name);
		otherName = parsing(otherName);
		
		return matching(name, otherName);
	}
	
	private String parsing(String name) {
		return name;
	}
	
	private double matching(String name, String otherName) {
		int distance = StringUtils.getLevenshteinDistance(name, otherName);
		System.out.println(distance);
		
		double score = (otherName.length() - distance) / (double) otherName.length();
		System.out.println(score);

		return score;
	}
	
	public static void main(String[] args) {
		NameMatcher matcher = new NameMatcher();
		System.out.println(matcher.match("Shtovba SD", "Shtovba SD "));
	}
}
