package com.morgan.design.utils;

import java.util.List;

import com.morgan.design.db.domain.BrewPlayer;
import com.morgan.design.utils.EmailBuilder.TableBuilder;

public class HTMLResultsBuilder {

	private final List<BrewPlayer> resultsList;

	private HTMLResultsBuilder(final List<BrewPlayer> resultsList) {
		this.resultsList = resultsList;
	}

	public static String createFromResults(final List<BrewPlayer> resultsList) {
		return new HTMLResultsBuilder(resultsList).buildScoresEmail();
	}

	private String buildScoresEmail() {
		// @formatter:off
		final TableBuilder table = new EmailBuilder()
				.content()
				.h3(this.resultsList.get(0).getName()
						+ " Won The Tea Round......!!!")
				.p("Scores as it stands: ").table().tbodyStart()
				.row(" #   ", " Score   ", " Name   ");

		for (int i = 0; i < this.resultsList.size(); i++) {
			table.row(result(i), score(i), name(i));
		}

		final EmailBuilder emailBuilder = table.tbodyStart().endTable()
				.p("<a href='http://www.morgan-design.com'>Morgan-Design</a>")
				.endContent();
		// @formatter:on
		return emailBuilder.build();
	}

	private String name(final int index) {
		return this.resultsList.get(index).getName();
	}

	private String score(final int index) {
		return Integer.toString(this.resultsList.get(index).getScore());
	}

	private String result(final int index) {
		final int position = index + 1;
		return position + Utils.getSuffix(position);
	}
}
