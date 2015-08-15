package moose.com.ac.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import moose.com.ac.ArticleListAdapter;
import moose.com.ac.R;
import moose.com.ac.retrofit.article.Article;
import moose.com.ac.ui.view.MultiSwipeRefreshLayout;

/**
 * Created by Farble on 2015/8/13 23.
 */
public abstract class ListFragment extends Fragment {
    private static final String TAG = "ListFragment";
    protected View rootView;
    protected RecyclerView mRecyclerView;
    protected LinearLayoutManager mLayoutManager;
    protected MultiSwipeRefreshLayout mSwipeRefreshLayout;

    protected List<Article>lists = new ArrayList<>();
    protected ArticleListAdapter adapter = new ArticleListAdapter(lists,getActivity());
    protected boolean isRequest = false;//request data status
    protected boolean isScroll = false;//is RecyclerView scrolling

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(
                R.layout.fragment_article_list, container, false);
        initRecyclerView();
        initRefreshLayout();
        init();
        return rootView;
    }


    private void initRefreshLayout() {
        mSwipeRefreshLayout = (MultiSwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);

        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.md_orange_700, R.color.md_red_500,
                R.color.md_indigo_900, R.color.md_green_700);
        mSwipeRefreshLayout.setSwipeableChildren(R.id.recycler_view);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            Log.i(TAG, "onRefresh called from SwipeRefreshLayout");
            doSwapeRefresh();
        });
    }


    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                isScroll = newState == RecyclerView.SCROLL_STATE_SETTLING;
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isScroll && recyclerView.canScrollVertically(1)) {
                    loadMore();
                }
            }
        });
    }
    protected abstract void init();

    protected abstract void loadMore();

    protected abstract void doSwapeRefresh();

    protected void Snack(String msg){
        Snackbar.make(mRecyclerView,msg,Snackbar.LENGTH_SHORT).show();
    }

}
