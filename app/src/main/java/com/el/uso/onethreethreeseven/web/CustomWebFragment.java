package com.el.uso.onethreethreeseven.web;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.el.uso.onethreethreeseven.BaseFragment;
import com.el.uso.onethreethreeseven.Constants;
import com.el.uso.onethreethreeseven.R;
import com.el.uso.onethreethreeseven.helper.PaymentsUtil;
import com.el.uso.onethreethreeseven.helper.Utils;
import com.el.uso.onethreethreeseven.log.L;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentMethodToken;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.TransactionInfo;
import com.google.android.gms.wallet.WalletConstants;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CustomWebFragment extends BaseFragment {

    private static final String TAG = "CustomWebFragment";
    public static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 991;
    private static final BigDecimal MICROS = new BigDecimal(1000000d);

    // Supported countries for shipping (use ISO 3166-1 alpha-2 country codes).
    // Relevant only when requesting a shipping address.
    public static final List<String> SHIPPING_SUPPORTED_COUNTRIES = Arrays.asList(
            "US",
            "GB"
    );

    public static final List<Integer> SUPPORTED_METHODS = Arrays.asList(
            // PAYMENT_METHOD_CARD returns to any card the user has stored in their Google Account.
            WalletConstants.PAYMENT_METHOD_CARD,

            // PAYMENT_METHOD_TOKENIZED_CARD refers to EMV tokenized credentials stored in the
            // Google Pay app, assuming it's installed.
            // Please keep in mind tokenized cards may exist in the Google Pay app without being
            // added to the user's Google Account.
            WalletConstants.PAYMENT_METHOD_TOKENIZED_CARD
    );

    public static final List<Integer> SUPPORTED_NETWORKS = Arrays.asList(
            WalletConstants.CARD_NETWORK_AMEX,
            WalletConstants.CARD_NETWORK_DISCOVER,
            WalletConstants.CARD_NETWORK_VISA,
            WalletConstants.CARD_NETWORK_MASTERCARD
    );

    // The name of your payment processor / gateway. Please refer to their documentation for
    // more information.
    public static final String GATEWAY_TOKENIZATION_NAME = "example";

    // Custom parameters required by the processor / gateway.
    // In many cases, your processor / gateway will only require a gatewayMerchantId.
    // Please refer to your processor's documentation for more information. The number of parameters
    // required and their names vary depending on the processor.
    public static final List<Pair<String, String>> GATEWAY_TOKENIZATION_PARAMETERS = Arrays.asList(
            Pair.create("gatewayMerchantId", "exampleGatewayMerchantId")

            // Your processor may require additional parameters.
    );

    // Only used for DIRECT tokenization. Can be removed when using GATEWAY tokenization.
    public static final String DIRECT_TOKENIZATION_PUBLIC_KEY = "REPLACE_ME";

    private View mContentView;
    // Arbitrarily-picked result code.

    private PaymentsClient mPaymentsClient;

    private ObservableWebView mGooglePayWeb;
    private View mGooglePayButton;
    private TextView mGooglePayStatusText;
    private ItemInfo mBikeItem = new ItemInfo("Simple Bike", 300 * 1000000, R.drawable.bike);
    private long mShippingCost = 90 * 1000000;

    public static CustomWebFragment newInstance() {
        return new CustomWebFragment();
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        L.d(TAG, "onCreateView");
        mContentView = inflater.inflate(R.layout.checkout_fragment, container, false);
        mGooglePayWeb = mContentView.findViewById(R.id.googlepay_web);
        mGooglePayWeb.getSettings().setJavaScriptEnabled(true);
        mGooglePayWeb.addJavascriptInterface(new GooglePayInterface(), "GooglePayInterface");
        mGooglePayWeb.getSettings().setDomStorageEnabled(true);
        ObservableWebView.setWebContentsDebuggingEnabled(true);
        mGooglePayWeb.setWebChromeClient(new WebChromeClient());
        mGooglePayWeb.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                L.d(TAG, "onPageFinished, url = " + url);
            }
        });
        mGooglePayWeb.loadUrl("file:///android_asset/google_pay.html");

        mGooglePayButton = mContentView.findViewById(R.id.googlepay_button);
        mGooglePayStatusText = mContentView.findViewById(R.id.googlepay_status);

        mGooglePayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPayment(view);
            }
        });

        // It's recommended to create the PaymentsClient object inside of the onCreate method.
        mPaymentsClient = PaymentsUtil.createPaymentsClient(getActivity());
        checkIsReadyToPay();
        return mContentView;
    }

    private void checkIsReadyToPay() {
        // The call to isReadyToPay is asynchronous and returns a Task. We need to provide an
        // OnCompleteListener to be triggered when the result of the call is known.
        PaymentsUtil.isReadyToPay(mPaymentsClient)
                .addOnCompleteListener(new OnCompleteListener<Boolean>() {
                    public void onComplete(Task<Boolean> task) {
                        try {
                            boolean result = task.getResult(ApiException.class);
                            setGooglePayAvailable(result);
                        } catch (ApiException exception) {
                            // Process error
                            L.w(TAG, "isReadyToPay failed: " + exception);
                            setGooglePayAvailable(false);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private void setGooglePayAvailable(boolean available) {
        // If isReadyToPay returned true, show the button and hide the "checking" text. Otherwise,
        // notify the user that Pay with Google is not available.
        // Please adjust to fit in with your current user flow. You are not required to explicitly
        // let the user know if isReadyToPay returns false.
        if (available) {
            mGooglePayStatusText.setVisibility(View.GONE);
            mGooglePayButton.setVisibility(View.VISIBLE);
        } else {
            mGooglePayStatusText.setText(R.string.googlepay_status_unavailable);
        }
    }

    public void handlePaymentSuccess(PaymentData paymentData) {
        // PaymentMethodToken contains the payment information, as well as any additional
        // requested information, such as billing and shipping address.
        //
        // Refer to your processor's documentation on how to proceed from here.

        // Re-enables the Pay with Google button.
        mGooglePayButton.setClickable(true);
        PaymentMethodToken token = paymentData.getPaymentMethodToken();

        // getPaymentMethodToken will only return null if PaymentMethodTokenizationParameters was
        // not set in the PaymentRequest.
        if (token != null) {
            // If the gateway is set to example, no payment information is returned - instead, the
            // token will only consist of "examplePaymentMethodToken".
            if (token.getToken().equals("examplePaymentMethodToken")) {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                        .setTitle("Warning")
                        .setMessage("Gateway name set to \"example\" - please modify " +
                                "Constants.java and replace it with your own gateway.")
                        .setPositiveButton(android.R.string.ok, null)
                        .create();
                alertDialog.show();
            }

            String billingName = paymentData.getCardInfo().getBillingAddress().getName();
            Toast.makeText(getActivity(), getString(R.string.payments_show_name, billingName), Toast.LENGTH_LONG).show();

            // Use token.getToken() to get the token string.
            L.d("PaymentData", "PaymentMethodToken received");
        }
    }

    public void handleError(int statusCode) {
        // At this stage, the user has already seen a popup informing them an error occurred.
        // Normally, only logging is required.
        // statusCode will hold the value of any constant from CommonStatusCode or one of the
        // WalletConstants.ERROR_CODE_* constants.
        String errorMsg = "loadPaymentData failed: " + String.format(Locale.getDefault(), "Error code: %d", statusCode);
        L.w(TAG, errorMsg);
        mGooglePayWeb.loadUrl("javascript:onPaymentResult('" + errorMsg + "')");
        // Re-enables the Pay with Google button.
        mGooglePayButton.setClickable(true);
    }

    // This method is called when the Pay with Google button is clicked.
    public void requestPayment(View view) {
        // Disables the button to prevent multiple clicks.
        mGooglePayButton.setClickable(false);

        // The price provided to the API should include taxes and shipping.
        // This price is not displayed to the user.
        String price = PaymentsUtil.microsToString(mBikeItem.getPriceMicros() + mShippingCost);

        TransactionInfo transaction = PaymentsUtil.createTransaction(price);
        PaymentDataRequest request = PaymentsUtil.createPaymentDataRequestDirect(transaction);
        Task<PaymentData> futurePaymentData = mPaymentsClient.loadPaymentData(request);

        // Since loadPaymentData may show the UI asking the user to select a payment method, we use
        // AutoResolveHelper to wait for the user interacting with it. Once completed,
        // onActivityResult will be called with the result.
        AutoResolveHelper.resolveTask(futurePaymentData, getActivity(), LOAD_PAYMENT_DATA_REQUEST_CODE);
    }

    public void requestPayment(PaymentDataRequest request) {
        Task<PaymentData> futurePaymentData = mPaymentsClient.loadPaymentData(request);

        // Since loadPaymentData may show the UI asking the user to select a payment method, we use
        // AutoResolveHelper to wait for the user interacting with it. Once completed,
        // onActivityResult will be called with the result.
        AutoResolveHelper.resolveTask(futurePaymentData, getActivity(), LOAD_PAYMENT_DATA_REQUEST_CODE);
    }

    public class ItemInfo {
        private final String name;
        private final int imageResourceId;

        // Micros are used for prices to avoid rounding errors when converting between currencies.
        private final long priceMicros;

        public ItemInfo(String name, long price, int imageResourceId) {
            this.name = name;
            this.priceMicros = price;
            this.imageResourceId = imageResourceId;
        }

        public String getName() {
            return name;
        }

        public int getImageResourceId() {
            return imageResourceId;
        }

        public long getPriceMicros() {
            return priceMicros;
        }
    }

    private class GooglePayInterface {

        @JavascriptInterface
        public boolean checkGooglePay() {
            return Utils.isAppInstalled(getContext(), Constants.GOOGLE_PAY_PACKAGE_NAME);
        }

        @JavascriptInterface
        public void onPaymentDataRequest(String paymentDataRequest) {
            L.w(TAG, "onPaymentDataRequest: " + paymentDataRequest);
            PaymentDataRequest request = PaymentDataRequest.fromJson(paymentDataRequest);
            if (Utils.isAppInstalled(getContext(), Constants.GOOGLE_PAY_PACKAGE_NAME)) {
                L.w(TAG, "detect Google Pay");
            } else {
                L.w(TAG, "no Google Pay");
                return;
            }
            requestPayment(request);
        }
    }
}
