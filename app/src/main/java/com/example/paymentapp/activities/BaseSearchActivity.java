package com.example.paymentapp.activities;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.paymentapp.R;

public abstract class BaseSearchActivity extends BaseActivity {

    // Abstracts
    public abstract void onClickCloseButton();
    public abstract void onQueryTextResult(String query);

    // Attributes
    protected SearchView searchView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem menuSearchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if(searchManager!=null) {
            // Set attributes to search view
            searchView = (SearchView) menuSearchItem.getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(true);
            searchView.setQueryHint(getString(R.string.all_search));
            searchView.setMaxWidth(Integer.MAX_VALUE);

            // Handle search view icons
            ImageView closeButton = searchView.findViewById(R.id.search_close_btn);
            ImageView searchIcon = searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
            closeButton.setColorFilter(Color.WHITE);
            searchIcon.setColorFilter(Color.WHITE);

            // Handle search view texts
            EditText searchEditText = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
            searchEditText.setTextColor(Color.WHITE);
            searchEditText.setHintTextColor(Color.WHITE);

            // Set listeners
            closeButton.setOnClickListener(onClickListener);
            searchView.setOnQueryTextListener(onQueryTextListener);
        }
        return true;
    }

    // Listeners
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Clear query and collapsed search view
            EditText et = findViewById(R.id.search_src_text);
            et.setText("");
            searchView.setQuery("", false);
            searchView.onActionViewCollapsed();
            searchView.clearFocus();
            onClickCloseButton();
        }
    };

    private SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            onQueryTextResult(s);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            return false;
        }
    };

}