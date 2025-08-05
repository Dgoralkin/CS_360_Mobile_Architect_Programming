package com.zybooks.mobile2app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private final List<TableRowData> dataList;
    private final DatabaseHelper dbHelper;
    private final Context context;

    public NotificationAdapter(Context context, List<TableRowData> dataList, DatabaseHelper dbHelper) {
        this.context = context;
        this.dataList = dataList;
        this.dbHelper = dbHelper;
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        EditText quantityEditViewText;
        Button incrementBtn, decrementBtn;

        // Constructor
        public NotificationViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image);
            nameTextView = itemView.findViewById(R.id.Notification_item_name);
            quantityEditViewText = itemView.findViewById(R.id.Notification_QTY_view);
            incrementBtn = itemView.findViewById(R.id.Notification_btn_increment);
            decrementBtn = itemView.findViewById(R.id.Notification_btn_decrement);
        }
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.table_row_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {
        TableRowData item = dataList.get(position);

        // Load image or fallback
        if (item.getColumn1() != null && !item.getColumn1().isEmpty()) {
            File imgFile = new File(item.getColumn1());
            if (imgFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                holder.imageView.setImageBitmap(bitmap);
            } else {
                holder.imageView.setImageResource(R.drawable.product_image);
            }
        } else {
            holder.imageView.setImageResource(R.drawable.product_image);
        }

        // Set SKU and Name
        holder.nameTextView.setText(item.getColumn3());

        // Set quantity text field
        holder.quantityEditViewText.setText(String.valueOf(item.getColumn4()));

        // Disable direct editing, or alternatively, you can allow it
        holder.quantityEditViewText.setEnabled(true);

        // Increment button logic
        holder.incrementBtn.setOnClickListener(v -> {
            int currentQty = item.getColumn4();
            int newQty = currentQty + 1;
            updateQuantity(item, newQty, holder.getAdapterPosition());
        });

        // Decrement button logic
        holder.decrementBtn.setOnClickListener(v -> {
            int currentQty = item.getColumn4();
            if (currentQty > 0) {
                int newQty = currentQty - 1;
                updateQuantity(item, newQty, holder.getAdapterPosition());
            } else {
                Toast.makeText(context, "Quantity cannot be less than 0", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateQuantity(TableRowData item, int newQuantity, int position) {
        item.setColumn4(newQuantity);
        notifyItemChanged(position);

        // Update database
        boolean success = dbHelper.updateItemQuantity(item.getColumn2(), newQuantity);
        if (!success) {
            Toast.makeText(context, "Failed to update database", Toast.LENGTH_SHORT).show();
            Log.e("NotificationAdapter", "DB update failed for SKU: " + item.getColumn2());
        } else {
            Toast.makeText(context, "Quantity updated", Toast.LENGTH_SHORT).show();
            Log.d("NotificationAdapter", "Updated quantity for SKU: " + item.getColumn2() + " to " + newQuantity);

            int minQuantity = dbHelper.getMinQuantity(item.getColumn2());
            if (newQuantity < minQuantity) {
                Log.d("NotificationAdapter", "Quantity below minimum, sending notification");
                NotificationHelper.sendLowStockNotification(context, item.getColumn3(), newQuantity);
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    // Optional: add method to update list and refresh
    public void setData(List<TableRowData> newData) {
        dataList.clear();
        dataList.addAll(newData);
        notifyDataSetChanged();
    }
}
