package com.morgan.design.activity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.morgan.design.R;
import com.morgan.design.helpers.Constants;
import com.morgan.design.ui.SeparatedListAdapter;
import com.morgan.design.utils.BuildUtils;
import com.morgan.design.utils.Utils;

public class TeaRoundGeneratorCreditsActivity extends BaseBrewActivity {

	private final static String ITEM_TITLE = "title";
	private final static String ITEM_CAPTION = "caption";
	private final static String SUB_CAPTION = "sub_caption";
	private final static String URL = "complex_url";
	private final static String ITEM_IMAGE = "image";

	private SeparatedListAdapter adapter;
	private ListView list;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// create our list and custom adapter
		adapter = new SeparatedListAdapter(this, R.layout.list_header);
		list = new ListView(this);

		final List<Map<String, ?>> credits = new LinkedList<Map<String, ?>>();
		credits.add(createImageItem("Twitter", "Follow me", Constants.TWITTER_URL, Constants.TWITTER_URL, R.drawable.twitter_logo));
		credits.add(createImageItem("Created By", "James Morgan", Constants.MORGAN_DESIGN, Constants.MORGAN_DESIGN,
				R.drawable.morgan_design_icon));
		credits.add(createImageItem("Weather Slider", "Try Weather Slider", Constants.MARKET_LINK_URL, R.drawable.weather_slider_icon));
		credits.add(createImageItem("Donate", "Buy me a beer", Constants.DONATE_URL, R.drawable.donate_beer_icon));
		credits.add(createImageItem("Rate Me", "Who's Making The Brew", Constants.MARKET_LINK_URL, R.drawable.rate_me_icon));
		credits.add(createImageItem("Icons", "Tango Icon Library", Constants.TANGO_ICON_URL, R.drawable.tango_icon));

		adapter.addSection("Credits", new SimpleAdapter(this, credits, R.layout.list_complex_sub_with_image, new String[] { ITEM_TITLE,
				ITEM_CAPTION, SUB_CAPTION, URL, ITEM_IMAGE }, new int[] { R.id.list_complex_title, R.id.list_complex_caption,
				R.id.list_complex_sub_caption, R.id.list_complex_url, R.id.list_complex_image }));

		final List<Map<String, ?>> version = new LinkedList<Map<String, ?>>();
		version.add(createSimple(BuildUtils.getVersion(this)));

		adapter.addSection("Version", new SimpleAdapter(this, version, R.layout.list_simple, new String[] { ITEM_TITLE },
				new int[] { R.id.list_simple_title }));

		final List<Map<String, ?>> deviceId = new LinkedList<Map<String, ?>>();
		deviceId.add(createSimple(BuildUtils.getDeviceId()));

		adapter.addSection("Device ID", new SimpleAdapter(this, deviceId, R.layout.list_simple, new String[] { ITEM_TITLE },
				new int[] { R.id.list_simple_title }));

		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long duration) {
				@SuppressWarnings("unchecked")
				final HashMap<String, ?> item = (HashMap<String, ?>) adapter.getItem(position);
				if (item.containsKey(URL) && null != item.get(URL)) {
					Utils.openUrl(TeaRoundGeneratorCreditsActivity.this, item.get(URL).toString());
				}
			}
		});

		list.setAdapter(adapter);
		setContentView(list);
	}

	private Map<String, ?> createImageItem(final String title, final String caption, final String url, final int imageId) {
		final Map<String, Object> item = new HashMap<String, Object>();
		item.put(ITEM_TITLE, title);
		item.put(ITEM_CAPTION, caption);
		item.put(SUB_CAPTION, null);
		item.put(URL, url);
		item.put(ITEM_IMAGE, imageId);
		return item;
	}

	private Map<String, ?> createImageItem(final String title, final String caption, final String subCaption, final String url,
			final int imageId) {
		final Map<String, Object> item = new HashMap<String, Object>();
		item.put(ITEM_TITLE, title);
		item.put(ITEM_CAPTION, caption);
		item.put(SUB_CAPTION, subCaption);
		item.put(URL, url);
		item.put(ITEM_IMAGE, imageId);
		return item;
	}

	private Map<String, ?> createSimple(final String title) {
		final Map<String, Object> item = new HashMap<String, Object>();
		item.put(ITEM_TITLE, title);
		return item;
	}
}
