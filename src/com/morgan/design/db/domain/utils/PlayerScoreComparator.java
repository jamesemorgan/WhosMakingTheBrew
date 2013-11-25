package com.morgan.design.db.domain.utils;

import java.util.Comparator;

import com.morgan.design.db.domain.BrewPlayer;

public class PlayerScoreComparator implements Comparator<BrewPlayer> {

	public static final PlayerScoreComparator INSTANCE = new PlayerScoreComparator();

	@Override
	public int compare(BrewPlayer lhs, BrewPlayer rhs) {
		if (lhs.getScore() < rhs.getScore()) {
			return 1;
		}
		else if (lhs.getScore() > rhs.getScore()) {
			return -1;
		}
		else {
			return 0;
		}
	}

}
