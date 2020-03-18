package com.pihta24.dolgi;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;

    int cardColor = Color.parseColor("#C0C0C0");
    int textColor = Color.parseColor("#000000");

    private LayoutInflater layoutInflater;
    private MyDatabase myDatabase;

    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<Double> debts = new ArrayList<>();
    private ArrayList<Integer> ids = new ArrayList<>();

    public MyAdapter(Context context){
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        myDatabase = new MyDatabase(context);
    }

    public void update(int cardColor, int textColor){
        names.clear();
        debts.clear();
        ids.clear();

        this.cardColor = cardColor;
        this.textColor = textColor;

        SQLiteDatabase database = myDatabase.getReadableDatabase();
        Cursor cursor = database.query(MyDatabase.TB_DEB_NAME,null,null,null,null,null,null);
        if (cursor.moveToFirst())
            do {
                names.add(cursor.getString(cursor.getColumnIndex(MyDatabase.COL_NAME)));
                debts.add(cursor.getDouble(cursor.getColumnIndex(MyDatabase.COL_SUM)));
                ids.add(cursor.getInt(cursor.getColumnIndex(MyDatabase.COL_ID)));
            } while (cursor.moveToNext());

        cursor.close();
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.item_debt,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        holder.name.setText(names.get(position));
        holder.debt.setText(debts.get(position)+"");
        holder.id.setText(ids.get(position)+"");

        holder.card.setCardBackgroundColor(cardColor);
        holder.name.setTextColor(textColor);
        holder.debt.setTextColor(textColor);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,EditDebtActivity.class);
                intent.putExtra("id",Integer.parseInt(holder.id.getText().toString()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView debt;
        TextView id;
        CardView card;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            debt = itemView.findViewById(R.id.debt);
            id = itemView.findViewById(R.id.id);
            card = itemView.findViewById(R.id.card);

        }
    }
}

