package net.smitpatel.cricketscore;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<ListItem> listItems;
    private Context context;
    private String user;
    public MyAdapter(List<ListItem> listItems, Context context, String user) {
        this.listItems = listItems;
        this.context = context;
        this.user = user;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                                .inflate(R.layout.list_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final ListItem listItem = this.listItems.get(i);
        viewHolder.textViewHead.setText(listItem.getHeader());
        viewHolder.textViewDescription.setText(listItem.getDescription());
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.equals("MainActivity")) {
                    matchInfo(listItem);
                }
            }
        });
        viewHolder.textViewDetails.setText(listItem.getDetails());
    }
    public void matchInfo(ListItem listItem){
        Intent intent = new Intent(context, matchInfo.class);
        intent.putExtra("matchId", listItem.getMatchId());
        context.startActivity(intent);
    }
    @Override
    public int getItemCount() {
        return this.listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewHead;
        public TextView textViewDescription;
        public LinearLayout linearLayout;
        public TextView textViewDetails;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewHead = (TextView) itemView.findViewById(R.id.textViewHead);
            textViewDescription = (TextView) itemView.findViewById(R.id.textViewDescription);
            textViewDetails = itemView.findViewById(R.id.textViewDetails);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
        }
    }
}
