package kuari.in.remotelock;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
//Permission grant
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chk();
updateKey();

        Button submitButton = (Button) findViewById(R.id.submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLockKey();
            }
        });
    }
    private void setLockKey(){

        //lock();
        EditText editText= (EditText) findViewById(R.id.password);
        String passKey= String.valueOf(editText.getText());
        SharedPreferences pref = getSharedPreferences("LOCKKEY", MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("key",passKey);

        boolean isSaved = edit.commit();
        if(isSaved){
            updateKey();

        }
    }
    void lock(){
        DevicePolicyManager deviceManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
//        activityManager= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName componentName = new ComponentName(this, AdminClass.class);
        Log.d("val",deviceManager.isAdminActive(componentName)+"");
        deviceManager.lockNow();

        if(deviceManager.isAdminActive(componentName)){
            deviceManager.lockNow();
        }

    }
    void chk(){
        TextView txt = (TextView) findViewById(R.id.chk);
        txt.setText(getSharedPreferences("msg",MODE_PRIVATE).getString("lastmsg","error"));
    }
    void updateKey(){
        SharedPreferences pref = getSharedPreferences("LOCKKEY", MODE_PRIVATE);

        TextView text = (TextView) findViewById(R.id.password_entered);
        text.setText(pref.getString("key","Error occurred"));
    }
}
