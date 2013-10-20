package com.morgan.design.db.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class TeaRound {

	public static List<BrewPlayer> determineWinner(final List<BrewPlayer> players) {
		final List<Integer> randomScores = generateUniqueRandomNumber(players.size());

		Collections.shuffle(players);
		for (int i = 0; i < randomScores.size(); i++) {
			players.get(i).setScore(randomScores.get(i));
		}

		Collections.sort(players);

		return players;
	}

	private static List<Integer> generateUniqueRandomNumber(final int numberOf) {
		final Random random = new Random();
		final Random random2 = new Random();

		final Set<Integer> randoms = new HashSet<Integer>();

		while (randoms.size() < numberOf) {
			final int nextInt = random.nextInt(500);
			final int nextInt2 = random2.nextInt(500);
			randoms.add(nextInt + nextInt2);
		}
		final List<Integer> uniqueList = new ArrayList<Integer>(randoms);
		Collections.shuffle(uniqueList);
		return uniqueList;
	}
}
