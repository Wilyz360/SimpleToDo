package com.example.simpletoddo;

import android.support.annotation.NonNull;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


//Creating new adapter
// Responsible displaying data from the model into a row in the recycle view
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>{

    // Item adapter interface that mainActivity can implement it. For passing information
    // from main activity to the adapter
    public interface onClickListener {
        void onItemClicked(int position); // defying position of item clicked
    }
    public interface onLongClickListener {
        void onItemLongClicked(int position);   // For notifying the position where the long click was presses
    }

    List<String> items;
    onLongClickListener longClickListener;
    onClickListener clickListener;

    public ItemAdapter(List<String> items, onLongClickListener longClickListener, onClickListener clickListener) {       // Constructor
        this.items = items;
        this.longClickListener = longClickListener;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    // Responsible for creating each view
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        // Use layout inflator and inflate a view

        View todoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);

        // wrap it inside a View Holder and return it
        return new ViewHolder(todoView);
    }

    @Override
    // Responsible for taking data of a particular position and putting into the view holder
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Grab the item at the position
        String item = items.get(position);
        // Bind the item into the specified view holder
        holder.bind(item);
    }

    @Override
    // Number of items available in the data
    public int getItemCount() {
        return items.size();
    }

    // Container that provide easy access to the views that represent each row of the List
    class ViewHolder extends RecyclerView.ViewHolder{   // Need to provide a constructor
       TextView tvItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(android.R.id.text1);
        }

        // Update the view inside of the view holder with this data
        public void bind(String item) {

            tvItem.setText(item);
            // Attach clickListener on the text View Item
            tvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { // onClick invoke method on interface
                    clickListener.onItemClicked(getAdapterPosition());
                }
            });
            tvItem.setOnLongClickListener(  new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // Notify the listener which position was long pressed
                    longClickListener.onItemLongClicked(getAdapterPosition());
                    return true;
                }
            });

        }
    }
}
