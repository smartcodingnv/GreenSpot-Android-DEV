package com.greenspot.app.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.greenspot.app.R;
import com.greenspot.app.utils.PreferenceHelper;
import com.greenspot.app.utils.Progress;
import com.greenspot.app.utils.Utils;
import com.greenspot.app.utils.ViewDialog;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;
import com.stripe.android.model.Card;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PaymnetJActivity extends AppCompatActivity {

    private Stripe stripe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);





        Button payButton = findViewById(R.id.btn_done);
        WeakReference<PaymnetJActivity> weakActivity = new WeakReference<>(this);
        payButton.setOnClickListener((View view) -> {
            // Get the card details from the card widget
            CardInputWidget cardInputWidget = findViewById(R.id.cardInputWidget);
            Card card = cardInputWidget.getCard();
            if (card != null) {
                // Create a Stripe token from the card details
                stripe = new Stripe(getApplicationContext(), PaymentConfiguration.getInstance(getApplicationContext()).getPublishableKey());


                stripe.createToken(card, new ApiResultCallback<Token>() {
                    @Override
                    public void onSuccess(@NonNull Token result) {
                        String tokenID = result.getId();
                        // Send the token identifier to the server...

                        Log.e("tokeen"," "+tokenID);
                    }

                    @Override
                    public void onError(@NonNull Exception e) {
                        // Handle error
                        Log.e("tokeen"," "+e.getMessage());
                        Log.e("tokeen"," "+e.getCause());
                    }
                });
            }
        });


    }

}
