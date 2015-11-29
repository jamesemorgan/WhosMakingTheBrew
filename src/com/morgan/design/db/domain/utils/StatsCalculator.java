package com.morgan.design.db.domain.utils;

import com.morgan.design.db.domain.BrewPlayer;

import java.util.List;

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
