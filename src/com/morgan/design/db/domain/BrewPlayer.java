package com.morgan.design.db.domain;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = BrewPlayer.BREW_PLAYERS)
public class BrewPlayer implements Serializable {

	private static final long serialVersionUID = 8844544489762032207L;

	public final static String BREW_PLAYERS = "brew_players";
	public static final String ID = "id";
	public static final String BREW_GROUP_ID = "fk_group_id";
	public static final String LAST_RUN = "lastRun";

	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField(canBeNull = false)
	private String name;

	@DatabaseField(canBeNull = true, defaultValue = "0")
	private int score;

	@DatabaseField(canBeNull = true, defaultValue = "false", columnName = LAST_RUN)
	private boolean lastRun = false;

	@DatabaseField(canBeNull = true, foreign = true, foreignAutoRefresh = true, columnName = BREW_GROUP_ID)
	private BrewGroup brewGroup;

	public BrewPlayer() {
		//
	}

	public final int getId() {
		return id;
	}

	public final void setId(final int id) {
		this.id = id;
	}

	public final String getName() {
		return name;
	}

	public final void setName(final String name) {
		this.name = name;
	}

	public final int getScore() {
		return score;
	}

	public final void setScore(final int score) {
		this.score = score;
	}

	public boolean isLastRun() {
		return lastRun;
	}

	public void setLastRun(final boolean lastRun) {
		this.lastRun = lastRun;
	}

	public BrewGroup getBrewGroup() {
		return brewGroup;
	}

	public void setBrewGroup(final BrewGroup brewGroup) {
		this.brewGroup = brewGroup;
	}

}
