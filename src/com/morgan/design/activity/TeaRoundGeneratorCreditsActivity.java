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

import com.morgan.design.R;
import com.morgan.design.adaptor.CreditsAdaptor;
import com.morgan.design.helpers.Credit;
import com.morgan.design.utils.BuildUtils;

public class TeaRoundGeneratorCreditsActivity extends BaseBrewListActivity implements OnItemClickListener {

	private CreditsAdaptor adapter;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_credits);

		// create our list and custom adapter
		List<Credit> credits = new ArrayList<Credit>();

		addCredits(credits);
		addVersion(credits);
		addDeviceId(credits);

		adapter = new CreditsAdaptor(this, R.layout.player_data_row, credits);

		setListAdapter(adapter);
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

	@Override
	public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long duration) {
		// @SuppressWarnings("unchecked")
		// final HashMap<String, ?> item = (HashMap<String, ?>) adapter.getItem(position);
		// if (item.containsKey(URL) && null != item.get(URL)) {
		// Utils.openUrl(this, item.get(URL).toString());
		// }
	}
}
