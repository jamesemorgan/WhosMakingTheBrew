package com.morgan.design.db.domain.utils;

import java.util.List;

import com.morgan.design.db.domain.BrewPlayer;

public class StatsCalculator {

	public static final int average(int count, int totalNumber) {
		if (totalNumber == 0 || count == 0) {
			return 0;
		}
		return count / totalNumber;
	}

	public static final int countTotalScores(final List<BrewPlayer> players) {
		int totalScore = 0;
		for (final BrewPlayer player : players) {
			totalScore += player.getScore();
		}
		return totalScore;
	}

}
