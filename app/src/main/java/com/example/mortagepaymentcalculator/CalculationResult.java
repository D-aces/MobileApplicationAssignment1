package com.example.mortagepaymentcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

public class CalculationResult extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calculation_result);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalculationResult.this, MainActivity.class);
                startActivity(intent);
            }
        });

        String emi = getIntent().getStringExtra("emi");
        String totalPayments = getIntent().getStringExtra("totalPayments");
        String amortization = getIntent().getStringExtra("amortization");

        TextView monthlyOwingResult = findViewById(R.id.monthlyOwingResult);
        TextView totalPaymentsResult = findViewById(R.id.totalPaymentsResult);
        TextView totalAmortization = findViewById(R.id.totalAmortization);

        monthlyOwingResult.setText(String.format("$%s", emi));
        totalPaymentsResult.setText(totalPayments);
        totalAmortization.setText(amortization);

    }
}