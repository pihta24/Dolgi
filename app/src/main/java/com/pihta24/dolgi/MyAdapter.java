package com.pihta24.dolgi;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.ConnectException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;

    MyDatabase database;
    SQLiteDatabase database_r;

    private int cardColor = Color.parseColor("#C0C0C0");
    private int textColor = Color.parseColor("#000000");

    private LayoutInflater layoutInflater;

    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<Double> debts = new ArrayList<>();
    private ArrayList<Integer> ids = new ArrayList<>();

    Retrofit retrofit;
    final MyAPI api;

    public MyAdapter(Context context) {
        this.context = context;
        retrofit = new Retrofit.Builder().baseUrl(context.getString(R.string.api_address)).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).build();
        api = retrofit.create(MyAPI.class);
        layoutInflater = LayoutInflater.from(context);

        database = new MyDatabase(context);
        database_r = database.getReadableDatabase();
    }

    public void update(int cardColor, int textColor) {
        names.clear();
        debts.clear();
        ids.clear();

        this.cardColor = cardColor;
        this.textColor = textColor;

        MyAPI.MyGetDatabaseBody body = new MyAPI.MyGetDatabaseBody();

        Cursor cursor = database_r.query("settings", new String[]{"value"}, "parameter = 'token'", null, null, null, null);
        cursor.moveToFirst();

        body.id_token = cursor.getString(cursor.getColumnIndex("value"));

        api.get_database(body).enqueue(new Callback<MyAPI.MyDatabaseResponse>() {
            @Override
            public void onResponse(Call<MyAPI.MyDatabaseResponse> call, Response<MyAPI.MyDatabaseResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().response.equals("ok") && response.body().data != null) {
                        for (MyAPI.MyDatabaseNode node : response.body().data) {
                            names.add(node.name);
                            debts.add(node.debt);
                            ids.add(node.id);
                        }
                        notifyDataSetChanged();
                        MainActivity.update.setRefreshing(false);
                    } else if (response.body().response.equals("not valid token")) {
                        Toast.makeText(context, "Ошибка авторизации\nВойдите заново", Toast.LENGTH_SHORT).show();
                        MainActivity.update.setRefreshing(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<MyAPI.MyDatabaseResponse> call, Throwable t) {
                if (t instanceof ConnectException){
                    Toast.makeText(context, "Нет подключения к интернету", Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, NoConnection.class));
                }
                MainActivity.update.setRefreshing(false);
            }
        });
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.item_debt, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        holder.name.setText(names.get(position));
        holder.debt.setText(debts.get(position) + "");
        holder.id.setText(ids.get(position) + "");

        holder.card.setCardBackgroundColor(cardColor);
        holder.name.setTextColor(textColor);
        holder.debt.setTextColor(textColor);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditDebtActivity.class);
                intent.putExtra("id", Integer.parseInt(holder.id.getText().toString()));
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

