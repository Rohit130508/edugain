package com.edugainnow.edugain.ui.dashboard;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import me.relex.circleindicator.CircleIndicator;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.edugainnow.edugain.R;
import com.edugainnow.edugain.profile.MainProfile;
import com.edugainnow.edugain.ui.home.activity.NewRegistration;
import com.edugainnow.edugain.ui.home.activity.TodayPackageReg;
import com.edugainnow.edugain.util.Apis;
import com.edugainnow.edugain.util.CustomPerference;
import com.edugainnow.edugain.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    ViewPager vp_slider;
    int images_vp[] = {R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d,R.drawable.e,R.drawable.f, R.drawable.g};
    SliderPagerAdapter myCustomPagerAdapter;

    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<ImageModel> imageModelArrayList;
    private RecyclerView rec_fooding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        final TextView textView = root.findViewById(R.id.txtNewRegistration);
        final TextView textRegPack = root.findViewById(R.id.textRegPack);
        final TextView textAllRegistration = root.findViewById(R.id.textAllRegistration);
        root.findViewById(R.id.share).setOnClickListener(view -> shareApp());

        textRegPack.setOnClickListener(v -> startActivity(new Intent(getActivity(), TodayPackageReg.class)));
        textView.setOnClickListener(v -> startActivity(new Intent(getActivity(), NewRegistration.class)));
        textAllRegistration.setOnClickListener(v -> startActivity(new Intent(getActivity(), NewRegistration.class)));


        root.findViewById(R.id.ivNotification).setOnClickListener(v ->
                startActivity(new Intent(getActivity(), NotificationActivity.class)));

        rec_fooding =  root.findViewById(R.id.rec_fooding);
//        getServicesDashboard();

        imageModelArrayList = new ArrayList<>();
        imageModelArrayList = populateList();

        vp_slider = root.findViewById(R.id.vp_slider);



        myCustomPagerAdapter = new SliderPagerAdapter(getActivity(), images_vp);
        vp_slider.setAdapter(myCustomPagerAdapter);

        CircleIndicator indicator =  root.findViewById(R.id.indicator);
        indicator.setViewPager(vp_slider);

        NUM_PAGES = imageModelArrayList.size();
        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = () -> {
            if (currentPage == NUM_PAGES) {
                currentPage = 0;

            }

            vp_slider.setCurrentItem(currentPage++, true);
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2000, 2000);





        return root;
    }

    private ArrayList<ImageModel> populateList(){

        ArrayList<ImageModel> list = new ArrayList<>();

        for(int i = 0; i < images_vp.length; i++){
            ImageModel imageModel = new ImageModel();
            imageModel.setImage_drawable(images_vp[i]);
            list.add(imageModel);
        }

        return list;
    }

    public class ImageModel {

        private int image_drawable;

        public int getImage_drawable() {
            return image_drawable;
        }

        public void setImage_drawable(int image_drawable) {
            this.image_drawable = image_drawable;
        }
    }


    class SliderPagerAdapter extends PagerAdapter {
        Context context;
        int[] images;
        LayoutInflater layoutInflater;


        public SliderPagerAdapter(Context context, int[] images) {
            this.context = context;
            this.images = images;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = layoutInflater.inflate(R.layout.vp_slider_item, container, false);

            ImageView iv_vp_slider = itemView.findViewById(R.id.iv_vp_slider);
            iv_vp_slider.setImageResource(images[position]);

            container.addView(itemView);

            //listening to image click
            iv_vp_slider.setOnClickListener(v -> {
                //Toast.makeText(context, "you clicked image " + (position + 1), Toast.LENGTH_LONG).show();
            });

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }



    private void getServicesDashboard()
    {
        Utils.customProgress(getActivity(),"Please Wait...");

        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, Apis.GetAllInrList, null,
                response -> {
                    try {

                        System.out.println("Response===" + response);
                        Utils.customProgressStop();
                        if (response.getBoolean("Status")) {

                            JSONArray jsonArray = response.getJSONArray("Data");

                            Rec_Adapter adapter = new Rec_Adapter(jsonArray);
                            rec_fooding.setLayoutManager
                                    (new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,
                                            false));
                            rec_fooding.setAdapter(adapter);

                        } else {

                            Toast.makeText(getActivity(), response.getString("Message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Utils.customProgressStop();

                    }


                }, error ->                         Utils.customProgressStop()
);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        int socketTimeout = 20000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        requestQueue.add(jsonObjectRequest);

    }


    public class Rec_Adapter extends RecyclerView.Adapter<Rec_Adapter.ViewHolder>
    {
        JSONArray jsonArray ;

        Rec_Adapter(JSONArray jsonArray)
        {
            this.jsonArray = jsonArray;
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View layoutInflater = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.dashboard_item,parent,false);
            return new ViewHolder(layoutInflater);
        }
        @Override
        public void onBindViewHolder(ViewHolder holder, int position)
        {

            try {
                final JSONObject object = jsonArray.getJSONObject(position);

                final String catName = object.getString("CategoryName");
                final String catImg = object.getString("CategoryImage");

                holder.txt_servicesName.setText(catName);

                Utils.Picasso(catImg,holder.linImage);

//                holder.linImage.setOnClickListener(v -> {
//                    startActivity(new Intent(getActivity(), InrList.class)
//                            .putExtra("catName",catName)
//                            .putExtra("catImg",catImg));
//                    try {
//                        array = object.getJSONArray("tz");
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        @Override
        public int getItemCount() {
            return jsonArray.length();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private TextView txt_servicesName;
            private ImageView linImage;


            public ViewHolder(View itemView) {
                super(itemView);


                txt_servicesName = itemView.findViewById(R.id.txt_servicesName);
                linImage = itemView.findViewById(R.id.linImage);

            }
        }
    }


    void shareApp()
    {
//            String RefLink = sharedpreferences.getString("ReferralCode",null);
        String RefLink = CustomPerference.getString(getActivity(),CustomPerference.USER_REFERALCODE);

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "https://play.google.com/store/apps/details?id="+getActivity().getPackageName() +"&referrer=" +RefLink;
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }


}
