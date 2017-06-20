package com.puncturepoint.Adapter;

import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.puncturepoint.Fragments.User.RequestDetailFragment;
import com.puncturepoint.MainActivity;
import com.puncturepoint.R;
import com.puncturepoint.response.Request_Detail_Table;
import com.puncturepoint.utils.OnLoadMoreListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by mind on 21/6/16.
 */
public class MyRequestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    MainActivity mActivity;
    List<Request_Detail_Table> request_detail_tables;

    //------------------------------- Load More --------------------------------
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener mOnLoadMoreListener;
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;



    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    public MyRequestAdapter(List<Request_Detail_Table> request_detail_tables, MainActivity mActivity,RecyclerView rv_cat_product) {
        this.request_detail_tables = request_detail_tables;
        this.mActivity =mActivity;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) rv_cat_product.getLayoutManager();

        rv_cat_product.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                   /* System.out.println("you are in right conditions isLoading"+isLoading);
                    System.out.println("you are in right conditions totalItemCount"+totalItemCount);
                    System.out.println("you are in right conditions lastVisibleItem"+lastVisibleItem);
                    System.out.println("you are in right conditions visibleThreshold"+visibleThreshold);*/

                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {

                    //System.out.println("you are in right conditions CORRECT");

                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMore();
                    }

                    isLoading = true;

                }
            }
        });
        //--------------------------------------------------------------------------------------------

    }
    public void addData(List<Request_Detail_Table> request_detail_tables){

        this.request_detail_tables = request_detail_tables;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_my_request, null, false);
            return new ViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loading_item, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder_Main,final int position) {

        if (holder_Main instanceof ViewHolder) {

        final ViewHolder holder = (ViewHolder) holder_Main;

        holder.tv_title.setText(request_detail_tables.get(position).getSp_name());
        holder.tv_desc.setText(request_detail_tables.get(position).getAddress());
        holder.tv_mobile.setText(request_detail_tables.get(position).getMobile_no());
        //0=pending, 1=accept, 2=rejected, 3=completed (Accept means Mark as completed)

        if(request_detail_tables.get(position).getRequest_status().equals("0")){
            holder.tv_status.setText("Pending");
            holder.tv_status.setBackgroundResource(R.drawable.pending_box);
            Typeface face = Typeface.createFromAsset(mActivity.getAssets(),
                    "fonts/Roboto-Regular.ttf");
            holder.tv_status.setTypeface(face);
            holder.tv_status.setTextColor(mActivity.getResources().getColor(R.color.white));

        }else if(request_detail_tables.get(position).getRequest_status().equals("1")){
            holder.tv_status.setText("Accepted");
            holder.tv_status.setBackgroundResource(R.drawable.status_box);
            Typeface face = Typeface.createFromAsset(mActivity.getAssets(),
                    "fonts/Roboto-Regular.ttf");
            holder.tv_status.setTypeface(face);
            holder.tv_status.setTextColor(mActivity.getResources().getColor(R.color.white));

        }else if(request_detail_tables.get(position).getRequest_status().equals("2")){
            holder.tv_status.setText("Rejected");
            holder.tv_status.setBackgroundResource(R.drawable.pending_box);
            Typeface face = Typeface.createFromAsset(mActivity.getAssets(),
                    "fonts/Roboto-Regular.ttf");
            holder.tv_status.setTypeface(face);
            holder.tv_status.setTextColor(mActivity.getResources().getColor(R.color.white));


        }else if(request_detail_tables.get(position).getRequest_status().equals("3")){
            holder.tv_status.setText("Completed");
            Typeface face = Typeface.createFromAsset(mActivity.getAssets(),
                    "fonts/Sketch_Block.ttf");
            holder.tv_status.setTypeface(face);
            holder.tv_status.setTextColor(mActivity.getResources().getColor(R.color.pending));
            holder.tv_status.setBackgroundResource(R.drawable.box);

        }

        holder.card_view_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mActivity.pushFragmentDontIgnoreCurrent(RequestDetailFragment.newInstance1(request_detail_tables.get(position).getId()), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);

            }
        });

        } else if (holder_Main instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder_Main;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }


    @Override
    public int getItemCount() {
        return request_detail_tables == null ? 0 : request_detail_tables.size();
    }

    public void setLoaded() {
        isLoading = false;
    }

    @Override
    public int getItemViewType(int position) {
        return request_detail_tables.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_review_name)
        TextView tv_title;
        @Bind(R.id.tv_desc)
        TextView tv_desc;
        @Bind(R.id.tv_status)
        TextView tv_status;
        @Bind(R.id.tv_mobile)
        TextView tv_mobile;
        @Bind(R.id.card_view_request)
        CardView card_view_request;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {

        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
        }
    }



}
