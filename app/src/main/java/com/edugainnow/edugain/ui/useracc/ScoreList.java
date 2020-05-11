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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.edugainnow.edugain.R;
import com.edugainnow.edugain.util.Apis;
import com.edugainnow.edugain.util.CustomPerference;
import com.edugainnow.edugain.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ScoreList extends AppCompatActivity {

    private RecyclerView rvScoreList;
    private TextView txtNorecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_score_list);
        txtNorecords = findViewById(R.id.txtNorecords);
        rvScoreList = findViewById(R.id.rvScoreList);
        rvScoreList.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));

        if(Utils.isNetworkAvailable(this))
            executeAllScoreList();

    }

    void executeAllScoreList()
    {
        Utils.customProgress(this,"Please Wait...");
        JSONObject jsonObject =new JSONObject();
        try {
            jsonObject.put("Reg_ID", /*CustomPerference.getString(this,CustomPerference.USER_ID)*/"9839968806");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("req"+jsonObject);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Apis.AllScorList, jsonObject,
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
        RequestQueue queue = Volley.newRequestQueue(ScoreList.this);
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
        public ScoreListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_scorelist, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ScoreListAdapter.ViewHolder holder, int position) {

            try {
                JSONObject object = array.getJSONObject(position);
                holder.txtDate.setText(object.getString("attdate"));
                holder.txtquestion.setText(object.getString("TotalQues"));
                holder.txtcorrect.setText(object.getString("TotalRight"));
                holder.txtWrong.setText(object.getString("TotalWrong"));
                holder.txtMoney.setText("\u20b9 "+object.getString("amount"));
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
                    txtquestion,
                    txtcorrect,
                    txtWrong,
                    txtMoney;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                txtDate = itemView.findViewById(R.id.txtDate);
                txtquestion = itemView.findViewById(R.id.txtquestion);
                txtcorrect = itemView.findViewById(R.id.txtcorrect);
                txtWrong = itemView.findViewById(R.id.txtWrong);
                txtMoney = itemView.findViewById(R.id.txtMoney);
            }
        }
    }
}
