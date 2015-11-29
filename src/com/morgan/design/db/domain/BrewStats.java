package com.morgan.design.db.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.morgan.design.db.domain.utils.StatsCalculator;

@DatabaseTable(tableName = "brew_stats")
public class BrewStats {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = true, defaultValue = "0")
    private int totalTimesRun;

    @DatabaseField(canBeNull = true, defaultValue = "0")
    private int highestScore;

    @DatabaseField(canBeNull = true, defaultValue = "0")
    private int lowestScore;

    @DatabaseField(canBeNull = true, defaultValue = "0")
    private int totalScore;

    @DatabaseField(canBeNull = true, defaultValue = "0")
    private int totalNumPlayers;

    public BrewStats() {
        //
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public final int getTotalTimesRun() {
        return totalTimesRun;
    }

    public final void setTotalTimesRun(final int totalTimesRun) {
        this.totalTimesRun = totalTimesRun;
    }

    public final int getHighestScore() {
        return highestScore;
    }

    public final void setHighestScore(final int highestScore) {
        this.highestScore = highestScore;
    }

    public final int getLowestScore() {
        return lowestScore;
    }

    public final void setLowestScore(final int lowestScore) {
        this.lowestScore = lowestScore;
    }

    public final int getTotalScore() {
        return totalScore;
    }

    public final void setTotalScore(final int totalScore) {
        this.totalScore = totalScore;
    }

    public final int getTotalNumPlayers() {
        return totalNumPlayers;
    }

    public final void setTotalNumPlayers(final int totalNumPlayers) {
        this.totalNumPlayers = totalNumPlayers;
    }

    public int getAverageScore() {
        // total score / total number players
        return StatsCalculator.average(getTotalScore(), getTotalNumPlayers());
    }

    public int getAverageNumberOfPlayers() {
        // total number players / total times run
        return StatsCalculator.average(getTotalTimesRun(), getTotalNumPlayers());
    }

    public void incrementTotalTimesRun() {
        totalTimesRun++;
    }

    public void addTotalNumberOfPlayers(final int size) {
        totalNumPlayers = totalNumPlayers + size;
    }

    public void addTotalScores(int totalScores) {
        setTotalScore(totalScore + totalScores);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("BrewStats [id=");
        builder.append(id);
        builder.append(", totalTimesRun=");
        builder.append(totalTimesRun);
        builder.append(", highestScore=");
        builder.append(highestScore);
        builder.append(", lowestScore=");
        builder.append(lowestScore);
        builder.append(", totalScore=");
        builder.append(totalScore);
        builder.append(", totalNumPlayers=");
        builder.append(totalNumPlayers);
        builder.append("]");
        return builder.toString();
    }

}
