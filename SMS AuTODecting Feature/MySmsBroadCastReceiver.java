package com.pigi.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;

import com.pigi.app.MainActivity;

/**
 * Created by mind on 7/1/16.
 */
public class MySmsBroadCastReceiver extends BroadcastReceiver {


    MainActivity main = null;

    public void setMainActivityHandler(MainActivity main) {
        this.main = main;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            String sender = "";
            Object[] pdus1 = (Object[]) intent.getExtras().get("pdus");
            SmsMessage shortMessage1 = SmsMessage.createFromPdu((byte[]) pdus1[0]);

           /* Log.d("SMSReceiver", "SMS message sender: " +
                    shortMessage1.getOriginatingAddress());
            Log.d("SMSReceiver", "SMS message text: " +
                    shortMessage1.getDisplayMessageBody());*/
            sender = shortMessage1.getOriginatingAddress();
            Object[] pdus2 = (Object[]) intent.getExtras().get("pdus");

            StringBuilder text = new StringBuilder();
            // get sender from first PDU
            SmsMessage shortMessage2 = SmsMessage.createFromPdu((byte[]) pdus2[0]);

            for (int i = 0; i < pdus2.length; i++) {
                shortMessage2 = SmsMessage.createFromPdu((byte[]) pdus2[i]);
                text.append(shortMessage2.getDisplayMessageBody());
            }
            // SMS message will be like :
            //BH-NOTIBY
            //Welcome to Notiby. You are about to verify your account. You can proceed further using  OTP code : N2O2T5I6.
            if (text.toString().contains(":")) {

                if (!sender.trim().equalsIgnoreCase("") && sender != null) {
                    if (sender.contains("-PLVSMS")) {
                        String otp_to_broadcast = "";

                        //Methods.syso(" @@@@ Sms text @@ " + text.toString());
                        // Methods.syso(" @@@@ Sms text at 0 @@ " + text.toString().split(":")[0].trim());
                        // Methods.syso(" @@@@ Sms text OTP  @@  " + text.toString().split(":")[1].trim());

                        otp_to_broadcast = text.toString().split(":")[1].trim();

                        sendBroadcastToRefresOTP(context, otp_to_broadcast);
                    }
                }

                //Toast.makeText(context, "  <<Sms text OTP only at 00000 >>  " + text.toString().split(":")[0].trim() + "  <<Sms text OTP only at 11111 >>  " + text.toString().split(":")[1].trim() + " sender " + shortMessage1.getOriginatingAddress(), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendBroadcastToRefresOTP(Context context, String otp_value) {

        //verify signup
        Intent intent = new Intent("getotp");
        intent.putExtra("otp", otp_value);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);


    }
}
