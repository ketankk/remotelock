package kuari.in.remotelock;

import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by root on 4/23/17.
 */

public class SmsIncoming extends BroadcastReceiver{
    private static final String keyWord="LOCKMYPHONE";
    private static final String error="ERROR";

    private String lockPin;
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;
        Toast.makeText(context,"Onrece",Toast.LENGTH_LONG).show();
        Log.d("Onrec","rece");
        lockPin=context.getSharedPreferences("LOCKKEY", Context.MODE_PRIVATE).getString("key",error);
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle=intent.getExtras();
            String smsBody="";
            if(bundle!=null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                if (pdus != null) {
                    SmsMessage[] smsMessages = new SmsMessage[pdus.length];
                    for (int i = 0; i < smsMessages.length; i++) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            String format = bundle.getString("format");
                            smsMessages[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                        } else
                            smsMessages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        smsBody += smsMessages[i].getDisplayMessageBody();
                    }
                    context.getSharedPreferences("msg", Context.MODE_PRIVATE).edit().putString("lastmsg", smsBody).apply();
                    Toast.makeText(context, smsBody, Toast.LENGTH_LONG).show();

                    lockOnmsg(smsBody);
                    parseMessage(smsBody);

                }
            }

        }
    }
    void lockOnmsg(String sms){
        lockMyPhone();
    }
    private void parseMessage(String smsBody) {
        String[] strArr = smsBody.split(" ");
        // System.out.print(smsBody);
        if(strArr.length>2)
            return;
        if(strArr[0].equals(keyWord)&&strArr[1].equals(lockPin)){
            lockMyPhone();
        }
    }
    DevicePolicyManager deviceManager;
    ComponentName  componentName;

    private void lockMyPhone() {
        Log.d("lockMyPhone","Locking");
        deviceManager= (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentName=new ComponentName(context,AdminClass.class);
        Log.d("val2",deviceManager.isAdminActive(componentName)+"");

        if(deviceManager.isAdminActive(componentName)){
            deviceManager.lockNow();
        }


    }
}

