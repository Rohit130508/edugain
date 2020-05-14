package com.edugainnow.edugain.ui.useracc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.edugainnow.edugain.R;
import com.edugainnow.edugain.util.Apis;
import com.edugainnow.edugain.util.CustomPerference;
import com.edugainnow.edugain.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PriceHistory extends AppCompatActivity {


    private RecyclerView rvScoreList;
    private TextView txtNorecords,
            txtCancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_price_history);
        txtNorecords = findViewById(R.id.txtNorecords);
        rvScoreList = findViewById(R.id.rvScoreList);
        rvScoreList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));

        findViewById(R.id.txtCancel).setOnClickListener(v -> finish());
        if(Utils.isNetworkAvailable(this))
            executeAllScoreList();

    }

    void executeAllScoreList()
    {
        Utils.customProgress(this,"Please Wait...");
        JSONObject jsonObject =new JSONObject();
        try {
            jsonObject.put("Reg_ID", CustomPerference.getString(this,CustomPerference.USER_ID));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("req"+jsonObject);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Apis.PrizeCreditHistry, jsonObject,
                response -> {
                    Utils.customProgressStop();
                    System.out.println("req"+response);

                    try {
                        boolean status = response.getBoolean("Status");
                        if(status)
                        {
                            JSONArray array = response.getJSONArray("Data");
                            if(array.isNull(0))
                            {
                                txtNorecords.setVisibility(View.VISIBLE);
                                txtNorecords.setText("No records Found");
                            }else {
                                txtNorecords.setVisibility(View.GONE);

                                ScoreListAdapter adapter = new ScoreListAdapter(array);
                                rvScoreList.setAdapter(adapter);

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        System.out.println("error"+e.getMessage());
                    }

                }, error -> {
            System.out.println("error"+error.getMessage());
        });
        RequestQueue queue = Volley.newRequestQueue(PriceHistory.this);
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);

    }

    class ScoreListAdapter extends RecyclerView.Adapter<ScoreListAdapter.ViewHolder>
    {

        JSONArray array = new JSONArray();
        public ScoreListAdapter(JSONArray array) {
            this.array = array;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_prise_history, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            try {
                JSONObject object = array.getJSONObject(position);
                holder.txtDate.setText(object.getString("Quizedate"));
                holder.txtFullName.setText(object.getString("FullName"));
                holder.txtPackName.setText(object.getString("PackageName"));
                holder.txtcrAmount.setText(object.getString("CrAmount"));
                holder.txttransId.setText(object.getString("TransID"));
                holder.txtMoney.setText("\u20b9 "+object.getString("CrAmount"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return array.length();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtDate,
                    txtFullName,
                    txtPackName,
                    txttransId,
                    txtMoney,
                    txtcrAmount;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                txtDate = itemView.findViewById(R.id.txtDate);
                txtFullName = itemView.findViewById(R.id.txtFullName);
                txtPackName = itemView.findViewById(R.id.txtPackName);
                txtcrAmount = itemView.findViewById(R.id.txtcrAmount);
                txttransId = itemView.findViewById(R.id.txttransId);
                txtMoney = itemView.findViewById(R.id.txtMoney);
            }
        }
    }
}
