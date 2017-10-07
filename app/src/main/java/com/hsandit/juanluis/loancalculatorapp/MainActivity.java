package com.hsandit.juanluis.loancalculatorapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mAmount; //Monto
    private EditText mInstallments; //Cuotas
    private EditText mInterest; //Tasa de Interes
    private EditText mPaymentDate; //Fecha de Pago
    private DatePickerDialog mDatePicker;
    private Calendar mCalendar;
    private SimpleDateFormat mDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCalendar = Calendar.getInstance();
        mDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        mDatePicker = new DatePickerDialog(this, mDatePickerListener,
                mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DATE));

        mAmount = (EditText)findViewById(R.id.amount_edit_text);
        mInstallments = (EditText)findViewById(R.id.installments_edit_text);
        mInterest = (EditText)findViewById(R.id.interest_edit_text);
        mPaymentDate = (EditText) findViewById(R.id.payment_date_text);
        mPaymentDate.setEnabled(false);

        findViewById(R.id.pick_date_buttom).setOnClickListener(this);
        findViewById(R.id.calculate_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        Intent intent = null;

        switch (view.getId()){
            case R.id.pick_date_buttom:
                mDatePicker.show();
                break;
            case R.id.calculate_button:
                if (!mAmount.getText().toString().equals("") && !mInstallments.getText().toString().equals("") &&
                        !mInterest.getText().toString().equals("") && !mPaymentDate.getText().toString().equals("")) {
                    intent = new Intent(MainActivity.this, PaymentBreakdownActivity.class);
                    intent.putExtra("TITLE", "Payment Breakdown");
                    intent.putExtra("AMOUNT", mAmount.getText().toString());
                    intent.putExtra("INSTALLMENTS", mInstallments.getText().toString());
                    intent.putExtra("INTEREST", mInterest.getText().toString());
                    intent.putExtra("DAY_OF_MONTH", mCalendar.get(Calendar.DATE));
                    intent.putExtra("MONTH", mCalendar.get(Calendar.MONTH));
                    intent.putExtra("YEAR", mCalendar.get(Calendar.YEAR));
                } else {
                    Toast.makeText(this, "Please, validate all inputs.", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        if (intent != null) {
            startActivity (intent);
        }
    }

    private DatePickerDialog.OnDateSetListener mDatePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int j, int k) {
            mCalendar.set(Calendar.YEAR, i);
            mCalendar.set(Calendar.MONTH, j);
            mCalendar.set(Calendar.DATE, k);
            setPaymentDate();
        }
    };

    private void setPaymentDate() { mPaymentDate.setText(mDateFormat.format(mCalendar.getTime()));}
}
