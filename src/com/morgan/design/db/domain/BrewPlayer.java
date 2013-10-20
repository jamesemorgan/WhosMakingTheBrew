package com.morgan.design.db.domain;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = BrewPlayer.BREW_PLAYERS)
public class BrewPlayer implements Serializable, Comparable<BrewPlayer> {

	private static final long serialVersionUID = 8844544489762032207L;

	public final static String BREW_PLAYERS = "brew_players";
	public static final String ID = "id";
	public static final String BREW_GROUP_ID = "fk_group_id";
	public static final String LAST_RUN = "lastRun";

	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField(canBeNull = false)
	private String name;

	@DatabaseField(canBeNull = true)
	private int score;

	@DatabaseField(canBeNull = true, columnName = LAST_RUN)
	private boolean lastRun = false;

	@DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = BREW_GROUP_ID)
	private BrewGroup brewGroup;

	public BrewPlayer() {
		//
	}

	public final int getId() {
		return this.id;
	}

	public final void setId(final int id) {
		this.id = id;
	}

	public final String getName() {
		return this.name;
	}

	public final void setName(final String name) {
		this.name = name;
	}

	public final int getScore() {
		return this.score;
	}

	public final void setScore(final int score) {
		this.score = score;
	}

	public boolean isLastRun() {
		return this.lastRun;
	}

	public void setLastRun(final boolean lastRun) {
		this.lastRun = lastRun;
	}

	public BrewGroup getBrewGroup() {
		return this.brewGroup;
	}

	public void setBrewGroup(final BrewGroup brewGroup) {
		this.brewGroup = brewGroup;
	}

	@Override
	public int compareTo(final BrewPlayer player) {
		if (this.getScore() < player.getScore())
			return 1;
		else if (this.getScore() > player.getScore())
			return -1;
		else
			return 0;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("BrewPlayer [id=").append(this.id).append(", name=").append(this.name).append(", score=")
				.append(this.score).append(", lastRun=").append(this.lastRun).append(", brewGroup=")
				.append(this.brewGroup).append("]");
		return builder.toString();
	}

}
