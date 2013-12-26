package com.morgan.design.db.domain;

import java.io.Serializable;
import java.util.Collection;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "brew_groups")
public class BrewGroup implements Serializable {

	private static final long serialVersionUID = 8708960443720110522L;

	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField(canBeNull = true)
	private String name;

	@ForeignCollectionField
	private Collection<BrewPlayer> brewPlayers;

	public BrewGroup() {
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

	public final Collection<BrewPlayer> getBrewPlayers() {
		return brewPlayers;
	}

	public int getSize() {
		return brewPlayers.size();
	}

	public boolean hasNoPlayers() {
		return null == brewPlayers || brewPlayers.isEmpty();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BrewGroup [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", brewPlayers=");
		builder.append(brewPlayers);
		builder.append("]");
		return builder.toString();
	}

}
