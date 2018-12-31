package bluedevice.com;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import bluedevice.com.Wheel.WheelView;

public class controlActivity extends AppCompatActivity implements WheelView.WheelClickListener {

    private Button paired;
    private ListView pairedlist;
    private WheelView wheelView;

    private BluetoothAdapter mmBTAdapter;
    private ArrayAdapter<String> mmBTArrayAdapter;
    private Set<BluetoothDevice> devices;


    private static UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothSocket mCSocket;
    private OutputStream os;
    private static String Click_Right_Down = "0";
    private static String Click_Bottom_Down = "1";
    private static String Click_Left_Down = "2";
    private static String Click_Top_Down = "3";
    private static String Click_Right_Up = "4";
    private static String Click_Bottom_Up = "4";
    private static String Click_Left_Up = "4";
    private static String Click_Top_Up = "4";
    private static String error0 = "蓝牙未连接";
    private static String error1 = "蓝牙未打开，请打开蓝牙";
    private String msn = "msn";




    @Override
    protected void onCreate(Bundle savedInstanceState1) {
        super.onCreate(savedInstanceState1);
        setContentView(R.layout.control_activity);
        Log.i(msn, "onCreate: CActivity");
        paired = (Button) findViewById(R.id.paired);
        pairedlist = (ListView)findViewById(R.id.paired_lists);
        wheelView = (WheelView) findViewById(R.id.ctrls);
        mmBTAdapter = BluetoothAdapter.getDefaultAdapter();
        mmBTArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);

        pairedlist.setAdapter(mmBTArrayAdapter);
        pairedlist.setOnItemClickListener(mPairedDevicesClickListener);

        wheelView.setWheelClickListener(this);



        paired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listout(v);
            }
        });



    }



    @Override
    public void onWheelClick(int type) {
        switch (type) {
            case WheelView.CLICK_BOTTOM_DOWN:
                //下面按钮按下的时候
                try {
                    os.write(Click_Bottom_Down.getBytes("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (RuntimeException e){
                    Log.i(msn, error0);
                }
                break;
            case WheelView.CLICK_LEFT_DOWN:
                //左边按钮按下的时候
                try {
                    os.write(Click_Left_Down.getBytes("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (RuntimeException e){
                    Log.i(msn, error0);
                }
                break;
            case WheelView.CLICK_RIGHT_DOWN:
                //右边按钮按下的时候
                try {
                    os.write(Click_Right_Down.getBytes("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (RuntimeException e){
                    Log.i(msn, error0);
                }
                break;
            case WheelView.CLICK_TOP_DOWN:
                //上面按钮按下的时候
                try {
                    os.write(Click_Top_Down.getBytes("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (RuntimeException e){
                    Log.i(msn, error0);
                }
                break;
            case WheelView.CLICK_BOTTOM_UP:
                //下面按钮按下抬起的时候
                try {
                    os.write(Click_Bottom_Up.getBytes("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (RuntimeException e){
                    Log.i(msn, error0);
                    Toast.makeText(getBaseContext(),"蓝牙未连接，请连接蓝牙设备",Toast.LENGTH_SHORT).show();
                }
                break;
            case WheelView.CLICK_LEFT_UP:
                //左边按钮按下抬起的时候
                try {
                    os.write(Click_Left_Up.getBytes("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (RuntimeException e){
                    Log.i(msn, error0);
                    Toast.makeText(getBaseContext(),"蓝牙未连接，请连接蓝牙设备",Toast.LENGTH_SHORT).show();
                }
                break;
            case WheelView.CLICK_RIGHT_UP:
                //右边按钮按下抬起的时候
                try {
                    os.write(Click_Right_Up.getBytes("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (RuntimeException e){
                    Log.i(msn, error0);
                    Toast.makeText(getBaseContext(),"蓝牙未连接，请连接蓝牙设备",Toast.LENGTH_SHORT).show();
                }
                break;
            case WheelView.CLICK_TOP_UP:
                //上面按钮按下抬起的时候
                try {
                    os.write(Click_Top_Up.getBytes("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (RuntimeException e){
                    Log.i(msn, error0);
                    Toast.makeText(getBaseContext(),"蓝牙未连接，请连接蓝牙设备",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void listout(View v) {
        devices = mmBTAdapter.getBondedDevices();
        if (mmBTAdapter.isEnabled()) {
            mmBTArrayAdapter.clear();
            for (BluetoothDevice device : devices) {
                mmBTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                mmBTArrayAdapter.notifyDataSetChanged();
            }
            Toast.makeText(getBaseContext(), "Has shown all paired devices", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getBaseContext(), "蓝牙未打开，请打开蓝牙", Toast.LENGTH_LONG).show();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            set_result(3, error1);
            finish();
        }
    }

    private void set_result(int a,String string){
        Intent data = new Intent();
        data.putExtra("data",string);
        setResult(a,data);
    }


    private AdapterView.OnItemClickListener mPairedDevicesClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String info = ((TextView) view).getText().toString();
            final String address = info.substring(info.length() - 17);
            final String name = info.substring(0, info.length() - 17);
            final BluetoothDevice mCBTdevices = mmBTAdapter.getRemoteDevice(address);
            new Thread() {
                public void run() {
                    try {
                        if (mmBTAdapter.isDiscovering())
                            mmBTAdapter.cancelDiscovery();
                        mCSocket = mCBTdevices.createRfcommSocketToServiceRecord(BTMODULEUUID);
                    } catch (RuntimeException e) {

                    }
                    catch (IOException e){

                    }
                    try {
                        mCSocket.connect();
                        Toast.makeText(getBaseContext(),"Connected!!!",Toast.LENGTH_LONG).show();
                        os = mCSocket.getOutputStream();
                    } catch (Exception e) {
                        try {
                            mCSocket.close();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getBaseContext(),"连接失败，请检查设备是否在附近",Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (Exception e2) {
                            Log.i(msn, "mCSocket close failed");
                        }
                    }
                    long nStartMillTime = System.currentTimeMillis();

                    try {
                        long havePassTime = System.currentTimeMillis() - nStartMillTime;
                        if (havePassTime < 6000)
                            Thread.sleep(7000 - havePassTime);
                        }
                        catch (Exception e) {

                    }
                }
            }.start();


        }
    };


    public void  onDestroy(){
        Log.i(msn, "onDestroy: CActivity");
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        Log.i(msn, "onPause: CActivity");
        super.onPause();

    }

    @Override
    protected void onRestart() {
        Log.i(msn, "onRestart: CActivity");
        super.onRestart();

    }

    @Override
    protected void onResume() {
        Log.i(msn, "onResume: CActivity");
        super.onResume();

    }

    @Override
    protected void onStart() {
        Log.i(msn, "onStart: CActivity");
        super.onStart();

    }

    @Override
    protected void onStop() {
        Log.i(msn, "onStop: CActivity");
        super.onStop();

    }
}


