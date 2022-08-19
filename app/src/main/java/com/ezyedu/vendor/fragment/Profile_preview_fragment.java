package com.ezyedu.vendor.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.ezyedu.vendor.Preview_Page;
import com.ezyedu.vendor.R;
import com.ezyedu.vendor.Splash_Screen;
import com.ezyedu.vendor.adapter.PagePreviewAdapter;
import com.ezyedu.vendor.adapter.PendingOrderAdapter;
import com.ezyedu.vendor.adapter.VendorFacilityAdapter;
import com.ezyedu.vendor.adapter.VendorInfo1Adapter;
import com.ezyedu.vendor.adapter.VendorInfoAdapter;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;
import com.ezyedu.vendor.model.ProfilePreview;
import com.ezyedu.vendor.model.VendorFacilities;
import com.ezyedu.vendor.model.VendorInfo;
import com.ezyedu.vendor.model.VendorInfo1;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Profile_preview_fragment extends Fragment implements OnMapReadyCallback{
    SharedPreferences ven_id_shared;
    public String ven_id;
    RecyclerView recyclerView,recyclerView1,recyclerView2;
    VendorInfoAdapter vendorInfoAdapter;
    VendorInfo1Adapter vendorInfo1Adapter;
    VendorFacilityAdapter vendorFacilityAdapter;
    private RequestQueue requestQueue;
    private List<VendorInfo> vendorInfoList = new ArrayList<>();
    private List<VendorInfo1> vendorInfo1List = new ArrayList<>();
    private List<VendorFacilities> vendorFacilitiesList = new ArrayList<>();

    private String tag = "nullmain";
    TextView textView;

    private GoogleMap mMap;

    String ven_detail_url;


    ProgressDialog progressDialog;
    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

    String instagram,facebook,twitter,youtube,tiktok;

    Button relativeLayout;

    SharedPreferences sharedPreferences;
    String session_id = null;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        sharedPreferences = getContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("Session_Histry_activity",session_id);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile_preview_fragment, container, false);
        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

        requestQueue= Volley.newRequestQueue(getContext());
        recyclerView = view.findViewById(R.id.frag_insti1_recyc);
        vendorInfoAdapter = new VendorInfoAdapter(getContext(),vendorInfoList);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(vendorInfoAdapter);

        recyclerView1 = view.findViewById(R.id.ven_cat_desc_recyc);
        vendorInfo1Adapter = new VendorInfo1Adapter(getContext(),vendorInfo1List);
        LinearLayoutManager manager1 = new LinearLayoutManager(getContext());
        recyclerView1.setLayoutManager(manager1);
        recyclerView1.setAdapter(vendorInfo1Adapter);

        textView = view.findViewById(R.id.text_facility);
        recyclerView2 = view.findViewById(R.id.ven_facility_recyc);
        vendorFacilityAdapter = new VendorFacilityAdapter(getContext(),vendorFacilitiesList);
        LinearLayoutManager manager2 = new LinearLayoutManager(getContext());
        recyclerView2.setLayoutManager(manager2);
        recyclerView2.setAdapter(vendorFacilityAdapter);

        SupportMapFragment mMapFragment = SupportMapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.google_map, mMapFragment);
        fragmentTransaction.commit();
        mMapFragment.getMapAsync( this);

        requestQueue= Volley.newRequestQueue(getContext());


        relativeLayout = view.findViewById(R.id.opn_map);

        return view;
    }

