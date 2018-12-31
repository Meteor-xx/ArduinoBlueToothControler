package bluedevice.com;

/**
   created by meteor on 18/12/1
 **/
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static int REQUEST_ENABLE_BT = 1;
    private String msn = "msn";

    private Button mDiscoverBtn;
    private Button mShowBtn;
    private ListView mDevicesList;
    private Switch mSwitch;

    private BluetoothAdapter mBTAdapter;
    private ArrayAdapter<String> mBTArrayAdapter;
    private List<BluetoothDevice> bluetoothDevices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(msn, "onCreate: MainActivity");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 2){
            Log.i(msn, "onActivityResult: " + data.getStringExtra("data"));
        }
        else if(resultCode == 3)
        {
            Log.i(msn, "onActivityResult: " + data.getStringExtra("data"));
        }
    }


    private void bluetoothOn(View view){
            if (!mBTAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                Toast.makeText(getApplicationContext(), "Bluetooth turned on", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getApplicationContext(), "Bluetooth is already on", Toast.LENGTH_SHORT).show();
            }
    }


    private void bluetoothOff(View view) {
        mBTAdapter.disable(); // turn off
        mBTArrayAdapter.clear();
        bluetoothDevices.clear();
        Toast.makeText(getApplicationContext(), "Bluetooth turned Off", Toast.LENGTH_SHORT).show();
    }


    private void discover(View view){
        if(mBTAdapter.isDiscovering()){
            mBTAdapter.cancelDiscovery();
            Toast.makeText(getBaseContext(),"Discover stoped",Toast.LENGTH_SHORT).show();
        }else {
            if(mBTAdapter.isEnabled()){
                mBTArrayAdapter.clear();
                bluetoothDevices.clear();
                mBTAdapter.startDiscovery();
                Toast.makeText(getBaseContext(),"Start discovering...",Toast.LENGTH_SHORT).show();
                registerReceiver(blReceiver,new IntentFilter(BluetoothDevice.ACTION_FOUND));
            }
        }
    }


    final BroadcastReceiver blReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action))
            {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mBTArrayAdapter.add(device.getName()+"\n"+device.getAddress());
                bluetoothDevices.add(device);
                mBTArrayAdapter.notifyDataSetChanged();
            }
        }
    };


    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {
                Method method = BluetoothDevice.class.getMethod("createBond");
                method.invoke(bluetoothDevices.get(position));
                Toast.makeText(getBaseContext(), "pairing", Toast.LENGTH_SHORT).show();

            } catch (NoSuchMethodException e) {
                Toast.makeText(getBaseContext(), "pairing failed", Toast.LENGTH_SHORT).show();
            } catch (IllegalAccessException e) {
                Toast.makeText(getBaseContext(), "pairing failed", Toast.LENGTH_SHORT).show();
            } catch (InvocationTargetException e) {
                Toast.makeText(getBaseContext(), "pairing failed", Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(getBaseContext(),"Paired",Toast.LENGTH_SHORT).show();
        }
    };

    public void  onDestroy(){
        Log.i(msn, "onDestroy: MainActivity");
        unregisterReceiver(blReceiver);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        Log.i(msn, "onPause: MainActivity");
        super.onPause();

    }

    @Override
    protected void onRestart() {
        Log.i(msn, "onRestart: MainActivity");
        super.onRestart();

    }

    @Override
    protected void onResume() {
        Log.i(msn, "onResume: MainActivity");
        super.onResume();

    }
    @Override
    protected void onStart() {
        Log.i(msn, "onStart: MainActivity");
        super.onStart();
        mSwitch = (Switch) findViewById(R.id.switch1);
        mDevicesList = (ListView) findViewById(R.id.device_lists);
        mDiscoverBtn = (Button)findViewById(R.id.discover);
        mShowBtn = (Button)findViewById(R.id.show);

        //List
        bluetoothDevices = new ArrayList<>();
        //Adapter
        mBTAdapter = BluetoothAdapter.getDefaultAdapter();
        mBTArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        mDevicesList.setAdapter(mBTArrayAdapter);
        mDevicesList.setOnItemClickListener(mDeviceClickListener);


        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    bluetoothOn(buttonView);
                    //打开蓝牙优化 。。。。。。-
                } else {
                    bluetoothOff(buttonView);
                }
            }
        });

        mDiscoverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discover(v);
            }
        });

        mShowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity.this,controlActivity.class);
                startActivityForResult(intent,1);
            }
        });

    }

    @Override
    protected void onStop() {
        Log.i(msn, "onStop: MainActivity");
        super.onStop();

    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
