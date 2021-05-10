package com.example.trsahm;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;
import java.util.UUID;


public class BillActivity extends AppCompatActivity implements Runnable{
    ListView lvHoaDon;
    Button btnThanhToan;
    ArrayList<CTHD> list_cthd;
    HoaDonAdapter adapter;
    Ban ban;
    Database database;

    protected static final String TAG = "TAG";
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    Button mScan, mPrint, mDisc;


    BluetoothAdapter mBluetoothAdapter;
    private UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ProgressDialog mBluetoothConnectProgressDialog;
    private BluetoothSocket mBluetoothSocket;
    BluetoothDevice mBluetoothDevice;


    TextView stat;

    LinearLayout layout;

    NumberFormat formatter = new DecimalFormat("#,### VND");

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        init();
        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tongtien=tinhtongtien(ban.getMaHD());
                Toast.makeText(BillActivity.this,tongtien+" VND",Toast.LENGTH_LONG).show();
                database.QueryData("UPDATE Ban SET MaHD=null, TrangThai='Còn trống' WHERE TenBan='"+ban.getTenBan()+"'");
                database.QueryData("DELETE FROM HoaDon WHERE MaHD='"+list_cthd.get(0).getMaHD()+"'");
                database.QueryData("DELETE FROM CTHD WHERE MaHD='"+list_cthd.get(0).getMaHD()+"'");
                Intent intent=new Intent();
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        stat = (TextView)findViewById(R.id.bpstatus);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        layout = (LinearLayout)findViewById(R.id.layout);


