package com.ezyedu.vendor.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ezyedu.vendor.R;
import com.ezyedu.vendor.model.Chat_New;
import com.ezyedu.vendor.model.GetChat;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;

import java.util.ArrayList;
import java.util.List;

public class GetChatAdapter extends RecyclerView.Adapter<GetChatAdapter.GetChatHolder> {
    private Context context;
    private List<Chat_New> chat_newList = new ArrayList<>();
    String vendors_user_id = null;
    String session_id = null;
    String img_url_base;

    public GetChatAdapter(Context context, List<Chat_New> chat_newList) {
        this.context = context;
        this.chat_newList = chat_newList;

    }

    @NonNull
    @Override
    public GetChatAdapter.GetChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.get_chat_adapter, parent, false);
        return new GetChatHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GetChatAdapter.GetChatHolder holder, int position)
    {
        try {
            Chat_New a = chat_newList.get(position);
            String sender_id = String.valueOf(a.getSender_id());
            String receiver_id = String.valueOf(a.getReceiver_id());
            String message = a.getContent();
            Log.i("messageContent",message);

            if (sender_id.equals(vendors_user_id))
            {
                holder.sendermessage.setText(a.getContent());
                holder.receivermessage.setVisibility(View.GONE);
            }
            else if (receiver_id.equals(vendors_user_id)){
                holder.receivermessage.setText(a.getContent());
                holder.sendermessage.setVisibility(View.GONE);
            }

        }
        catch (Exception e)
        {
            Log.i("CatchMissData",e.toString());
        }

    }

    @Override
    public int getItemCount() {
        return chat_newList == null ?0: chat_newList.size();
    }

    public class GetChatHolder extends RecyclerView.ViewHolder {
        TextView sendermessage,receivermessage;

        //retrive base url
        Globals sharedData = Globals.getInstance();
        String base_app_url;

        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();

        public GetChatHolder(@NonNull View itemView) {
            super(itemView);

            sendermessage = itemView.findViewById(R.id.sender_message);
            receivermessage = itemView.findViewById(R.id.reciver_msg);
            SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences("vendor_login", Context.MODE_PRIVATE);
            vendors_user_id = sharedPreferences.getString("vendor_user_id","");
            Log.i("vendor_user_id_val",vendors_user_id);


            //get domain url
            base_app_url = sharedData.getValue();
            Log.i("feeds_domain_url",base_app_url);

            //get image loading url
            img_url_base = shareData1.getIValue();
            Log.i("img_url_global",img_url_base);
        }
    }
}
