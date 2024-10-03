package com.example.mortagepaymentcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String INTEREST_HELP_TEXT = "Annual interest rate for this mortgage.";
    private static final String PRINCIPAL_HELP_TEXT = "This is usually the purchase price minus your down payment.";
    private static final String AMORTIZATION_HELP_TEXT = "Decide on the length of time you will take to repay the mortgage in full.";
    private static final String PAYMENT_HELP_TEXT = "Select how often you would like to make payments on your mortgage.";

    // Declare spinners as global variables
    private Spinner amortizationSpinner;
    private Spinner frequencySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        setupInsets();
        setupToolbar();
        setupSpinners();
        setupHelpButtons();
        setupSubmitButton();
        setupPrincipalEditText();
    }

    // Method to set system window insets
    private void setupInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Setup toolbar
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    // Setup spinners and their adapters
    private void setupSpinners() {
        amortizationSpinner = findViewById(R.id.spinner2);
        frequencySpinner = findViewById(R.id.amortizationSpinner);

        ArrayAdapter<String> amortizationAdapter = createAdapter(getAmortizationList());
        ArrayAdapter<String> frequencyAdapter = createAdapter(getFrequencyList());

        amortizationSpinner.setAdapter(amortizationAdapter);
        frequencySpinner.setAdapter(frequencyAdapter);

        setupSpinnerListener(amortizationSpinner);
        setupSpinnerListener(frequencySpinner);
    }

    // Create a simple adapter for a spinner
    private ArrayAdapter<String> createAdapter(List<String> data) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        return adapter;
    }

    // Setup listener for spinner selections
    private void setupSpinnerListener(Spinner spinner) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Can implement specific behavior here if needed
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // No action needed
            }
        });
    }

    // Help button setup
    private void setupHelpButtons() {
        setupHelpButton(R.id.prinicalHelpButton, PRINCIPAL_HELP_TEXT);
        setupHelpButton(R.id.interestHelpButton, INTEREST_HELP_TEXT);
        setupHelpButton(R.id.frequencyHelpButton, PAYMENT_HELP_TEXT);
        setupHelpButton(R.id.amortizationHelpButton, AMORTIZATION_HELP_TEXT);
    }

    // Setup help button click listener
    private void setupHelpButton(int buttonId, String helpText) {
        ImageButton button = findViewById(buttonId);
        button.setOnClickListener(v -> showToast(helpText));
    }

    // Display a Toast message
    private void showToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
    }

    // Setup the principal EditText for formatting
    private void setupPrincipalEditText() {
        EditText principalEditText = findViewById(R.id.principalEditText); // Make sure you have this EditText in your layout

        principalEditText.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String cleanString = s.toString().replaceAll("[^\\d]", "");
                    String formatted = formatNumber(cleanString);
                    current = formatted;
                    principalEditText.setText(formatted);
                    principalEditText.setSelection(formatted.length()); // Move cursor to the end
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    // Format number with commas
    private String formatNumber(String number) {
        if (number.isEmpty()) {
            return "";
        }
        double parsed = Double.parseDouble(number);
        return NumberFormat.getInstance(Locale.US).format(parsed);
    }

    // Setup the submit button
    private void setupSubmitButton() {
        Button submitButton = findViewById(R.id.submit);
        submitButton.setOnClickListener(v -> {
            // Retrieve selected values from spinners
            String amortization = (String) amortizationSpinner.getSelectedItem();
            String frequency = (String) frequencySpinner.getSelectedItem();

            EditText principalEditText = findViewById(R.id.principalEditText); // Make sure you have this EditText in your layout
            EditText interestEditText = findViewById(R.id.interestEditText); // Make sure you have this EditText in your layout

            // Check for empty inputs
            if (principalEditText.getText().toString().isEmpty() || interestEditText.getText().toString().isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter both principal and interest.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Parse the principal and interest
            double principal = Double.parseDouble(principalEditText.getText().toString().replaceAll(",", "")); // Remove commas for parsing
            double interest = Double.parseDouble(interestEditText.getText().toString());

            // Call the calculation method
            double[] emiResult = calculateEmi(principal, interest, PaymentFrequency.MONTHLY, amortization);
            DecimalFormat df = new DecimalFormat("0.00");
            String emiFormatted = df.format(emiResult[0]);
            String totalPayments = String.valueOf((int) emiResult[1]);

            Intent i = new Intent(MainActivity.this, CalculationResult.class);
            i.putExtra("emi", emiFormatted);
            i.putExtra("totalPayments", totalPayments);
            i.putExtra("amortization", amortization);
            startActivity(i);
        });
    }

    // Generate amortization list
    private List<String> getAmortizationList() {
        List<String> amortizationList = new ArrayList<>();
        amortizationList.add("1 Year");
        for (int x = 2; x <= 30; x++) {
            amortizationList.add(x + " Years");
        }
        return amortizationList;
    }

    // Generate frequency list
    private List<String> getFrequencyList() {
        List<String> frequencyList = new ArrayList<>();
        frequencyList.add("Monthly");
        // Add other frequencies as needed
        return frequencyList;
    }

    // EMI Calculation
    public double[] calculateEmi(double principal, double interest, PaymentFrequency frequency, String amortization) {
        double[] emi = new double[3];
        int years = Integer.parseInt(amortization.split(" ")[0]);
        int numberOfPayments = years * frequency.getPaymentsPerYear();
        interest = (interest / 100) / frequency.getPaymentsPerYear();
        emi[0] = principal * interest * Math.pow(1 + interest, numberOfPayments) / (Math.pow(1 + interest, numberOfPayments) - 1);
        emi[1] = numberOfPayments;
        return emi;
    }
}
