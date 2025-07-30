package com.zybooks.mobile2app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/* *********************************************************************************************
 * TODO: This class defines the adapter for the RecyclerView in the activity_database.xml layout
 * *********************************************************************************************/
public class TableAdapter extends RecyclerView.Adapter<TableAdapter.TableViewHolder> {

    private List<TableRowData> dataList;
    private OnDeleteClickListener deleteClickListener;

    // Constructor - takes in a list of TableRowData objects (used to populate the RecyclerView)
    public TableAdapter(List<TableRowData> dataList) {
        this.dataList = dataList;
    }

    // *********************************************************************************************
    /* TODO: Constructor - takes in a list of TableRowData objects and an OnDeleteClickListener
     **********************************************************************************************/
    public TableAdapter(List<TableRowData> dataList, OnDeleteClickListener listener) {
        this.dataList = dataList;
        this.deleteClickListener = listener;
    }


    // *********************************************************************************************
    /* TODO: This class defines the ViewHolder for each row in the RecyclerView
     **********************************************************************************************/
    public static class TableViewHolder extends RecyclerView.ViewHolder {
        TextView col2, col3, col4;
        ImageView deleteItem;

        // *********************************************************************************************
        /* TODO: Read the text view ids from the table_row_item.xml layout
        // and set them to the corresponding variables. Columns 1 and 5 are kept empty for image rendering
         **********************************************************************************************/
        public TableViewHolder(View itemView) {
            super(itemView);
            col2 = itemView.findViewById(R.id.col2);
            col3 = itemView.findViewById(R.id.col3);
            col4 = itemView.findViewById(R.id.col4);
            // Find the delete button in the table_row_item.xml layout
            deleteItem = itemView.findViewById(R.id.delete_item);
        }
    }


    /* *********************************************************************************************
     * Todo: This Interface will be used for handling delete button clicks coming from each row in
     * the RecyclerView (table_row_item.xml) through a click listener in the activity_database.xml
     * ********************************************************************************************/
    public interface OnDeleteClickListener {
        void onDeleteClick(int position, TableRowData rowData);
    }

    /* *********************************************************************************************
     * Todo: Create a new ViewHolder for each row in the RecyclerView and inflate
     *  the table_row_item.xml layout
     * ********************************************************************************************/
    @Override
    public TableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.table_row_item, parent, false);
        return new TableViewHolder(view);
    }

    /* *********************************************************************************************
     * Todo: Bind the data from the TableRowData object to the corresponding text views in the ViewHolder
     * ********************************************************************************************/
    @Override
    public void onBindViewHolder(TableViewHolder holder, int position) {
        TableRowData row = dataList.get(position);
        holder.col2.setText(row.getColumn2());
        holder.col3.setText(row.getColumn3());
        holder.col4.setText(String.valueOf(row.getColumn4()));

        // Add delete button logic for each row in the RecyclerView
        holder.deleteItem.setOnClickListener(v -> {
            if (deleteClickListener != null) {
                deleteClickListener.onDeleteClick(holder.getAdapterPosition(), row);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
