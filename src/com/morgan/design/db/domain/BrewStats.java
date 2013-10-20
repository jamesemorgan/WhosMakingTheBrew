package com.morgan.design.db.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "brew_stats")
public class BrewStats {

	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField(canBeNull = true)
	private int totalTimesRun;

	@DatabaseField(canBeNull = true)
	private int highestScore;

	@DatabaseField(canBeNull = true)
	private int lowestScore;

	@DatabaseField(canBeNull = true)
	private int totalScore;

	@DatabaseField(canBeNull = true)
	private int totalNumPlayers;

	public BrewStats() {
		//
	}

	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public final int getTotalTimesRun() {
		return this.totalTimesRun;
	}

	public final void setTotalTimesRun(final int totalTimesRun) {
		this.totalTimesRun = totalTimesRun;
	}

	public final int getHighestScore() {
		return this.highestScore;
	}

	public final void setHighestScore(final int highestScore) {
		this.highestScore = highestScore;
	}

	public final int getLowestScore() {
		return this.lowestScore;
	}

	public final void setLowestScore(final int lowestScore) {
		this.lowestScore = lowestScore;
	}

	public final int getTotalScore() {
		return this.totalScore;
	}

	public final void setTotalScore(final int totalScore) {
		this.totalScore = totalScore;
	}

	public final int getTotalNumPlayers() {
		return this.totalNumPlayers;
	}

	public final void setTotalNumPlayers(final int totalNumPlayers) {
		this.totalNumPlayers = totalNumPlayers;
	}

	public int getAverageScore() {
		// total score / total number players
		final int totalScore = getTotalScore();
		final int totalNumberOfPlayers = getTotalNumPlayers();
		if (totalNumberOfPlayers == 0 || totalScore == 0) {
			return 0;
		}
		return totalScore / totalNumberOfPlayers;
	}

	public int getAverageNumberOfPlayers() {
		// total number players / total times run
		final int totalNumberOfPlayers = getTotalNumPlayers();
		final int totalTimesRun = getTotalTimesRun();
		if (totalNumberOfPlayers == 0 || totalTimesRun == 0) {
			return 0;
		}
		return totalNumberOfPlayers / totalTimesRun;
	}

	public void incrementTotalTimesRun() {
		this.totalTimesRun++;
	}

	public void addTotalNumberOfPlayers(final int size) {
		this.totalNumPlayers = this.totalNumPlayers + size;
	}
}
