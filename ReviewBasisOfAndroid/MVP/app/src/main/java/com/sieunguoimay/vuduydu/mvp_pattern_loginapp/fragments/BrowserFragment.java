package com.sieunguoimay.vuduydu.mvp_pattern_loginapp.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.sieunguoimay.vuduydu.mvp_pattern_loginapp.R;
import com.sieunguoimay.vuduydu.mvp_pattern_loginapp.models.ReqresJsonUser;
import com.sieunguoimay.vuduydu.mvp_pattern_loginapp.screens.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class BrowserFragment extends Fragment {
    private TextView tvUrl;
    private ListView lvReqresUsers;
    private UserListAdapter userListAdapter;
    private List<ReqresJsonUser> userList = new ArrayList<>();
    private String url = "https://reqres.in/api/users?page=";
    private OkHttpClient client;
    public BrowserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_browser, container, false);
        tvUrl= v.findViewById(R.id.tvUrl);
        lvReqresUsers = v.findViewById(R.id.lvReqresUsers);
        userListAdapter = new UserListAdapter(getContext(),R.layout.row,userList);
        lvReqresUsers.setAdapter(userListAdapter);


        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getContext()).build();
        ImageLoader.getInstance().init(config);

        client = new OkHttpClient();
        sendRequest(0);

        setHasOptionsMenu(true);

        return v;
    }

    private int currentPage = 0;
    private int maxPage = 0;
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_browser,menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.it_next){
            currentPage++;
            currentPage%=maxPage;
        }else if(item.getItemId()==R.id.it_prev){
            if(currentPage<0)currentPage+=maxPage;
        }else if(item.getItemId()==R.id.it_refresh){
        }
        sendRequest(currentPage);

        return true;
    }

    private void sendRequest(int pageNumber){
        String currentUrl = url+pageNumber;
        tvUrl.setText(currentUrl);

        Request request = new Request.Builder().url(currentUrl).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    final String responsString = response.body().string();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            processJson(responsString);
                        }
                    });
                }
            }
        });

    }
    private void processJson(String json){
        try {
            JSONObject parentObject = new JSONObject(json);
            JSONArray parentArray = parentObject.getJSONArray("data");
            maxPage = parentObject.getInt("total_pages");
            userList.clear();
            for(int i =0; i<parentArray.length(); i++){
                JSONObject finalObject = parentArray.getJSONObject(i);
                ReqresJsonUser user = new ReqresJsonUser();

                user.setId(finalObject.getInt("id"));
                user.setFirstName(finalObject.getString("first_name"));
                user.setLastName(finalObject.getString("last_name"));
                user.setEmail(finalObject.getString("email"));
                user.setAvatarUrl(finalObject.getString("avatar"));
                userList.add(user);
            }
            userListAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public class UserListAdapter extends ArrayAdapter{

        private List<ReqresJsonUser>userList;
        private int resource;
        private LayoutInflater layoutInflater;
        public UserListAdapter(Context context,int resource, List<ReqresJsonUser>objects){
            super(context,resource, objects);
            this.userList = objects;
            this.resource =resource;
            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            if(convertView==null){
                convertView = layoutInflater.inflate(resource, null);
            }
            ImageView ivAvatar = convertView.findViewById(R.id.ivAvatar);
            TextView tvUserName = convertView.findViewById(R.id.tvUserName);
            TextView tvEmail = convertView.findViewById(R.id.tvEmail);


            tvUserName.setText(userList.get(position).getFirstName()+" "+userList.get(position).getLastName());
            tvEmail.setText(userList.get(position).getEmail());
            ImageLoader.getInstance().displayImage(userList.get(position).getAvatarUrl(),ivAvatar);
            return convertView;
        }
    }

}

