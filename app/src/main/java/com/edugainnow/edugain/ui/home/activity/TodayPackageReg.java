package com.edugainnow.edugain.ui.home.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.edugainnow.edugain.R;
import com.edugainnow.edugain.util.Apis;
import com.edugainnow.edugain.util.CustomPerference;
import com.edugainnow.edugain.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TodayPackageReg extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_AppCompat_DayNight_NoActionBar);
        setContentView(R.layout.activity_today_package_reg);
        initView();
        getExecuteMethods();


    }

        RecyclerView rvNewRegList;
        TextView txtNorecords;
        ArrayList<NewRegModel> arrayList = new ArrayList<>();

        void initView()
        {
            txtNorecords = findViewById(R.id.txtNorecords);
            rvNewRegList = findViewById(R.id.rvNewRegList);
            rvNewRegList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        }
        void getExecuteMethods()
        {
            if(Utils.isNetworkAvailable(this))
                executeTodayReg();
            else
                Utils.CustomAlert(this,R.string.nointernet_title,R.string.nointernet_message);
        }
        void executeTodayReg()
        {
            JSONObject object = new JSONObject();
            try {
                object.put("RegID",
                        CustomPerference.getString(TodayPackageReg.this,CustomPerference.USER_ID
                        ));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Utils.customProgress(this,"Please Wait ...");
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Apis.TodayPackageReg, object,
                    response -> {

                        Utils.customProgressStop();
                        System.out.println("Responce"+response);
                        JSONArray array = new JSONArray();

                        if(array.isNull(0))
                        {
                            txtNorecords.setVisibility(View.VISIBLE);
                        }else {
                            txtNorecords.setVisibility(View.GONE);

                            for (int i = 0; i < array.length(); i++) {
                                try {

                                    JSONObject jsonObject = array.getJSONObject(i);
                                    NewRegModel model = new NewRegModel();
                                    model.setPackName(jsonObject.getString("packName"));
                                    model.setPackPrice(jsonObject.getString("packPrice"));
                                    model.setDescription(jsonObject.getString("description"));
                                    model.setAmount(jsonObject.getString("amount"));
                                    model.setSHour(jsonObject.getString("SHour"));
                                    model.setId(jsonObject.getString("id"));

                                    arrayList.add(model);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                TodayPackageAdapter adapter = new TodayPackageAdapter(arrayList);
                                rvNewRegList.setAdapter(adapter);

                            }
                        }

                    }, error -> {

                Utils.customProgressStop();
            });

            RequestQueue queue = Volley.newRequestQueue(TodayPackageReg.this);
            request.setRetryPolicy(new DefaultRetryPolicy(20*1000, 2,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(request);

        }

        class TodayPackageAdapter extends RecyclerView.Adapter<TodayPackageAdapter.ViewHolder>
        {

            ArrayList<NewRegModel> arrayList;

            TodayPackageAdapter(ArrayList<NewRegModel> arrayList) {
                this.arrayList = arrayList;
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_new_reg, parent, false);
                return new ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

                NewRegModel model = arrayList.get(position);
                holder.txtPackName.setText(model.getPackName());
                holder.txtPackPrice.setText(model.getPackPrice());
                holder.txtstartTime.setText(model.getSHour());
                holder.btnQuizStart.setOnClickListener(v ->
                        startActivity(new Intent(TodayPackageReg.this, PackageRegistration.class)
                                .putExtra("packName",model.getPackName())
                                .putExtra("amount",model.getAmount())
                                .putExtra("packageId",model.getId())
                        ));

            }

            @Override
            public int getItemCount() {
                return arrayList.size();
            }

            class ViewHolder extends RecyclerView.ViewHolder {

                TextView txtPackName , txtPackPrice, txtstartTime;
                Button btnQuizStart;

                ViewHolder(@NonNull View itemView) {
                    super(itemView);

                    txtPackName = itemView.findViewById(R.id.txtPackName);
                    txtPackPrice = itemView.findViewById(R.id.txtPackPrice);
                    txtstartTime = itemView.findViewById(R.id.txtstartTime);
                    btnQuizStart = itemView.findViewById(R.id.btnQuizStart);
                }
            }
        }

    }
