package com.morgan.design.utils;

import java.util.List;

public class EmailBuilder {

	protected EmailContentBuilder contentBuilder;

	public EmailContentBuilder content() {
		if (null == this.contentBuilder) {
			this.contentBuilder = new EmailContentBuilder(this);
		}
		return this.contentBuilder;
	}

	public static final class EmailContentBuilder {

		private final EmailBuilder parent;
		private final StringBuilder content = new StringBuilder();

		public EmailContentBuilder(final EmailBuilder emailBuilder) {
			this.parent = emailBuilder;
		}

		public EmailContentBuilder h1(final String h1Val) {
			this.getContent().append("<h1>").append(h1Val).append("</h1>");
			return this;
		}

		public EmailContentBuilder h2(final String h2Val) {
			this.getContent().append("<h2>").append(h2Val).append("</h2>");
			return this;
		}

		public EmailContentBuilder h3(final String h3Val) {
			this.getContent().append("<h3>").append(h3Val).append("</h3>");
			return this;
		}

		public EmailContentBuilder h4(final String h4Val) {
			this.getContent().append("<h4>").append(h4Val).append("</h4>");
			return this;
		}

		public EmailContentBuilder h5(final String h5Val) {
			this.getContent().append("<h5>").append(h5Val).append("</h5>");
			return this;
		}

		public EmailContentBuilder h6(final String h6Val) {
			this.getContent().append("<h6>").append(h6Val).append("</h6>");
			return this;
		}

		public EmailContentBuilder sub(final String pVal) {
			this.getContent().append("<sub>").append(pVal).append("</sub>");
			return this;
		}

		public EmailContentBuilder p(final String pVal) {
			this.getContent().append("<p>").append(pVal).append("</p>");
			return this;
		}

		public TableBuilder table() {
			return new TableBuilder(this, this.getContent());
		}

		public EmailBuilder endContent() {
			return this.parent;
		}

		public StringBuilder getContent() {
			return this.content;
		}
	}

	public static final class TableBuilder {
		private final EmailContentBuilder parent;
		private final StringBuilder content;

		public TableBuilder(final EmailContentBuilder emailContentBuilder, final StringBuilder content) {
			this.parent = emailContentBuilder;
			this.content = content;
			this.content.append("<table>");
		}

		public TableBuilder row(final List<String> cells) {
			this.content.append("<tr>");
			for (final String cell : cells) {
				this.content.append("<td>").append(cell == null ? "" : cell.toString()).append("</td>");
			}
			this.content.append("</tr>");
			return this;
		}

		public TableBuilder tbodyStart() {
			this.content.append("<tbody>");
			return this;
		}

		public TableBuilder tbodyEnd() {
			this.content.append("</tbody>");
			return this;
		}

		public TableBuilder row(final Object... cells) {
			this.content.append("<tr>");
			for (final Object cell : cells) {
				this.content.append("<td>").append(cell == null ? "" : cell.toString()).append("</td>");
			}
			this.content.append("</tr>");
			return this;
		}

		public EmailContentBuilder endTable() {
			this.content.append("</table>");
			return this.parent;
		}

	}

	public String build() {
		return this.contentBuilder.getContent().toString();
	}

}
