package com.edugainnow.edugain.ui.dashboard;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DashboardPackageQues extends AppCompatActivity {

    private String quesId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_AppCompat_DayNight_NoActionBar);
        setContentView(R.layout.activity_dashboard_package_ques);

        initView();



        getExecuteMethods();
        
    }

    RecyclerView rvNewRegList;
    TextView txtNorecords,
            txtCancel,
            txtTimer;
    private String selectedOption = null;
    ArrayList<DashPackQModel> arrayList = new ArrayList<>();

    void initView()
    {

        txtTimer = findViewById(R.id.txtTimer);
        txtCancel = findViewById(R.id.txtCancel);
        txtCancel.setText(getIntent().getStringExtra("packName"));
        txtCancel.setOnClickListener(v -> finish());
        txtNorecords = findViewById(R.id.txtNorecords);
        rvNewRegList = findViewById(R.id.rvNewRegList);
        rvNewRegList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }
    void getExecuteMethods()
    {
        if(Utils.isNetworkAvailable(this))
            executeTodayRegPackID();
        else
            Utils.CustomAlert(this,R.string.nointernet_title,R.string.nointernet_message);
    }
    void executeTodayRegPackID()
    {


        JSONObject object = new JSONObject();
        try {
            object.put("RegID",
                    CustomPerference.getString(DashboardPackageQues.this,CustomPerference.USER_ID
                    ));

            object.put("PackageID",
                    /*getIntent().getStringExtra("packageId")*/"1");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("Request"+object);

        Utils.customProgress(this,"Please Wait ...");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Apis.todaywithPackageidQuestion, object,
                response -> {

                    Utils.customProgressStop();
                    System.out.println("Responce"+response);
                    JSONArray array = null;
                    try {
                        array = response.getJSONArray("Data");

                        if(array.isNull(0))
                        {
                            txtNorecords.setVisibility(View.VISIBLE);
                            txtNorecords.setText("NO recods found");
                        }else {
                            txtNorecords.setVisibility(View.GONE);

                            for (int i = 0; i < array.length(); i++) {
                                try {

                                    JSONObject jsonObject = array.getJSONObject(i);


                                    quesId = jsonObject.getString("Qid");
                                    DashPackQModel model = new DashPackQModel();
                                    model.setQuestionEnglish(jsonObject.getString("QuestionEnglish"));
                                    model.setOption1(jsonObject.getString("Option1"));
                                    model.setOption2(jsonObject.getString("Option2"));
                                    model.setOption3(jsonObject.getString("Option3"));
                                    model.setOption4(jsonObject.getString("Option4"));
                                    model.setQid(jsonObject.getString("Qid"));
                                    model.setSubject(jsonObject.getString("Subject"));

                                    arrayList.add(model);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    System.out.println("Error"+e.getMessage());
                                }

                                TodayPackageAdapter adapter = new TodayPackageAdapter(arrayList);
                                rvNewRegList.setAdapter(adapter);
                                new CountDownTimer(15000, 1000) {

                                    public void onTick(long millisUntilFinished) {
                                        txtTimer.setText(":" + millisUntilFinished / 1000);

                                    }

                                    public void onFinish() {
                                        executeSaveNext(quesId);
                                    }
                                }.start();

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {

            Utils.customProgressStop();
            System.out.println("Error"+error.getMessage());
        });

        RequestQueue queue = Volley.newRequestQueue(DashboardPackageQues.this);
        request.setRetryPolicy(new DefaultRetryPolicy(20*1000, 2,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);

    }

    class TodayPackageAdapter extends RecyclerView.Adapter<TodayPackageAdapter.ViewHolder>
    {

        ArrayList<DashPackQModel> arrayList;

        TodayPackageAdapter(ArrayList<DashPackQModel> arrayList) {
            this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_todaypack_questio, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            DashPackQModel model = arrayList.get(position);
            holder.txtSubject.setText("Subject is     --->  "
                    +model.getSubject());
            holder.txtquestion.setText(model.getQuestionEnglish());
            holder.rbtnOPT1.setText(model.getOption1());
            holder.rbtnOPT2.setText(model.getOption2());
            holder.rbtnOPT3.setText(model.getOption3());
            holder.rbtnOPT4.setText(model.getOption4());

            holder.rgroup.setOnCheckedChangeListener((group, checkedId) -> {
               if(checkedId== R.id.rbtnOPT1)
                   selectedOption = model.getOption1();
               else if(checkedId== R.id.rbtnOPT2)
                   selectedOption = model.getOption2();
               else if(checkedId== R.id.rbtnOPT3)
                   selectedOption = model.getOption3();
               else if(checkedId== R.id.rbtnOPT4)
                   selectedOption = model.getOption4();

           });
           holder.btnSave.setOnClickListener(v -> {
               executeSaveNext(model.getQid());

           });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView txtquestion,
                    txtSubject;
            Button btnSave;
            RadioGroup rgroup;
            RadioButton rbtnOPT1,
                    rbtnOPT2,
                    rbtnOPT3,
                    rbtnOPT4;

            ViewHolder(@NonNull View itemView) {
                super(itemView);

                btnSave = itemView.findViewById(R.id.btnSave);

                txtSubject = itemView.findViewById(R.id.txtSubject);
                txtquestion = itemView.findViewById(R.id.txtquestion);
                rgroup = itemView.findViewById(R.id.rgroup);
                rbtnOPT1 = itemView.findViewById(R.id.rbtnOPT1);
                rbtnOPT2 = itemView.findViewById(R.id.rbtnOPT2);
                rbtnOPT3 = itemView.findViewById(R.id.rbtnOPT3);
                rbtnOPT4 = itemView.findViewById(R.id.rbtnOPT4);
            }
        }
    }
    
    void executeSaveNext(String questionId)
    {
        String time = "00:00"+txtTimer.getText().toString();

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("Reg_No",CustomPerference.getString(DashboardPackageQues.this,
                    CustomPerference.USER_ID));
            jsonObject.put("Qid",questionId);
            jsonObject.put("ChooseOption",selectedOption);
            jsonObject.put("Category",getIntent().getStringExtra("packName"));
            jsonObject.put("PackageID",getIntent().getStringExtra("packageId"));
            jsonObject.put("TakeTime","00:01:00");

        }catch (Exception e)
        {}

        System.out.println("Request"+jsonObject);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Apis.QuizeSave, jsonObject,
                response -> {

                    System.out.println("Responce"+response);
            arrayList.clear();
            executeTodayRegPackID();

                }, error -> {

                });

        RequestQueue queue = Volley.newRequestQueue(DashboardPackageQues.this);
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }
    
}
