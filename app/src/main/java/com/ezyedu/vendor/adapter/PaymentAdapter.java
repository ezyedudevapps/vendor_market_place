package com.ezyedu.vendor.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ezyedu.vendor.Login_page;
import com.ezyedu.vendor.R;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;
import com.ezyedu.vendor.model.Payment;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentHolder> {
    private Context context;
    private List<Payment> paymentList = new ArrayList<>();
    String img_url_base;

    public PaymentAdapter(Context context, List<Payment> paymentList) {
        this.context = context;
        this.paymentList = paymentList;
    }

    @NonNull
    @Override
    public PaymentAdapter.PaymentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_adapter,parent,false);
        return  new PaymentHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PaymentAdapter.PaymentHolder holder, int position)
    {
        Payment p = paymentList.get(position);
        holder.order_id.setText("Order ID : "+p.getOrder_id());
        holder.date.setText("Date : "+p.getDate());

        Double amount = p.getAmount();


        String str = NumberFormat.getNumberInstance(Locale.US).format(amount);
        holder.amount.setText("Amount : "+"Rp "+str);

    }

    @Override
    public int getItemCount() {
        return paymentList == null ?0: paymentList.size();
    }

    public class PaymentHolder extends RecyclerView.ViewHolder {
        TextView order_id,amount,date;

        //retrive base url
        Globals sharedData = Globals.getInstance();
        String base_app_url;

        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();

        public PaymentHolder(@NonNull View itemView) {
            super(itemView);
            order_id = itemView.findViewById(R.id.order_payment_id);
            amount = itemView.findViewById(R.id.amount);
            date = itemView.findViewById(R.id.payment_date);

            //get domain url
            base_app_url = sharedData.getValue();
            Log.i("feeds_domain_url",base_app_url);

            //get image loading url
            img_url_base = shareData1.getIValue();
            Log.i("img_url_global",img_url_base);
        }
    }
}
