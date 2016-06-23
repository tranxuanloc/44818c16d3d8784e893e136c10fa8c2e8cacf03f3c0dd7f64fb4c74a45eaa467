package com.scsvn.whc_2016.main.gps.listuser;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.gps.GridDecoration;
import com.scsvn.whc_2016.main.gps.MapsActivity;
import com.scsvn.whc_2016.main.gps.RecyclerViewTouchListener;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ListUserActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = ListUserActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private View.OnClickListener tryAgain;
    private UserAdapter adapter;
    private ArrayList<UserInfo> data = new ArrayList<>();
    private Toolbar toolbar;
    private boolean isChoiceMode, isSearch;
    private ImageView ivCancel;
    private TextView tvActivityTitle, tvCounter;
    private String keyword;
    private SwipeRefreshLayout refreshLayout;
    private int flag = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert toolbar != null;
        ivCancel = (ImageView) toolbar.findViewById(R.id.iv_toolbar_cancel);
        tvActivityTitle = (TextView) toolbar.findViewById(R.id.tv_toolbar_name);
        tvCounter = (TextView) toolbar.findViewById(R.id.tv_toolbar_counter);
        ivCancel.setOnClickListener(this);
        updateToolbar();

        tryAgain = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getListUser();
            }
        };
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        assert refreshLayout != null;
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getListUser();
            }
        });
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        int spanCount = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));
        recyclerView.setHasFixedSize(false);
        recyclerView.addItemDecoration(new GridDecoration(spanCount, getResources().getDimensionPixelSize(R.dimen.grid_spacing), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new UserAdapter(this, data);

        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(this, recyclerView, new RecyclerViewTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (isChoiceMode) {
                    toggleSelection(position);
                    if (adapter.getSelectedItemCount() == 0) isChoiceMode = false;
                    updateToolbar();
                } else {
                    Intent intent = new Intent(ListUserActivity.this, MapsActivity.class);
                    intent.putExtra("USER", adapter.getItem(position).getUserName());
                    startActivity(intent);
                }
            }

            @Override
            public void onLongClick(View view, int position) {
                if (!isChoiceMode) {
                    isChoiceMode = true;
                    updateToolbar();
                    toggleSelection(position);
                }
            }
        }));
        getListUser();
    }

    private void getListUser() {
        progressBar.setVisibility(View.VISIBLE);
        if (!Utilities.isConnected(this)) {
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, recyclerView, tryAgain);
            updateUI();
            return;
        }
        MyRetrofit.initRequest(this).getListUser((short) flag).enqueue(new Callback<List<UserInfo>>() {
            @Override
            public void onResponse(Response<List<UserInfo>> response, Retrofit retrofit) {
                Log.d(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.body() != null && response.isSuccess()) {
                    data.clear();
                    data.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
                updateUI();
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorWithAction(ListUserActivity.this, t, TAG, recyclerView, tryAgain);
                updateUI();
            }
        });
    }


    private void updateUI() {
        refreshLayout.setRefreshing(false);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        updateToolbar();
        return true;
    }

    private void setUpSearchView() {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem menuItem = toolbar.getMenu().findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setMaxWidth((int) TypedValue.applyDimension(TypedValue.TYPE_DIMENSION, 100, getResources().getDisplayMetrics()));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                doSearch(newText);
                return true;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                isSearch = false;
                keyword = "";
                return false;
            }
        });
        if (isSearch) {
            searchView.setQuery(keyword, false);
        }
    }

    private void doSearch(String keyword) {
        isSearch = true;
        this.keyword = keyword;
        adapter.getFilter().filter(keyword);
    }


    private void toggleSelection(int position) {
        adapter.toggleSelection(position);
        String counter = String.format(Locale.US, "%d", adapter.getSelectedItemCount());
        tvCounter.setText(counter);
    }

    private void updateToolbar() {
        if (isChoiceMode) {
            tvCounter.setVisibility(View.VISIBLE);
            ivCancel.setVisibility(View.VISIBLE);
            tvActivityTitle.setVisibility(View.GONE);
            toolbar.getMenu().clear();
            toolbar.inflateMenu(R.menu.menu_start_action_mode);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.action_place) {
                        ArrayList<String> listUser = new ArrayList<>();
                        ArrayList<Integer> itemPosition = adapter.getSelectedItems();
                        for (int i = 0; i < itemPosition.size(); i++) {
                            listUser.add(adapter.getItem(itemPosition.get(i)).getUserName());
                        }
                        Intent intent = new Intent(ListUserActivity.this, MapsActivity.class);
                        intent.putStringArrayListExtra("LIST_USER", listUser);
                        startActivity(intent);
                        return true;
                    }
                    return false;
                }
            });
        } else {
            tvCounter.setVisibility(View.GONE);
            ivCancel.setVisibility(View.GONE);
            tvActivityTitle.setVisibility(View.VISIBLE);
            toolbar.getMenu().clear();
            toolbar.inflateMenu(R.menu.menu_list_user);
            final MenuItem itemFilter = toolbar.getMenu().findItem(R.id.action_filter);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();
                    if (id == R.id.action_filter_all) {
                        flag = 0;
                        item.setChecked(true);
                        itemFilter.setTitle(getString(R.string.all));
                        getListUser();
                    } else if (id == R.id.action_filter_outside) {
                        flag = 1;
                        item.setChecked(true);
                        itemFilter.setTitle(getString(R.string.outside));
                        getListUser();
                    } else if (id == R.id.action_filter_online) {
                        flag = 2;
                        item.setChecked(true);
                        itemFilter.setTitle(getString(R.string.online));
                        getListUser();
                    }
                    return true;
                }
            });
            setUpSearchView();
        }
    }


    @Override
    public void onClick(View v) {
        if (v == ivCancel) {
            isChoiceMode = false;
            adapter.clearSelections();
            updateToolbar();
        }
    }
}
