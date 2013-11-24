package com.morgan.design.activity;

import static com.morgan.design.helpers.Constants.DONATE_URL;
import static com.morgan.design.helpers.Constants.MARKET_LINK_URL;
import static com.morgan.design.helpers.Constants.MORGAN_DESIGN;
import static com.morgan.design.helpers.Constants.TANGO_ICON_URL;
import static com.morgan.design.helpers.Constants.TWITTER_URL;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.common.base.Strings;
import com.morgan.design.R;
import com.morgan.design.adaptor.CreditsAdaptor;
import com.morgan.design.helpers.Credit;
import com.morgan.design.utils.BuildUtils;
import com.morgan.design.utils.Utils;

public class CreditsFragmentActivity extends BaseBrewFragmentActivity implements OnItemClickListener {

	private CreditsAdaptor adapter;
	private ListView listView;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_credits);

		listView = (ListView) findViewById(R.id.credits_list_view);

		List<Credit> credits = new ArrayList<Credit>();
		addCredits(credits);
		addVersion(credits);
		addDeviceId(credits);

		adapter = new CreditsAdaptor(this, R.layout.player_data_row, credits);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long duration) {
		Credit credit = (Credit) parent.getItemAtPosition(position);
		String webAddress = credit.getCreditWebAddress();
		if (!Strings.isNullOrEmpty(webAddress)) {
			Utils.openUrl(this, webAddress);
		}
	}

	private void addDeviceId(List<Credit> credits) {
		credits.add(credit("Device ID", BuildUtils.getDeviceId(), "", 0));
	}

	private void addVersion(List<Credit> credits) {
		credits.add(credit("Version", BuildUtils.getVersion(this), "", 0));
	}

	private void addCredits(List<Credit> credits) {
		credits.add(credit("Twitter", "Follow me", TWITTER_URL, R.drawable.twitter_logo));
		credits.add(credit("Created By", "James Morgan", MORGAN_DESIGN, R.drawable.morgan_design_icon));
		credits.add(credit("Weather Slider", "Try Weather Slider", MARKET_LINK_URL, R.drawable.weather_slider_icon));
		credits.add(credit("Donate", "Buy me a beer", DONATE_URL, R.drawable.donate_beer_icon));
		credits.add(credit("Rate Me", "Who's Making The Brew", MARKET_LINK_URL, R.drawable.rate_me_icon));
		credits.add(credit("Icons", "Tango Icon Library", TANGO_ICON_URL, R.drawable.tango_icon));
	}

	private Credit credit(String title, String caption, String url, int logo) {
		Credit credit = new Credit();
		credit.setTitle(title);
		credit.setCaption(caption);
		credit.setCreditWebAddress(url);
		credit.setLogo(logo);
		return credit;
	}

}