/*
    private void fetchData()
    {
        String url = base_app_url+"api/vendor/info";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                progressDialog.dismiss();
                Log.i("ResponseFrag",response.toString());
                try {
                    String message = response.getString("message");
                    if (message.equals("success"))
                    {
                        JSONObject jsonObject = response.getJSONObject("data");
                        String logo = jsonObject.getString("logo");;
                        String inst_name = jsonObject.getString("inst_name");
                        Double rating = jsonObject.getDouble("rating");
                        Log.i("NameInsti",inst_name);

                        //instagram
                        JSONArray jsonArray = jsonObject.getJSONArray("social_medias");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String instagram = jsonObject1.getString("url");
                        //fb
                        JSONObject jsonObject2 = jsonArray.getJSONObject(1);
                        String facebook = jsonObject2.getString("url");
                        //linkedin
                        JSONObject jsonObject3 = jsonArray.getJSONObject(2);
                        String linkedin = jsonObject3.getString("url");
                        //twitter
                        JSONObject jsonObject4 = jsonArray.getJSONObject(3);
                        String twitter = jsonObject4.getString("url");
                        //youtube
                        JSONObject jsonObject5 = jsonArray.getJSONObject(4);
                        String youtube = jsonObject5.getString("url");
                        //tiktok
                        JSONObject jsonObject6 = jsonArray.getJSONObject(5);
                        String tiktok = jsonObject6.getString("url");

                        JSONObject jsonObject7 = jsonArray.getJSONObject(6);
                        String aaa = jsonObject7.getString("url");

                        JSONObject jsonObject8 = jsonArray.getJSONObject(7);
                        String eee = jsonObject8.getString("url");

                        JSONArray jsonArray1 = jsonObject.getJSONArray("operatings");
                        JSONObject jsonObject9 = jsonArray1.getJSONObject(0);
                        String open = jsonObject9.getString("open");
                        String close = jsonObject9.getString("close");
                        int monday = jsonObject9.getInt("monday");
                        int tuesday = jsonObject9.getInt("tuesday");
                        int wednesday = jsonObject9.getInt("wednesday");
                        int thursday = jsonObject9.getInt("thursday");
                        int friday = jsonObject9.getInt("friday");
                        int saturday = jsonObject9.getInt("saturday");
                        int sunday = jsonObject9.getInt("sunday");
                        String website = jsonObject.getString("website");
                        String email = jsonObject.getString("email");
                        String address = jsonObject.getString("address");

                        ProfilePreview post = new ProfilePreview(logo,inst_name,instagram,facebook,twitter,youtube,tiktok,open,close,
                                website,email,address,rating,monday,tuesday,wednesday,thursday,friday,saturday,sunday
                        );
                        profilePreviewList.add(post);
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("errorsos",e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
Log.i("errorFrag",error.toString());
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/json");
                params.put("Authorization",session_id);
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }


 */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        String url = base_app_url+"api/vendor/info";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                progressDialog.dismiss();
                Log.i("venbasicdtl",response.toString());
                try {
                    String message = response.getString("message");
                    JSONObject js = response.getJSONObject("data");
                    Integer vendor_chat_id = js.getInt("vendor_id");
               //     Integer is_chatting_allowed = response.getInt("is_chatting_allowed");
                //    String vendor_hash_id = response.getString("vendor_hash_id");
                    String vendor_name =js.getString("inst_name");
                    String vendor_address =js.getString("address");
                    String vendor_logo =js.getString("logo");
                    String vendor_email=js.getString("email");
                //    String vendor_phone=response.getString("vendor_phone");
                    String website=js.getString("website");
                 //   String vendor_image=response.getString("vendor_image");
                    String vendor_rating=js.getString("rating");
                    JSONArray jsonArray = js.getJSONArray("operatings");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String opt = jsonObject.getString("open")+" - "+jsonObject.getString("close");

                    String latitude = js.getString("latitude");
                    String longitude = js.getString("longitude");


                    JSONArray jsonArray1 = js.getJSONArray("social_medias");

                    for (int i = 0;i<jsonArray1.length();i++)
                    {
                        JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                        int id = jsonObject1.getInt("id");
                        if (id == 1)
                        {
                            facebook = jsonObject1.getString("url");
                        }
                        else if (id == 4)
                        {
                            instagram = jsonObject1.getString("url");
                        }
                        else if (id == 6)
                        {
                            youtube = jsonObject1.getString("url");
                        }
                        else if (id == 7)
                        {
                            twitter = jsonObject1.getString("url");
                        }
                        else if (id == 9 || id == 10)
                        {
                            tiktok = jsonObject1.getString("url");
                        }

                    }

                    JSONArray jsonArray2 = js.getJSONArray("operatings");
                    JSONObject jsonObject9 = jsonArray2.getJSONObject(0);
                    int monday = jsonObject9.getInt("monday");
                    int tuesday = jsonObject9.getInt("tuesday");
                    int wednesday = jsonObject9.getInt("wednesday");
                    int thursday = jsonObject9.getInt("thursday");
                    int friday = jsonObject9.getInt("friday");
                    int saturday = jsonObject9.getInt("saturday");
                    int sunday = jsonObject9.getInt("sunday");

                    Double latii = Double.parseDouble(latitude);
                    Double longii = Double.parseDouble(longitude);

                    if (latii != null)
                    {
                        LatLng latLng = new LatLng(latii,longii);
                        mMap = googleMap;
                        mMap.addMarker(new MarkerOptions().position(latLng));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));
                        float zoomLevel = 11.0f;
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoomLevel));
                    }

                    relativeLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (latii!= null && longii != null)
                            {
                                Intent intent1 = new Intent(Intent.ACTION_VIEW);
                                intent1.setData(Uri.parse("geo:"+latii+","+longii));
                                Intent chooser = Intent.createChooser(intent1,"Launch Maps");
                                startActivity(chooser);
                            }
                        }
                    });


                    VendorInfo post = new VendorInfo(vendor_chat_id,"null",vendor_name,vendor_address,vendor_logo,vendor_email,
                            "",website,"",vendor_rating,0,opt,instagram,facebook,twitter,youtube,tiktok,
                            monday,tuesday,wednesday,thursday,friday,saturday,sunday);
                    vendorInfoList.add(post);
                    Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();

                    JSONArray jsonArray3 = js.getJSONArray("category_description_contents");
                    for (int i = 0;i<jsonArray3.length();i++)
                    {
                        JSONObject jsonObject1 = jsonArray3.getJSONObject(i);
                        String titttle = jsonObject1.getString("title");
                        String content = jsonObject1.getString("content");
                        VendorInfo1 post1 = new VendorInfo1(titttle,content);
                        vendorInfo1List.add(post1);
                        recyclerView1.getAdapter().notifyDataSetChanged();
                    }
                    if (js.has("vendor_facilities"))
                    {
                        JSONArray jsonArray4 = response.getJSONArray("vendor_facilities");
                        if (jsonArray4.length() >0)
                        {
                            textView.setText("Facilities");
                        }
                        for (int i = 0; i<jsonArray4.length();i++)
                        {
                            JSONObject jsonObject1 = jsonArray4.getJSONObject(i);
                            String tittle = jsonObject1.getString("title");
                            String image = jsonObject1.getString("image");
                            VendorFacilities post2 = new VendorFacilities(tittle,image);
                            vendorFacilitiesList.add(post2);
                            recyclerView2.getAdapter().notifyDataSetChanged();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/json");
                params.put("Authorization",session_id);
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
}