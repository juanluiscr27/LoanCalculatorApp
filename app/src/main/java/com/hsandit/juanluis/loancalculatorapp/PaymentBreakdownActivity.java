package com.hsandit.juanluis.loancalculatorapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PaymentBreakdownActivity extends AppCompatActivity {

    private Calendar mPaymentCalendar;
    private SimpleDateFormat mDateFormat;
    private TextView mHeader;
    private TextView mPaymentBreakDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String title = "No Title";
        double amount = 0;
        int installments = 0;
        double interest = 0;
        int day = 0; int month = 0;  int year = 0;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_breakdown);

        mHeader = (TextView) findViewById(R.id.header_text_view);
        mPaymentBreakDown = (TextView) findViewById(R.id.breakdown_text_view);

        if (getIntent().hasExtra("TITLE")) title = getIntent().getStringExtra("TITLE");
        if (getIntent().hasExtra("AMOUNT")) amount = Double.parseDouble(getIntent().getStringExtra("AMOUNT"));
        if (getIntent().hasExtra("INSTALLMENTS")) installments = Integer.parseInt(getIntent().getStringExtra("INSTALLMENTS"));
        if (getIntent().hasExtra("INTEREST")) interest = Double.parseDouble(getIntent().getStringExtra("INTEREST"));
        if (getIntent().hasExtra("DAY_OF_MONTH")) day = getIntent().getIntExtra("DAY_OF_MONTH", 0);
        if (getIntent().hasExtra("MONTH")) month = getIntent().getIntExtra("MONTH", 0);
        if (getIntent().hasExtra("YEAR")) year = getIntent().getIntExtra("YEAR", 0);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

        mDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        mPaymentCalendar = Calendar.getInstance();
        mPaymentCalendar.set(year, month, day);

        calculateLoanPayments(amount, installments, interest/100/12);
    }
    // Calculo de la amortizacion del prestamo
    private void calculateLoanPayments(double amount, int installments, double interest) {

        DecimalFormat decimalFormat = new DecimalFormat("###.##");

        // pagos = (monto * tasa de interes)/(1 - (1 + tasa de interes)^ -cuotas)
        double payment = (amount * interest)/(1-Math.pow(1+ interest,-installments));
        double nPrincipal; double nInterest; double nBalance = amount;

        String header = "Fecha        Pago       Capital       Interes       Saldo \r\n";
        StringBuilder sbPaymentBreakDown = new StringBuilder();
        sbPaymentBreakDown.append("\n");
        sbPaymentBreakDown.append(mDateFormat.format(mPaymentCalendar.getTime()));
        sbPaymentBreakDown.append("          0                 0                0              ");
        sbPaymentBreakDown.append(decimalFormat.format(nBalance));
        sbPaymentBreakDown.append("\r\n");

        for (int i = 0; i < installments; i++ ) {
            nInterest = nBalance*interest; // pago de interes = monto * tasa de interes
            nPrincipal = payment-nInterest; // pago de capital = cuota de pago - pago de interes
            nBalance -= nPrincipal; // monto = monto - capital

            mPaymentCalendar.add(Calendar.MONTH, 1);

            sbPaymentBreakDown.append("\n");
            sbPaymentBreakDown.append(mDateFormat.format(mPaymentCalendar.getTime()));
            sbPaymentBreakDown.append("    ");
            sbPaymentBreakDown.append(decimalFormat.format(payment));
            sbPaymentBreakDown.append("      ");
            sbPaymentBreakDown.append(decimalFormat.format(nPrincipal));
            sbPaymentBreakDown.append("      ");
            sbPaymentBreakDown.append(decimalFormat.format(nInterest));
            sbPaymentBreakDown.append("        ");
            sbPaymentBreakDown.append(decimalFormat.format(Math.abs(nBalance)));
            sbPaymentBreakDown.append("\r\n");
        }

        String paymentBreakDown = sbPaymentBreakDown.toString();

        mHeader.setText(header);
        mPaymentBreakDown.setText(paymentBreakDown);
    }
}
