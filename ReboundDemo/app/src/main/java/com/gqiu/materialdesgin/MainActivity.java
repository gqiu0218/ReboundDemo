package com.gqiu.materialdesgin;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.yqritc.recyclerviewflexibledivider.VerticalDividerItemDecoration;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements ImageRecyclerCardAdapter.OnItemClickListener, BrowsePicDialog.DismissListener {

    private ImageRecyclerCardAdapter mAdapter;
    private View itemView;
    private boolean mSpring = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.pretty_gril);
        setSupportActionBar(toolbar);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(this)
                        .color(Color.WHITE)
                        .sizeResId(R.dimen.divider)
                        .build());
        mRecyclerView.addItemDecoration(new VerticalDividerItemDecoration.Builder(this)
                .color(Color.WHITE)
                .sizeResId(R.dimen.divider)
                .build());


        //设置网格布局管理器
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mAdapter = new ImageRecyclerCardAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);
        requestData();
    }


    private void requestData() {
        HttpEngine.getInstance().getImageList(new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                JSONObject jo = JSONObject.parseObject(responseString);
                String data = jo.getString("imgs");

                List<ImageBean> result = JSONObject.parseArray(data, ImageBean.class);
                mAdapter.setData(result);
            }
        });
    }

    @Override
    public void onItemClick(View v, ImageBean item) {
        int[] loc = new int[2];
        v.getLocationInWindow(loc);

        //减去状态栏高度
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        loc[1] = loc[1] - statusBarHeight;

        itemView = v;
        BrowsePicDialog dialog = new BrowsePicDialog(this, v.getWidth(), v.getHeight(), loc[0], loc[1], item.getImageUrl(), this, mSpring);
        dialog.show();

    }

    @Override
    public void dismiss() {
        itemView.setVisibility(View.VISIBLE);
    }

    @Override
    public void show() {
        itemView.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_has_spring:
                mSpring = true;
                break;
            case R.id.action_no_spring:
                mSpring = false;
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
