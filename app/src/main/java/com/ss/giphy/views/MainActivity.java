package com.ss.giphy.views;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ss.giphy.R;
import com.ss.giphy.fragments.FragmentFav;
import com.ss.giphy.fragments.FragmentTopRated;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shubham
 * Main screen to display Top Rated gif'ss and Favourite gif's using fragments and tab layout.
 */
public class MainActivity extends AppCompatActivity {

	private Toolbar toolbar;
	private TabLayout tabLayout;
	private ViewPager viewPager;
	private ImageView tab_header;
	private AppBarLayout appbar;
	private CollapsingToolbarLayout collapse_toolbar;
	private DataListener mDataListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		handleIntent(getIntent());
	}

	/**
	 * Initialise widgets.
	 */
	private void init() {
		appbar = findViewById(R.id.appbar);
		collapse_toolbar = findViewById(R.id.collapse_toolbar);
		toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		tab_header = appbar.findViewById(R.id.tab_header);

		Glide.with(this).load(R.raw.gif).into(tab_header);

		viewPager = findViewById(R.id.viewpager);
		setupViewPager(viewPager);

		tabLayout = findViewById(R.id.tabs);
		tabLayout.setupWithViewPager(viewPager);

		initCollapsingToolbar();
	}

	/**
	 * Set fragments to view pager.
	 *
	 * @param viewPager
	 */
	private void setupViewPager(ViewPager viewPager) {
		ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
		adapter.addFragment(new FragmentTopRated(), "Top Rated Gifs");
		adapter.addFragment(new FragmentFav(), "Favourite Gifs");
		viewPager.setAdapter(adapter);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_main, menu);

		// Associate searchable configuration with the SearchView
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				mDataListener.onDataReceived(query);
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				mDataListener.onDataReceived(newText);
				return true;
			}
		});

		if(searchView.isIconified()){
			mDataListener.onDataReceived("");
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_search) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		handleIntent(intent);
	}

	private void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			Toast.makeText(getApplicationContext(), query, Toast.LENGTH_LONG).show();
		}
	}


	/**
	 * Adapter class to load fragments on view pager.
	 */
	class ViewPagerAdapter extends FragmentPagerAdapter {
		private final List<Fragment> mFragmentList = new ArrayList<>();
		private final List<String> mFragmentTitleList = new ArrayList<>();

		public ViewPagerAdapter(FragmentManager manager) {
			super(manager);
		}

		@Override
		public Fragment getItem(int position) {
			return mFragmentList.get(position);
		}

		@Override
		public int getCount() {
			return mFragmentList.size();
		}

		public void addFragment(Fragment fragment, String title) {
			mFragmentList.add(fragment);
			mFragmentTitleList.add(title);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return mFragmentTitleList.get(position);
		}
	}

	private void initCollapsingToolbar() {
		collapse_toolbar.setTitle(" ");
		appbar.setExpanded(true);

		// hiding & showing the title when toolbar expanded & collapsed
		appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
			boolean isShow = false;
			int scrollRange = -1;

			@Override
			public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
				if (scrollRange == -1) {
					scrollRange = appBarLayout.getTotalScrollRange();
				}
				if (scrollRange + verticalOffset == 0) {
					collapse_toolbar.setTitle(getString(R.string.app_name));
					isShow = true;
				} else if (isShow) {
					collapse_toolbar.setTitle(" ");
					isShow = false;
				}
			}
		});
	}

	/**
	 * Interface to tranferring information to the fragment.
	 */
	public interface DataListener {
		void onDataReceived(String s);
	}

	public void setDataListener(DataListener listener) {
		this.mDataListener = listener;
	}
}
