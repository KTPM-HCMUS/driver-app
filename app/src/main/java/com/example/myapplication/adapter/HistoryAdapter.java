package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.BookingItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>{
    private List<BookingItem> list;
    public IClickItemListener iClickItemListener;
    public HistoryAdapter(List<BookingItem> mList, IClickItemListener listener){
        this.list = mList;
        this.iClickItemListener = listener;
    }

    public interface IClickItemListener{
        void onClickItem(BookingItem bookingItem);
    }
    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history,parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        BookingItem itemHistory = list.get(position);
        if(itemHistory == null){
            return;
        }
        Date date = new Date(itemHistory.getTimeStamp());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy' 'HH:mm:ss");
        holder.price.setText("Tổng tiền: " + itemHistory.getPrice());
        holder.address.setText(itemHistory.getAddressDepart());
        holder.time.setText(simpleDateFormat.format(date));
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickItemListener.onClickItem(itemHistory);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(list != null){
            return list.size();
        }
        return 0;
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder{
        private TextView address, price, time;
        private RelativeLayout layout;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            address = itemView.findViewById(R.id.address_history);
            price = itemView.findViewById(R.id.price_history);
            time = itemView.findViewById(R.id.time_history);
            layout = itemView.findViewById(R.id.item_history);

        }
    }
}
