package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class income extends AppCompatActivity {

    ListView listView;
    FirebaseListAdapter adapter;
    double totincome;
    TextView textView;
    Button btncheck;//Add the payment temporary

    //Temporary
    DatabaseReference dbref;
    PaymentModel paymentModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);

        listView = findViewById(R.id.listIncome);
        textView = findViewById(R.id.txtIncome);

        //Temporary
        btncheck = findViewById(R.id.test);
        paymentModel = new PaymentModel();

         //Check this
        Query query = FirebaseDatabase.getInstance().getReference().child("Payment");
        FirebaseListOptions<PaymentModel> options = new FirebaseListOptions.Builder<PaymentModel>()
                .setLayout(R.layout.payment_info)
                .setLifecycleOwner(income.this)
                .setQuery(query,PaymentModel.class)
                .build();

        adapter = new FirebaseListAdapter(options) {
            double income = 0;
            @Override
            protected void populateView(View v, Object model, int position) {
                TextView payid = v.findViewById(R.id.payID);
                TextView paydate = v.findViewById(R.id.paydate);
                TextView category = v.findViewById(R.id.paycat);
                TextView amount = v.findViewById(R.id.amount);

                PaymentModel pay = (PaymentModel) model;
                payid.setText("Payment ID : "+pay.getPaymentID().toString());
                paydate.setText("Payment Date : "+pay.getPayDate().toString());
                category.setText("Payment Category : "+pay.getPayCategory().toString());
                amount.setText("Payment Amount : "+pay.getAmount().toString());

                income += Double.parseDouble(pay.getAmount());
                textView.setText(String.valueOf(income));
            }
        };
        listView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    //Temporary
    @Override
    protected void onResume() {
        super.onResume();

        btncheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dbref = FirebaseDatabase.getInstance().getReference().child("Payment");

                paymentModel.setPaymentID("1");
                paymentModel.setPayCategory("Rooms");
                paymentModel.setPayDate("2020/01/01");
                paymentModel.setAmount("2000");

                dbref.push().setValue(paymentModel);

                Toast.makeText(getApplicationContext(), "Payment Added", Toast.LENGTH_SHORT).show();


            }
        });
    }
}