        mScan = (Button)findViewById(R.id.Scan);
        mScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View mView) {

                if(mScan.getText().equals("Connect"))
                {
                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mBluetoothAdapter == null) {
                        Toast.makeText(BillActivity.this, "Message1", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!mBluetoothAdapter.isEnabled()) {
                            Intent enableBtIntent = new Intent(
                                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            startActivityForResult(enableBtIntent,
                                    REQUEST_ENABLE_BT);
                        } else {
                            ListPairedDevices();
                            Intent connectIntent = new Intent(BillActivity.this,
                                    DeviceListActivity.class);
                            startActivityForResult(connectIntent,
                                    REQUEST_CONNECT_DEVICE);

                        }
                    }

                }
                else if(mScan.getText().equals("Disconnect"))
                {
                    if (mBluetoothAdapter != null)
                        mBluetoothAdapter.disable();
                    stat.setText("");
                    stat.setText("Disconnected");
                    stat.setTextColor(Color.rgb(199, 59, 59));
                    mPrint.setEnabled(false);
                    mScan.setEnabled(true);
                    mScan.setText("Connect");
                }
            }
        });






        mPrint = (Button) findViewById(R.id.mPrint);
        //mPrint.setTypeface(custom);
        mPrint.setOnClickListener(new View.OnClickListener() {
            public void onClick(View mView) {
                p1();
            }
        });
    }
    private void init(){
        database = new Database(this,"trasua.sqlite", null, 1);
        lvHoaDon=findViewById(R.id.listviewHoaDon);
        btnThanhToan=findViewById(R.id.buttonThanhToan);
        list_cthd=new ArrayList<>();
        Intent intent=getIntent();
        ban = new Ban();
        ban = (Ban) intent.getSerializableExtra("dulieuban");
        Cursor dataCTHD = database.GetData("SELECT * FROM CTHD WHERE MaHD='"+ban.getMaHD()+"'");
        while (dataCTHD.moveToNext()){
            list_cthd.add(new CTHD(dataCTHD.getString(0),dataCTHD.getInt(1),dataCTHD.getInt(2)));
            // sp=GetSP(dataCTHD.getInt(1));
        }
        adapter=new HoaDonAdapter(BillActivity.this,R.layout.dong_hoadon,list_cthd);
        lvHoaDon.setAdapter(adapter);
    }
    public SanPham GetSP(int masp){
        SanPham sp=new SanPham();
        Cursor dataSP = database.GetData("SELECT * FROM SanPham WHERE MaSP="+masp);
        while (dataSP.moveToNext()){
            sp=new SanPham(dataSP.getInt(0),dataSP.getString(1),dataSP.getInt(2),dataSP.getInt(3));
        }
        return sp;
    }
    public int tinhtongtien(String mahd){
        int tong=0;
        Cursor dataHD = database.GetData("SELECT * FROM CTHD WHERE MaHD='"+mahd+"'");
        while (dataHD.moveToNext()){
            SanPham sp=GetSP(dataHD.getInt(1));
            tong+=sp.getGia()*dataHD.getInt(2);
        }
        return tong;
    }

    public void DialogXoaSP(final int position){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(BillActivity.this);
        final CTHD cthd=list_cthd.get(position);
        SanPham sp = GetSP(cthd.getMaSP());
        alertDialog.setTitle("THÔNG BÁO");
        alertDialog.setIcon(R.drawable.thongbaoxoa);
        alertDialog.setMessage("Bạn có muốn xóa "+sp.getTenSP().toUpperCase()+"(x"+cthd.getSoLuong()+") không ?");
        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                database.QueryData("DELETE FROM CTHD WHERE MaHD='"+cthd.getMaHD()+"' AND MaSP='"+cthd.getMaSP()+"'");
                list_cthd.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
        alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }

    public void p1(){

        Thread t = new Thread() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void run() {
                try {
                    OutputStream os = mBluetoothSocket
                            .getOutputStream();

                    String he = "";
                    String copy = "";

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
                    Calendar calendar = Calendar.getInstance();
                    String time=simpleDateFormat.format(calendar.getTime());

                    he = "          TRA SUA HEM\n       "+time+"\nAddress: B58D Duong so 2, Ap My Hoa 2, Hoc Mon, Tp HCM\nHotline: 0933237072\n";
                    he = he +"________________________________\n\n";
                    os.write(he.getBytes());

                    String header =  String.format("%-23s%-3s%-8s\n","TEN","SL","TT");
                    os.write(header.getBytes());
                    for(CTHD cthd:list_cthd){
                        SanPham sp = GetSP(cthd.getMaSP());
                        String row = String.format("%-23s%-3s%-8s\n",sp.getTenSP(),cthd.getSoLuong()+"",(cthd.getSoLuong()*sp.getGia())+"");
                        os.write(row.getBytes());
                    }
                    String line="________________________________\n\n";
                    os.write(line.getBytes());
                    String total=String.format("%-21s%-12s\n","TOTAL",formatter.format(tinhtongtien(list_cthd.get(0).getMaHD())));
                    os.write(total.getBytes());
                    copy = "\n\n\n";
                    os.write(copy.getBytes());



                    //This is printer specific code you can comment ==== > Start

                    // Setting height
                    int gs = 29;
                    os.write(intToByteArray(gs));
                    int h = 104;
                    os.write(intToByteArray(h));
                    int n = 162;
                    os.write(intToByteArray(n));

                    // Setting Width
                    int gs_width = 29;
                    os.write(intToByteArray(gs_width));
                    int w = 119;
                    os.write(intToByteArray(w));
                    int n_width = 2;
                    os.write(intToByteArray(n_width));


                } catch (Exception e) {
                    Log.e("PrintActivity", "Exe ", e);
                }
            }
        };
        t.start();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        try {
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();
        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
    }


    public void onActivityResult(int mRequestCode, int mResultCode,
                                 Intent mDataIntent) {
        super.onActivityResult(mRequestCode, mResultCode, mDataIntent);

        switch (mRequestCode) {
            case REQUEST_CONNECT_DEVICE:
                if (mResultCode == Activity.RESULT_OK) {
                    Bundle mExtra = mDataIntent.getExtras();
                    String mDeviceAddress = mExtra.getString("DeviceAddress");
                    Log.v(TAG, "Coming incoming address " + mDeviceAddress);
                    mBluetoothDevice = mBluetoothAdapter
                            .getRemoteDevice(mDeviceAddress);
                    mBluetoothConnectProgressDialog = ProgressDialog.show(this,
                            "Connecting...", mBluetoothDevice.getName() + " : "
                                    + mBluetoothDevice.getAddress(), true, false);
                    Thread mBlutoothConnectThread = new Thread(this);
                    mBlutoothConnectThread.start();

                }
                break;

            case REQUEST_ENABLE_BT:
                if (mResultCode == Activity.RESULT_OK) {
                    ListPairedDevices();
                    Intent connectIntent = new Intent(BillActivity.this,
                            DeviceListActivity.class);
                    startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
                } else {
                    Toast.makeText(BillActivity.this, "Message", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + mRequestCode);
        }
    }

    private void ListPairedDevices() {
        Set<BluetoothDevice> mPairedDevices = mBluetoothAdapter
                .getBondedDevices();
        if (mPairedDevices.size() > 0) {
            for (BluetoothDevice mDevice : mPairedDevices) {
                Log.v(TAG, "PairedDevices: " + mDevice.getName() + "  "
                        + mDevice.getAddress());
            }
        }
    }

    public void run() {
        try {
            mBluetoothSocket = mBluetoothDevice
                    .createRfcommSocketToServiceRecord(applicationUUID);
            mBluetoothAdapter.cancelDiscovery();
            mBluetoothSocket.connect();
            mHandler.sendEmptyMessage(0);
        } catch (IOException eConnectException) {
            Log.d(TAG, "CouldNotConnectToSocket", eConnectException);
            closeSocket(mBluetoothSocket);
            return;
        }
    }

    private void closeSocket(BluetoothSocket nOpenSocket) {
        try {
            nOpenSocket.close();
            Log.d(TAG, "SocketClosed");
        } catch (IOException ex) {
            Log.d(TAG, "CouldNotCloseSocket");
        }
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mBluetoothConnectProgressDialog.dismiss();

            // Snackbar snackbar = Snackbar.make(layout, "Bluetooth Printer is Connected!", Snackbar.LENGTH_LONG);
            // snackbar.show();
            stat.setText("");
            stat.setText("Connected");
            stat.setTextColor(Color.rgb(97, 170, 74));
            mPrint.setEnabled(true);
            mScan.setText("Disconnect");
            //mDisc.setEnabled(true);
            //mDisc.setBackgroundColor(Color.rgb(0, 0, 0));
            //mScan.setEnabled(false);
            //mScan.setBackgroundColor(Color.rgb(161, 161, 161));

        }
    };

    public static byte intToByteArray(int value) {
        byte[] b = ByteBuffer.allocate(4).putInt(value).array();

        for (int k = 0; k < b.length; k++) {
            System.out.println("Selva  [" + k + "] = " + "0x"
                    + UnicodeFormatter.byteToHex(b[k]));
        }

        return b[3];
    }

    public byte[] sel(int val) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putInt(val);
        buffer.flip();
        return buffer.array();
    }
}


