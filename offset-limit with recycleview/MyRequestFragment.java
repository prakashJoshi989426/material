package com.puncturepoint.Fragments.User;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.commonmodule.mi.utils.CommonMsgsAndKeys;
import com.commonmodule.mi.utils.ConnectionUtil;
import com.puncturepoint.Adapter.MyRequestAdapter;
import com.puncturepoint.Fragments.Common_fragment.BaseFragment;
import com.puncturepoint.MainActivity;
import com.puncturepoint.R;
import com.puncturepoint.response.Request_Detail_Table;
import com.puncturepoint.response.ServiceRequestListingResponse;
import com.puncturepoint.utils.Methods;
import com.puncturepoint.utils.OnLoadMoreListener;
import com.puncturepoint.webservices.WebApiClient;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyRequestFragment extends BaseFragment implements View.OnClickListener {

    public boolean isFromViewPager = false;
    @Bind(R.id.recycle_request)
    RecyclerView recycle_request;
    @Bind(R.id.tv_no_data_found)
    TextView tv_no_data_found;
    MyRequestAdapter requestAdapter;
    int offset = 0;
    int limit = 10;

    public static MyRequestFragment newInstance(boolean isFromViewPager) {
        MyRequestFragment fragment = new MyRequestFragment();
        Bundle args = new Bundle();
        args.putBoolean("isFromViewPager", isFromViewPager);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isFromViewPager = getArguments().getBoolean("isFromViewPager");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_request, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
        SetHeader();
        initComponent();

    }

    public void SetHeader() {

        if (!isFromViewPager) {
            mActivity.setTitle(mActivity.getString(R.string.my_profile), MainActivity.Back_Button_Show, 0, 0);
        }

    }


    public void initComponent() {

        recycle_request.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycle_request.setLayoutManager(linearLayoutManager);
        getServiceRequestList(mActivity, "request_list", offset, limit);
        common_methods.getEstimatedPrice(mActivity, "estimated_price", common_methods.getCurrentTimeStamp());

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

        }

    }

    private void getServiceRequestList(Activity activity, String tag, final int offset_, int limit) {

        if (ConnectionUtil.isInternetAvailable(mActivity)) {
            Methods.isProgressShow(mActivity);

            Call<ServiceRequestListingResponse> response_Call = WebApiClient.getInstance(activity).getWebApi_another().getServiceList(tag, "" + offset_, "" + limit);
            response_Call.enqueue(new Callback<ServiceRequestListingResponse>() {
                @Override
                public void onResponse(Call<ServiceRequestListingResponse> call, Response<ServiceRequestListingResponse> response) {

                    if (response.code() == 200) {

                        ServiceRequestListingResponse serviceResponse = response.body();
                        String status = serviceResponse.getStatus();

                        if (status.equals(CommonMsgsAndKeys.SUCCESS)) {

                            if (offset_ == 0) {
                                Delete.tables(Request_Detail_Table.class);
                            }

                            if (serviceResponse.getData().length > 0) {
                                if (serviceResponse.getOffset().toString().length() > 0) {
                                    offset = Integer.parseInt(serviceResponse.getOffset().toString());
                                }
                            }

                            if (serviceResponse.getData() != null) {
                                for (int i = 0; i < serviceResponse.getData().length; i++) {
                                    Request_Detail_Table table = new Request_Detail_Table();
                                    table = serviceResponse.getData()[i];
                                    table.save();
                                }
                            }
                            setRequestAdapter();


                            methods.isProgressHide();


                        } else if (status.equals(CommonMsgsAndKeys.FAILURE)) {

                            common_methods.getStatus_Failure(response.body().getMessage(), mActivity);

                        } else if (status.equals(CommonMsgsAndKeys.INACTIVE_USER)) {

                            common_methods.getStatus_InActiveUser(response.body().getMessage(), mActivity);

                        } else if (status.equals(CommonMsgsAndKeys.SERVER_UNDER_MAINTENANCE)) {

                            common_methods.getStatus_ServerUnderMaintenance(response.body().getMessage(), mActivity);

                        } else if (status.equals(CommonMsgsAndKeys.APP_IS_UPDATED)) {

                            common_methods.getStatus_AppIsUpdated(response.body().getMessage(), mActivity);

                        }

                        Methods.isProgressHide();
                    } else {
                        Methods.ShowSnackbar(mActivity.rl_container, CommonMsgsAndKeys.something_went_wrong);
                    }
                    Methods.isProgressHide();

                }

                @Override
                public void onFailure(Call<ServiceRequestListingResponse> call, Throwable t) {
                    t.printStackTrace();
                    common_methods.get_show_Error("" + t.getMessage(), mActivity);
                    Methods.isProgressHide();

                }
            });
        } else {
            Methods.ShowSnackbar(mActivity.rl_container, CommonMsgsAndKeys.noInternet);
        }

    }


    private void setRequestAdapter() {

        List<Request_Detail_Table> request_detail_tables = new Select().from(Request_Detail_Table.class)
                .queryList();

     /*   if (requestAdapter==null){
            requestAdapter = new MyRequestAdapter(request_detail_tables,mActivity,recycle_request);
            recycle_request.setAdapter(requestAdapter);
        }else {
            requestAdapter.addData(request_detail_tables);
        }*/

        if (request_detail_tables.size() > 0) {
            tv_no_data_found.setVisibility(View.GONE);
        } else {
            tv_no_data_found.setVisibility(View.VISIBLE);
            tv_no_data_found.setText("No any request you have sent yet");
        }

        setupRecyclerView(recycle_request, request_detail_tables);

    }

    public void setupRecyclerView(RecyclerView recyclerView, final List<Request_Detail_Table> request_detail_tables) {

        if (requestAdapter == null) {
            requestAdapter = new MyRequestAdapter(request_detail_tables, mActivity, recyclerView);
            recycle_request.setAdapter(requestAdapter);
        } else {
            requestAdapter.addData(request_detail_tables);
            requestAdapter.setLoaded();

        }

        requestAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                Log.e("Load More", "Category Items");

                if (!request_detail_tables.contains(null)) {
                    request_detail_tables.add(null);
                    requestAdapter.notifyItemInserted(request_detail_tables.size() - 1);
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (request_detail_tables.contains(null)) {
                            request_detail_tables.remove(request_detail_tables.indexOf(null));
                        }
                        //category_tables_list.remove(category_tables_list.size() - 1);

                        requestAdapter.notifyItemRemoved(request_detail_tables.size());
                        getServiceRequestList(mActivity, "request_list", offset, limit);

                    }
                }, 2000);
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            SetHeader();
        }
    }

}
