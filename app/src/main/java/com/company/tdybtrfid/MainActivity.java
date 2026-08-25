package com.company.tdybtrfid;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Fragment;
import android.app.LocalActivityManager;
import android.app.TabActivity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;

import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bth.api.cls.Comm_Bluetooth;

import com.google.android.material.snackbar.Snackbar;
import com.silionmodule.DataListener;
import com.silionmodule.Functional;
import com.silionmodule.ParamNames;
import com.silionmodule.ReaderException;
import com.silionmodule.SimpleReadPlan;
import com.silionmodule.StatusEventListener;
import com.silionmodule.TAGINFO;
import com.silionmodule.TagProtocol.TagProtocolE;
import com.silionmodule.TagReadData;
import com.tool.log.LogD;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MainActivity extends BaseActivity  { // ActionBarActivity
	static {
		System.setProperty(
				"org.apache.poi.javax.xml.stream.XMLInputFactory",
				"com.fasterxml.aalto.stax.InputFactoryImpl"
		);
		System.setProperty(
				"org.apache.poi.javax.xml.stream.XMLOutputFactory",
				"com.fasterxml.aalto.stax.OutputFactoryImpl"
		);
		System.setProperty(
				"org.apache.poi.javax.xml.stream.XMLEventFactory",
				"com.fasterxml.aalto.stax.EventFactoryImpl"
		);
	}
	private static final int REQUEST_SELECT_DEVICE = 1;
	static Set<String> scanTags=new HashSet<>();
	private Handler mainHandler;
	private static Adapter adapter;
	private static Adapter2 adapter2;
	private static Adapter3 adapter3;
	private static RecyclerView LvTags;
	private RecyclerView.LayoutManager layoutManager;
	//private Context mContext;
	Intent sub1Intent;
	PlaceholderFragment fragment;
	static int acount=0;
	//List<ReadExcelModel> readExcelModels=new ArrayList<>();
	static List<ReadExcelModel> foundTags=new LinkedList<>();
	static Set<ReadExcelModel> foundAll=new LinkedHashSet<>();
	List<ReadExcelModel> models=new ArrayList<>();
	List<ReadExcelModel> excelModels=new ArrayList<>();
	static List<String> scanned;
	private java.lang.Thread runThread;
	private static final int FILE_PICKER_REQUEST_CODE=1;
	boolean isrun, issound = true;
	TextView tv_once, tv_state, tv_tags, tv_cost;
	ExpandableListView tab4_left, tab4_right;

	Button button_read, button_stop, button_clear;
	private ListView listView;
	Toolbar toolbar;

	Map<String, TAGINFO> TagsMap = new LinkedHashMap<String, TAGINFO>();
	private MyApplication myapp;
	private SoundPool soundPool, soundPoolerr;
	boolean isreading;
	RadioGroup gr_match;
	public static TabHost tabHost;
	public static TabSpec tab1, tab2, tab3, tab4_1, tab4_2, tab5;
	ScreenListener l;
	Lock lockobj = new ReentrantLock();
	String[] Coname;

	List<Map<String, ?>> ListMs = new ArrayList<Map<String, ?>>();
	MyAdapter Adapter;

	AndroidWakeLock Awl;
	static Set<ReadExcelModel> readExcelModels=new LinkedHashSet<>();
	private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
	private static String fileType = "";
	private static String extensionXLS = "XLS";
	private static String extensionXLSX = "XLSX";
	private View mLayout;

	private static final int PERMISSION_REQUEST_MEMORY_ACCESS = 0;
	ActivityResultLauncher<Intent> filePicker;//
	private Button claimDiscountButton;
	private TextView countdownTimer;


	public enum Region_Conf {
		RG_NONE(0x0), RG_NA(0x01), RG_EU(0x02), RG_EU2(0X07), RG_EU3(0x08), RG_KR(
				0x03), RG_PRC(0x06), RG_PRC2(0x0A), RG_OPEN(0xFF);

		int p_v;

		Region_Conf(int v) {
			p_v = v;
		}

		public int value() {
			return this.p_v;
		}

		public static Region_Conf valueOf(int value) {
			switch (value) {
			case 0:
				return RG_NONE;
			case 1:
				return RG_NA;
			case 2:
				return RG_EU;
			case 7:
				return RG_EU2;
			case 8:
				return RG_EU3;
			case 3:
				return RG_KR;
			case 6:
				return RG_PRC;
			case 0x0A:
				return RG_PRC2;
			case 0xff:
				return RG_OPEN;
			}
			return null;
		}
	}

	public Handler handler2 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0: {
				// showlist();
			//	Adapter.notifyDataSetChanged();
				try {
					adapter.notifyDataSetChanged();
					adapter2.notifyDataSetChanged();
					adapter3.notifyDataSetChanged();
				}catch (NullPointerException e){
					e.printStackTrace();
				}
				Bundle bd = msg.getData();
				TextView et = (TextView) findViewById(R.id.textView_readoncecnt);
				et.setText(String.valueOf(bd.get("OnceCount")));
				TextView et2 = (TextView) findViewById(R.id.textView_readallcnt);
				et2.setText(String.valueOf(TagsMap.size()));
				break;
			}
			case 1: {
				Bundle bd = msg.getData();

				TextView et = (TextView) findViewById(R.id.textView_invstate);
				if (et != null)
					et.setText(" " + bd.get("Msg"));

				if (myapp.CommBth.ConnectState() != Comm_Bluetooth.CONNECTED) {
					if (et != null)
						et.setText("disconnect...reconnect...");
					myapp.CommBth.ReConnect();

				}
				break;
			}
			}
		}
	};

	public Handler handler3 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Bundle bd = msg.getData();
			switch (msg.what) {
			case 0: {
				String count = bd.get("Msg_cnt").toString();
				tv_once.setText(count);
				tv_tags.setText(bd.get("Msg_all").toString());
				tv_cost.setText(bd.get("Msg_time").toString());
				//Adapter.notifyDataSetChanged();
				try {
					adapter.notifyDataSetChanged();
					adapter2.notifyDataSetChanged();
					adapter3.notifyDataSetChanged();
				}catch (NullPointerException e){
					e.printStackTrace();
				}
				break;

			}
			case 1: {
				button_read.setText(MyApplication.Constr_READ);
				tv_state.setText(bd.get("Msg_error_1").toString());
				//soundPoolerr.play(1, 1, 1, 0, 0, 1);
				break;
			}
			case 2: {
				tv_state.setText(bd.get("Msg_error_2").toString());
				//soundPoolerr.play(1, 1, 1, 0, 0, 1);
				break;
			}

			}
		}
	};

	StatusEventListener SL = new StatusEventListener() {

		@Override
		public void StatusCatch(Object t) {
			// TODO Auto-generated method stub

			Message msg = new Message();
			msg.what = 1;
			Bundle bundle = new Bundle();
			bundle.putString("Msg", (String) t);
			msg.setData(bundle);

			handler2.sendMessage(msg);
		}

	};

	DataListener DL = new DataListener() {

		@Override
		public void ReadData(TagReadData[] t) {
			// TODO Auto-generated method stub

			TagReadData[] trds = t;
			if (trds != null && trds.length > 0) {
				//soundPool.play(1, 1, 1, 0, 0, 1);
				for (int i = 0; i < trds.length; i++) {
					if (!TagsMap.containsKey(trds[i].EPCHexstr())) {
						TAGINFO Ti = new TAGINFO();
						Ti.AntennaID = (byte) trds[i].Antenna();
						Ti.CRC = trds[i].CRC();
						Ti.EmbededData = trds[i].AData();
						Ti.EmbededDatalen = (short) trds[i].AData().length;
						Ti.EpcId = trds[i].EPCbytes();
						Log.d("myApp", Arrays.toString(Ti.EpcId));
						System.out.println("myApp"+ Arrays.toString(Ti.EpcId));
						Ti.Epclen = (short) trds[i].EPCbytes().length;
						Ti.Frequency = trds[i].Frequency();
						Ti.PC = trds[i].PC();
						Ti.protocol = -1;
						Ti.ReadCnt = trds[i].ReadCount();
						Ti.RSSI = trds[i].RSSI();
						Ti.TimeStamp = (int) trds[i].Time().getTime();
						TagsMap.put(trds[i].EPCHexstr(), Ti);

						// list
						Map<String, String> m = new HashMap<String, String>();
						m.put(Coname[0], String.valueOf(TagsMap.size()));

						String epcstr = Functional.bytes_Hexstr(Ti.EpcId);
						Log.d("MYAPP","epcstr"+epcstr);
						scanTags.add(epcstr);
						if (epcstr.length() < 24)
							epcstr = String.format("%-24s", epcstr);
						Log.d("String","mmmm"+epcstr);
						m.put(Coname[1], epcstr);
						String cs = m.get("����");
						if (cs == null)
							cs = "0";
						int isc = Integer.parseInt(cs) + Ti.ReadCnt;

						m.put(Coname[2], String.valueOf(isc));
						m.put(Coname[3], String.valueOf(Ti.AntennaID));
						m.put(Coname[4], "");
						m.put(Coname[5], String.valueOf(Ti.RSSI));
						m.put(Coname[6], String.valueOf(Ti.Frequency));

						if (Ti.EmbededDatalen > 0) {
							byte[] out = new byte[Ti.EmbededDatalen];
							System.arraycopy(Ti.EmbededData, 0, out, 0,
									Ti.EmbededDatalen);

							m.put(Coname[7], Functional.bytes_Hexstr(out));
						} else
							m.put(Coname[7], "                 ");

						ListMs.add(m);

					} else {
						TAGINFO tf = TagsMap.get(trds[i].EPCHexstr());
						tf.ReadCnt += trds[i].ReadCount();
						tf.RSSI = trds[i].RSSI();
						tf.Frequency = trds[i].Frequency();

						String epcstr = trds[i].EPCHexstr();
						scanTags.add(epcstr);
						if (epcstr.length() < 24)
							epcstr = String.format("%-24s", epcstr);

						for (int k = 0; k < ListMs.size(); k++) {
							@SuppressWarnings("unchecked")
							Map<String, String> m = (Map<String, String>) ListMs
									.get(k);
							if (m.get(Coname[1]).equals(epcstr)) {

								m.put(Coname[2], String.valueOf(tf.ReadCnt));
								m.put(Coname[5], String.valueOf(tf.RSSI));
								m.put(Coname[6], String.valueOf(tf.Frequency));
								break;
							}
						}
					}
				}
				compareTags();
			}

			Message msg = new Message();
			msg.what = 0;
			Bundle bundle = new Bundle();
			bundle.putInt("OnceCount", trds.length);
			msg.setData(bundle);
			// ������Ϣ��Handler
			handler2.sendMessage(msg);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mainHandler = new Handler(Looper.getMainLooper());

//		button_read.setOnClickListener(this);
		checkReadWritePermission();
		Application app = getApplication();
		myapp = (MyApplication) app;

		setLange();
		
	//	soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
	//	soundPool.load(this, R.raw.beep, 1);

	//	soundPoolerr = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
		//soundPoolerr.load(this, R.raw.alarm, 1);

		Awl = new AndroidWakeLock(
				(PowerManager) getSystemService(Context.POWER_SERVICE));
		Awl.WakeLock();

		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		mLayout=findViewById(R.id.main_layout);
		LocalActivityManager localActivityManager = new LocalActivityManager(this, false);
		localActivityManager.dispatchCreate(savedInstanceState);
		tabHost.setup(localActivityManager);
		tabHost.setup();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			toolbar=(Toolbar)findViewById(R.id.toolbar);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			toolbar.setTitle("JWRT");
			setSupportActionBar(toolbar);
		}
//		tab1 = tabHost
//				.newTabSpec("tab1")
//				.setIndicator(MyApplication.Constr_CONNECT,
//						getResources().getDrawable(R.drawable.ic_launcher))
//				.setContent(new Intent(this, Sub1TabActivity.class));
//		tabHost.addTab(tab1);
		tab2 = tabHost.newTabSpec("tab2")
				.setIndicator(MyApplication.Constr_INVENTORY)
				.setContent(R.id.tab2);
		tabHost.addTab(tab2);
		tab3 = tabHost
				.newTabSpec("tab3")
				.setIndicator(
						MyApplication.Constr_RWLOP,
						getResources().getDrawable(
								android.R.drawable.arrow_down_float))
				.setContent(new Intent(this, Sub3TabActivity.class));

		tab4_1 = tabHost
				.newTabSpec("tab4")
				.setIndicator(
						MyApplication.Constr_PASSVICE,
						getResources().getDrawable(
								android.R.drawable.arrow_down_float))
				.setContent(new Intent(this, Sub4TabActivity.class));
		tab4_2 = tabHost
				.newTabSpec("tab5")
				.setIndicator(
						MyApplication.Constr_ACTIVE,
						getResources().getDrawable(
								android.R.drawable.arrow_down_float))
				.setContent(new Intent(this, SubBlueSetTabActivity.class));

		tabHost.setCurrentTab(0);
		TabWidget tw = tabHost.getTabWidget();
//		tw.getChildAt(1).setVisibility(View.INVISIBLE);

		/*
		   Region_Conf rcf1=Region_Conf.valueOf(Integer.valueOf("8")); byte[]
		   data=new byte[1]; data[0]=(byte)((Region_Conf)rcf1).value();
		   System.out.println(String.valueOf(data[0]));
		 */
 
		// myapp.CommBth = new Comm_Bluetooth(this);
		myapp.Mact = this;
		l = new ScreenListener(this);
		l.begin(new ScreenListener.ScreenStateListener() {

			@Override
			public void onScreenOn() {
				if (myapp.CommBth != null) {

					// LogD.LOGD("init bluetooth");
					// myapp.CommBth = new Comm_Bluetooth(myapp.Mact);

				}
			}

			@Override
			public void onScreenOff() {

				Log.d("MYINFO", "onScreenoff");
				/*
				   if(button_stop.isEnabled()) { button_stop.performClick();}
				   
				   if(myapp.Mreader!=null) myapp.Mreader.DisConnect();
				   
				   if(myapp.CommBth!=null) { myapp.CommBth.Comm_Close();
				   
				   } StopHandleUI(); TextView et = (TextView)
				   findViewById(R.id.textView_invstate); if (et != null)
				   et.setText("disconnect...Please to reconnect...");
				 */
			}
		});

		Coname = MyApplication.Coname;
		myapp.Rparams = myapp.new ReaderParams();
		myapp.tabHost = tabHost;
		/*
		   spinner_opbank= (Spinner)findViewById(R.id.spinner_opfbank);
		   arradp_opbank = new
		   ArrayAdapter<String>(this,android.R.layout.simple_spinner_item
		   ,spibank); arradp_opbank.setDropDownViewResource(android.R.layout.
		   simple_spinner_dropdown_item);
		   spinner_opbank.setAdapter(arradp_opbank);
		 */

	//	compareTags();
		button_read = (Button) findViewById(R.id.button_start);
		button_stop = (Button) findViewById(R.id.button_stop);
		button_stop.setEnabled(false);
		button_clear = (Button) findViewById(R.id.button_readclear);
		LvTags = (RecyclerView) findViewById(R.id.LvTags);
		layoutManager = new LinearLayoutManager(this); // Initialize the LayoutManager
		LvTags.setLayoutManager(layoutManager);
		
		adapter3 = new Adapter3(this, foundTags);
		LvTags.setAdapter(adapter3);
//		listView = (ListView) findViewById(R.id.listView_epclist);
		gr_match = (RadioGroup) findViewById(R.id.radioGroup_opmatch);

		tv_once = (TextView) findViewById(R.id.textView_readoncecnt);
		tv_state = (TextView) findViewById(R.id.textView_invstate);
		tv_tags = (TextView) findViewById(R.id.textView_readallcnt);
		tv_cost = (TextView) findViewById(R.id.textView_cost);
		claimDiscountButton = findViewById(R.id.claimDiscountButton);
		claimDiscountButton.setBackgroundColor(Color.rgb(229,14,75));
		countdownTimer = findViewById(R.id.countdownTimer);
		View rootView = findViewById(android.R.id.content);
		rootView.invalidate();

		for (int i = 0; i < Coname.length; i++)
			h.put(Coname[i], Coname[i]);

		if(foundTags.size()>0){
			claimDiscountButton.setVisibility(View.VISIBLE);
		}

		claimDiscountButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Handle the button click action (e.g., disable the button and start countdown)
				countdownTimer.setVisibility(View.VISIBLE);
				startCountdown(view);
			}
		});

		// Set the initial countdown timer value
	countdownTimer.setText("2:00");
		button_read.setOnClickListener(new OnClickListener() {
	@Override
	public void onClick(View view) {
		if (readExcelModels.size() == 0) {
			displayAlert("Warning!","Kindly import Excel");
		} else {


			try {
				TextView et = (TextView) findViewById(R.id.textView_invstate);
				if (et != null)
					et.setText("...");

//					if (Adapter == null) {
//						Map<String, String> h = new HashMap<String, String>();
//						for (int i = 0; i < Coname.length; i++)
//							h.put(Coname[i], Coname[i]);
//						Log.d("myApp","adapter"+h);
//						ListMs.add(h);
//						Adapter = new MyAdapter(getApplicationContext(),
//								ListMs, R.layout.listitemview_inv, Coname,
//								new int[] { R.id.textView_readsort,
//										R.id.textView_readepc,
//										R.id.textView_readcnt,
//										R.id.textView_readant,
//										R.id.textView_readpro,
//										R.id.textView_readrssi,
//										R.id.textView_readfre,
//										R.id.textView_reademd });
//
//						listView.setAdapter(Adapter);
//					}

				myapp.Mreader.addStatusListener(SL);
//compareTags();
				if (myapp.Mode == 0) {
					SimpleReadPlan srp = new SimpleReadPlan(
							myapp.Rparams.uants);

					try {

						if (myapp.Rparams.To != null
								|| myapp.Rparams.Tf != null) {
							srp = new SimpleReadPlan(myapp.Rparams.uants,
									TagProtocolE.Gen2, myapp.Rparams.Tf,
									myapp.Rparams.To, 10);
						}

						myapp.Mreader.paramSet(ParamNames.Reader_Read_Plan,
								srp);

						/* ȡ��������� */
						myapp.Mreader.paramSet(
								ParamNames.Reader_Antenna_CheckPort, false);

						isrun = true;
						runThread = new java.lang.Thread(runnable);
						runThread.start();
//							compareTags();

						isreading = true;
						myapp.isread = true;
						ReadHandleUI();

					} catch (ReaderException e) {
						Toast.makeText(
								MainActivity.this,
								MyApplication.Constr_SetFaill
										+ e.GetMessage(),
								Toast.LENGTH_SHORT).show();
						return;
					}
				} else if (myapp.Mode == 1) {
					try {
						myapp.Mreader.addDataListener(DL);
						myapp.Mreader.StartTagEvent();
//							compareTags();
						myapp.isread = true;
						isreading = true;
						ReadHandleUI();

					} catch (ReaderException e) {
						// TODO Auto-generated catch block
						Toast.makeText(
								MainActivity.this,
								MyApplication.Constr_SetFaill
										+ e.GetMessage(),
								Toast.LENGTH_SHORT).show();
						return;
					}
				}
			} catch (Exception ex) {
				Toast.makeText(MainActivity.this,
						MyApplication.Constr_SetFaill + ex.getMessage(),
						Toast.LENGTH_SHORT).show();
			}

		}
	}
}
);

		button_stop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				StopHandle();
				myapp.isread = false;
				myapp.TagsMap.putAll(TagsMap);
				myapp.scanTags.addAll(scanTags);
				myapp.foundTags.addAll(foundTags);
				myapp.foundAll.addAll(foundAll);
			}
		});


			button_clear.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// Reset UI elements and clear data
					if (button_stop.isEnabled()) {
						Toast.makeText(MainActivity.this, "Please stop scanning and then clear", Toast.LENGTH_SHORT).show();
					} else {
						clearDataAndUI();

						// Restart the activity
						restartActivity();
					}
				}
			});




//		this.listView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				// TODO Auto-generated method stub
//				arg1.setBackgroundColor(Color.YELLOW);
//
//				EditText et = (EditText) findViewById(R.id.editText_opfilterdata);
//				EditText et2 = (EditText) findViewById(R.id.editText_opfilsadr);
//				HashMap<String, String> hm = (HashMap<String, String>) listView
//						.getItemAtPosition(arg2);
//				String epc = hm.get("EPC ID");
//				Log.d("myApp",epc);
//				System.out.println("myApp"+epc);
//				myapp.Rparams.Curepc = epc.trim();
//				// et.setText(epc);
//				// et2.setText("32");
//				// gr_match.check(gr_match.getChildAt(0).getId());
//				// spinner_opbank.setSelection(1);
//
//				for (int i = 0; i < listView.getCount(); i++) {
//					if (i != arg2) {
//						View v = listView.getChildAt(i);
//						if (v != null) {
//							ColorDrawable cd = (ColorDrawable) v
//									.getBackground();
//							if (Color.YELLOW == cd.getColor()) {
//								int[] colors = { Color.WHITE,
//										Color.rgb(219, 238, 244) };// RGB��ɫ
//								v.setBackgroundColor(colors[i % 2]);// ÿ��item֮����ɫ��ͬ
//							}
//						} else {
//							break;
//						}
//					}
//				}
//			}
//
//		});

		tabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String arg0) {
				int j = tabHost.getCurrentTab();
				if (tabHost.getTabWidget().getChildCount() == 4) {
					if (j == 2) {
						Sub3TabActivity.EditText_sub3fildata
								.setText(myapp.Rparams.Curepc);
						Sub3TabActivity.EditText_sub3wdata
								.setText(myapp.Rparams.Curepc);
					} else if (j == 1) {
						if (myapp.connectok) {
							myapp.connectok = false;
							tv_state.setText("connect sucessful");
						}
					}
				}

			}
		});
		setLange();
		//*
		   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // Android MPermission check
		  if
		   (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
		  { requestPermissions(new
		   String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
		   PERMISSION_REQUEST_COARSE_LOCATION); }
		   } //
		// */
		filePicker = registerForActivityResult(//
				new ActivityResultContracts.StartActivityForResult(),
				result -> {
					if (result.getResultCode() == Activity.RESULT_OK) {

						Intent intent1 = result.getData();

						Uri uri = intent1.getData();
						ReadExcelFile(this,uri);

					}
				});
	}

	public void startCountdown(View view) {
		new CountDownTimer(120000, 1000) { // 120000 ms (2 minutes) with 1000 ms (1 second) interval
			public void onTick(long millisUntilFinished) {
				// Update the countdown timer text with the remaining time
				long minutes = millisUntilFinished / 60000;
				long seconds = (millisUntilFinished % 60000) / 1000;
				String timeLeft = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
				countdownTimer.setText(timeLeft);
			}

			public void onFinish() {
// Countdown timer finished, you can perform any desired action here
				countdownTimer.setText("00:00"); // Display 00:00 when the timer is done
				claimDiscountButton.setVisibility(View.GONE);
				countdownTimer.setVisibility(View.GONE);
			}
		}.start();
	}
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//
//		if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
//			Uri uri = data.getData();
//			// Call a method to handle the picked file
//
//		}
//	}
//@Override
//public void onClick(View view) {
//	switch (view.getId()){
//		case R.id.action_import_xls:
//			Read();
//			break;
//	}
//}
//		this.listView.setOnItemClickListener(new OnItemClickListener() {
public void displayAlert(String title, String msg) {
	AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this); // Use activity context
	builder.setTitle(title);
	builder.setMessage(msg);
	builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
		}
	});
	builder.create().show(); // Create and show the AlertDialog
}

	private void clearDataAndUI() {
		// Clear data structures
		scanTags.clear();
		foundTags.clear();
		foundAll.clear();
		TagsMap.clear();
		ListMs.clear();

		// Clear data in another instance of myapp
		myapp.TagsMap.clear();
		myapp.scanTags.clear();
		myapp.foundTags.clear();
		myapp.foundAll.clear();

		// Add h to ListMs
		ListMs.add(h);

		// Update adapters (if you need to)
		updateAdapters();

		// Reset TextViews
		TextView et = findViewById(R.id.textView_readoncecnt);
		et.setText("0");

		TextView et2 = findViewById(R.id.textView_readallcnt);
		et2.setText("0");

		TextView et3 = findViewById(R.id.textView_invstate);
		et3.setText("...");

		TextView et4 = findViewById(R.id.textView_cost);
		et4.setText("0");

		// Reset myapp's Curepc
		myapp.Rparams.Curepc = "";
	}

	private void updateAdapters() {
		// Update your adapters here (if applicable)
		// adapter.notifyDataSetChanged();
		// adapter2.notifyDataSetChanged();
		// adapter3.notifyDataSetChanged();
	}

	private void restartActivity() {
		// Restart the activity to reflect the changes
		/*Intent intent = getIntent();
		finish();
		overridePendingTransition(0, 0);
		startActivity(intent);
		overridePendingTransition(0, 0);*/
		// You might want to adjust transitions here
		//this.recreate();
		Intent refresh = new Intent(this, MainActivity.class);
		refresh.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(refresh);
		this.finish();
	}
	/*@Override
	public void onResume(){
		super.onResume();
		//here...

	}*/


	public void onRequestPermissionsResult(int requestCode,
			String permissions[], int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
			case PERMISSION_REQUEST_COARSE_LOCATION:
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// TODO request success
				}
				break;
			case PERMISSION_REQUEST_MEMORY_ACCESS:
				if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					OpenFilePicker();
				} else {
					Snackbar.make(mLayout, "Please grant access to memory permission",
									Snackbar.LENGTH_SHORT)
							.show();
				}break;
		}
		}
	private void requestStoragePermission() {//

		if (ActivityCompat.checkSelfPermission(this,
				Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {


			ActivityCompat.requestPermissions(MainActivity.this,
					new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
					PERMISSION_REQUEST_MEMORY_ACCESS);

		} else {
			Snackbar.make(mLayout, R.string.storage_unavailable, Snackbar.LENGTH_SHORT).show();
			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_MEMORY_ACCESS);
		}
	}
	public void ChooseFile() {//
		try {
			Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
			fileIntent.addCategory(Intent.CATEGORY_OPENABLE);

			if (fileType == extensionXLS)
				fileIntent.setType("application/vnd.ms-excel");
			else
				fileIntent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

			filePicker.launch(fileIntent);
		} catch (Exception ex) {
			//Toast("ChooseFile error: " + ex.getMessage().toString(), ex);

		}
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - myapp.exittime) > 2000) {
				Toast.makeText(getApplicationContext(),
						MyApplication.Constr_Putandexit, Toast.LENGTH_SHORT)
						.show();
				myapp.exittime = System.currentTimeMillis();
			} else {
				finish();
				// System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	void StopHandle() {
		isreading = false;
		if (myapp.Mreader != null)
			myapp.Mreader.removeStatusListener(SL);
		if (myapp.Mode == 0) {

			isrun = false;
			try {
				if (runThread != null)
					runThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			StopHandleUI();
		} else {
			try {
				if (myapp.Mreader != null) {
					myapp.Mreader.EndTagEvent();
					myapp.Mreader.removeDataListener(DL);
				}
				StopHandleUI();
			} catch (ReaderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	Map<String, String> h = new HashMap<String, String>();

	private Runnable runnable = new Runnable() {
		public void run() {

			while (isrun) {
				synchronized (this) {
					if (isreading) {
						if (myapp.CommBth.ConnectState() == Comm_Bluetooth.CONNECTED) {
							TagReadData[] trds = null;
							long st = System.currentTimeMillis();
							try {
								trds = myapp.Mreader
										.Read(myapp.Rparams.readtime);
								/*
								   LogD.LOGD("read cost time:" +
								   String.valueOf(System .currentTimeMillis() -
								   st));
								 */
								if (trds != null && trds.length > 0) {
									if (issound)
										//soundPool.play(1, 1, 1, 0, 0, 1);
									for (int i = 0; i < trds.length; i++) {

										if (!TagsMap.containsKey(trds[i]
												.EPCHexstr())) {
											TAGINFO Ti = new TAGINFO();
											Ti.AntennaID = (byte) trds[i]
													.Antenna();
											Ti.CRC = trds[i].CRC();
											Ti.EmbededData = trds[i].AData();
											Ti.EmbededDatalen = (short) trds[i]
													.AData().length;
											Ti.EpcId = trds[i].EPCbytes();
											Ti.Epclen = (short) trds[i]
													.EPCbytes().length;
											Ti.Frequency = trds[i].Frequency();
											Ti.PC = trds[i].PC();
											Ti.protocol = -1;
											Ti.ReadCnt = trds[i].ReadCount();
											Ti.RSSI = trds[i].RSSI();
											Ti.TimeStamp = (int) trds[i].Time()
													.getTime();
											TagsMap.put(trds[i].EPCHexstr(), Ti);

											// list
											Map<String, String> m = new HashMap<String, String>();
											m.put(Coname[0], String
													.valueOf(TagsMap.size()));

											String epcstr = Functional
													.bytes_Hexstr(Ti.EpcId);
											scanTags.add(epcstr);
											compareTags();
											if (epcstr.length() < 24)
												epcstr = String.format("%-24s",
														epcstr);

											m.put(Coname[1], epcstr);
											String cs = m.get("����");
											if (cs == null)
												cs = "0";
											int isc = Integer.parseInt(cs)
													+ Ti.ReadCnt;

											m.put(Coname[2],
													String.valueOf(isc));
											m.put(Coname[3], String
													.valueOf(Ti.AntennaID));
											m.put(Coname[4], "");
											m.put(Coname[5],
													String.valueOf(Ti.RSSI));
											m.put(Coname[6], String
													.valueOf(Ti.Frequency));

											if (Ti.EmbededDatalen > 0) {
												byte[] out = new byte[Ti.EmbededDatalen];
												System.arraycopy(
														Ti.EmbededData, 0, out,
														0, Ti.EmbededDatalen);

												m.put(Coname[7], Functional
														.bytes_Hexstr(out));
											} else
												m.put(Coname[7],
														"                 ");

											ListMs.add(m);

										} else {

											TAGINFO tf = TagsMap.get(trds[i]
													.EPCHexstr());
											tf.ReadCnt += trds[i].ReadCount();
											tf.RSSI = trds[i].RSSI();
											tf.Frequency = trds[i].Frequency();
											tf.AntennaID = (byte) trds[i]
													.Antenna();

											String epcstr = trds[i].EPCHexstr();
											compareTags();
											if (epcstr.length() < 24)
												epcstr = String.format("%-24s",
														epcstr);

											for (int k = 0; k < ListMs.size(); k++) {
												@SuppressWarnings("unchecked")
												Map<String, String> m = (Map<String, String>) ListMs
														.get(k);
												if (m.get(Coname[1]).equals(
														epcstr)) {

													m.put(Coname[2],
															String.valueOf(tf.ReadCnt));
													m.put(Coname[5], String
															.valueOf(tf.RSSI));
													m.put(Coname[6],
															String.valueOf(tf.Frequency));
													break;
												}
											}
										}

										// ���˶�tid----------------
										/*
										   int count=0; while(true) { try {
										   byte[] rdata = new byte[12];
										   Gen2TagFilter g2tf=null; byte[]
										   rpaswd = new byte[4]; byte[] fdata =
										   Functional
										   .hexstr_Bytes(trds[i].EPCHexstr());
										   
										   g2tf = new
										   Gen2TagFilter(MemBankE.EPC, 32,
										  fdata, fdata.length*8);
										  myapp.Mreader.
										  paramSet(ParamNames.Reader_Tagop_Antenna
										  ,trds[i].Antenna()); short[]
										  epddata=myapp
										  .Mreader.ReadTagMemWords(g2tf,
										  MemBankE.TID, 0,4);
										  
										  TAGINFO tf2 = TagsMap.get(trds[i]
										  .EPCHexstr());
										  tf2.EmbededData=Functional
										  .hexstr_Bytes
										  (Functional.shorts_HexStr(epddata));
										  tf2.EmbededDatalen=(short)
										  tf2.EmbededData.length; } catch
										  (ReaderException ex) {
										  LogD.LOGD(ex.GetMessage()); }
										  if(count++>3) break; } //
										 */

									}
									compareTags();
								}
//								compareTags();
							} catch (ReaderException rex) {
								Message msg2 = new Message();
								msg2.what = 1;
								Bundle bundle2 = new Bundle();
								bundle2.putString("Msg_error_1",
										"error:" + rex.GetMessage());
								msg2.setData(bundle2);
								handler3.sendMessage(msg2);
								// isrun = false;
								LogD.LOGD(rex.GetMessage());

							} catch (Exception ex) {

								Message msg = new Message();
								msg.what = 2;
								Bundle bundle = new Bundle();
								bundle.putString(
										"Msg_error_2",
										"error:" + ex.toString()
												+ ex.getMessage());
								msg.setData(bundle);
								LogD.LOGD(ex.toString() + ex.getMessage());
								handler3.sendMessage(msg);
								try {
									java.lang.Thread.sleep(1000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								continue;

							}

							if (trds != null && trds.length > 0) {
								Message msg = new Message();
								Bundle bundle = new Bundle();
								msg.what = 0;
								bundle.putString("Msg_cnt",
										(String.valueOf(trds.length)));
								int vl = (int) (System.currentTimeMillis() - st);
								bundle.putString("Msg_time",
										(String.valueOf(vl)));
								synchronized (this) {
									bundle.putString("Msg_all",
											(String.valueOf(TagsMap.size())));
								}
								msg.setData(bundle);
								handler3.sendMessage(msg);
//								compareTags();
							}
							try {
								java.lang.Thread.sleep(myapp.Rparams.sleep);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					} else {
						try {
							java.lang.Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

				}
			}

		}

	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;

	}

	@SuppressLint("NonConstantResourceId")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		if (myapp.isread) {
			Toast.makeText(MainActivity.this, MyApplication.Constr_stopscan,
					Toast.LENGTH_SHORT).show();
			return false;
		}
		int id = item.getItemId();

		if (id == R.id.action_debug) {

			if (myapp.m != null
					&& myapp.CommBth.ConnectState() == Comm_Bluetooth.CONNECTED) {
				Intent intent = new Intent(MainActivity.this,
						SubDebugActivity.class);
				startActivityForResult(intent, 0);
				return true;
			}
			Toast.makeText(MainActivity.this,
					MyApplication.Constr_scanselectabluereaderandconnect,
					Toast.LENGTH_SHORT).show();
			return false;
		} else if (id == R.id.action_system) {
			if (myapp.m != null) {
				Intent intent = new Intent(MainActivity.this,
						SubSystemActivity.class);
				startActivityForResult(intent, 0);
				return true;
			}

			Toast.makeText(MainActivity.this,
					MyApplication.Constr_scanselectabluereader,
					Toast.LENGTH_SHORT).show();
			return true;
		} else if (id == R.id.action_custom) {
			if (myapp.m != null
					&& myapp.CommBth.ConnectState() == Comm_Bluetooth.CONNECTED) {
				Intent intent = new Intent(MainActivity.this,
						SubCustomActivity.class);
				startActivityForResult(intent, 0);
				return true;
			}
			Toast.makeText(MainActivity.this,
					MyApplication.Constr_scanselectabluereaderandconnect,
					Toast.LENGTH_SHORT).show();
			return false;
		}
		// Retrieve the ID of the selected menu item
		if(id==R.id.btn_search){
			sub1Intent = new Intent(this, Sub1TabActivity.class);

   /*// Create the TabSpec
   tab1 = tabHost
         .newTabSpec("tab1")
         .setIndicator(MyApplication.Constr_CONNECT,
               getResources().getDrawable(R.mipmap.ic_launcher))
         .setContent(sub1Intent);*/ // Set the content to the intent
			startActivityForResult(sub1Intent,REQUEST_SELECT_DEVICE);
		}else if(id==R.id.button_disconnect){
			LogD.LOGD("disconnect_1");
			myapp.CommBth.StopSearch();
			//button_search.setText(MyApplication.Constr_search);
			if (myapp.Mreader != null) {
				LogD.LOGD("disconnect_2");
				myapp.Mreader.DisConnect();
			}
			//DisConnectHandleUI();
		}
		if (id == R.id.action_import_xls) {
			fileType = extensionXLS; // Use the appropriate file extension
			OpenFilePicker();
		} else if (id == R.id.action_import_xlxs) {
			fileType = extensionXLSX; // Use the appropriate file extension
			OpenFilePicker();
		}

		return super.onOptionsItemSelected(item);
	}
	public void OpenFilePicker() {//
		try {
			if (CheckPermission()) {
				ChooseFile();
			}
		} catch (ActivityNotFoundException e) {
			Toast.makeText(this, "No activity can handle picking a file. Showing alternatives.", Toast.LENGTH_SHORT).show();
		}

	}

	private boolean CheckPermission() {//
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
				== PackageManager.PERMISSION_GRANTED) {
			return true;
		} else {
			Snackbar.make(mLayout, R.string.storage_access_required,
					Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					requestStoragePermission();
				}
			}).show();


			return false;
		}
	}
	private void checkReadWritePermission() {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

			if (!Environment.isExternalStorageManager()) {
				Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
				intent.setData(Uri.parse("package:" + getPackageName()));
				startActivityForResult(intent, 0);
				finish();
			}
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
				requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
			}
			if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
				requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
			}
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	private void ReadHandleUI() {

		this.button_read.setEnabled(false);
		this.button_stop.setEnabled(true);

	//	TabWidget tw = myapp.tabHost.getTabWidget();
	//	tw.getChildAt(0).setEnabled(false);
	//	tw.getChildAt(2).setEnabled(false);
	//	if (myapp.Mode == 0)
	//		tw.getChildAt(3).setEnabled(false);

	}

	private void StopHandleUI() {
		button_read.setEnabled(true);
		button_stop.setEnabled(false);
//		TabWidget tw = myapp.tabHost.getTabWidget();
//		tw.getChildAt(0).setEnabled(true);
//		if (tw.getChildCount() > 2)
//			tw.getChildAt(2).setEnabled(true);
//		if (tw.getChildCount() > 3 && myapp.Mode == 0)
//			tw.getChildAt(3).setEnabled(true);
	}

	/*
	 * protected void onPause() {
	 * 
	 * long now=System.currentTimeMillis();
	 * if(!(myapp.exittime<now&&now-myapp.exittime<2000)) { myapp.exittime=now;
	 * Toast.makeText(MainActivity.this, "�ٰ�һ���˳�", Toast.LENGTH_SHORT).show();
	 * return; }
	 * 
	 * super.onPause(); }
	 * 
	 * protected void onResume() { //this.setVisible(true);
	 * 
	 * super.onResume(); }
	 */

	/*protected void onDestroy() {

		if (button_read.isEnabled())
			StopHandle();

		if (myapp.Mreader != null)
			myapp.Mreader.DisConnect();

		if (myapp.CommBth.getRemoveType() == 4
				&& myapp.CommBth.ConnectState() != Comm_Bluetooth.DISCONNECTED)
			myapp.CommBth.DisConnect();

		Awl.ReleaseWakeLock();
		//System.exit(0);
		super.onDestroy();
	}*/
	
	/**
	 * ����android ƽ̨������ʾ����
	 */
	private void setLange() {
		// Toast.makeText(getApplicationContext(), "setl1",
		// Toast.LENGTH_SHORT).show();
		Locale locale = getApplicationContext().getResources()
				.getConfiguration().locale;
		String language = locale.getLanguage();
		MyApplication.Constr_READ = this.getString(R.string.Constr_READ);
		MyApplication.Constr_CONNECT = this.getString(R.string.Constr_CONNECT);
		MyApplication.Constr_INVENTORY = this
				.getString(R.string.Constr_INVENTORY);
		MyApplication.Constr_RWLOP = this.getString(R.string.Constr_RWLOP);
		 
		MyApplication.Constr_PASSVICE=this.getString(R.string.Constr_PASSVICE);
		MyApplication.Constr_ACTIVE=this.getString(R.string.Constr_ACTIVE);
		
		MyApplication.Constr_SetFaill = this
				.getString(R.string.Constr_SetFaill);
		MyApplication.Constr_GetFaill = this
				.getString(R.string.Constr_GetFaill);
		MyApplication.Constr_SetOk = this.getString(R.string.Constr_SetOk);
		MyApplication.Constr_unsupport = this
				.getString(R.string.Constr_unsupport);
		MyApplication.Constr_Putandexit = this
				.getString(R.string.Constr_Putandexit);

		MyApplication.Constr_stopscan = this
				.getString(R.string.Constr_stopscan);
		MyApplication.Constr_scanasetconnecto=this.getString(R.string.Constr_scanasetconnecto);
		MyApplication.Constr_scanselectabluereader=this
				.getString(R.string.Constr_scanselectabluereader);
		MyApplication.Constr_scanselectabluereaderandconnect=this
				.getString(R.string.Constr_scanselectabluereaderandconnect);
		MyApplication.Constr_hadconnected = this
				.getString(R.string.Constr_hadconnected);
		MyApplication.Constr_plsetuuid = this
				.getString(R.string.Constr_plsetuuid);
		MyApplication.Constr_pwderror = this
				.getString(R.string.Constr_pwderror);
		MyApplication.Constr_search = this.getString(R.string.Constr_search);
		MyApplication.Constr_stop = this.getString(R.string.Constr_stop);
		MyApplication.Constr_plselectsearchblueset = this.getString(R.string.Constr_plselectsearchblueset);
		MyApplication.Constr_startsearchblueok = this.getString(R.string.Constr_startsearchblueok);
		MyApplication.Constr_startsearchbluefail1 = this.getString(R.string.Constr_startsearchbluefail1);
		MyApplication.Constr_startsearchbluefail2 = this.getString(R.string.Constr_startsearchbluefail2);
		MyApplication.Constr_startsearchbluefail12 = this.getString(R.string.Constr_startsearchbluefail12);
		MyApplication.Constr_canclebluematch = this.getString(R.string.Constr_canclebluematch);
		MyApplication.Constr_connectbluesetfail =this.getString(R.string.Constr_connectbluesetfail);
		MyApplication.Constr_matchbluefail =this.getString(R.string.Constr_matchbluefail);
		MyApplication.Constr_pwdmatchfail =this.getString(R.string.Constr_pwdmatchfail);
		MyApplication.Constr_connectblueokthentoreader = this
				.getString(R.string.Constr_connectblueokthentoreader);
		MyApplication.Constr_connectblueserfail =this
				.getString(R.string.Constr_connectblueserfail);
		MyApplication.Constr_createreaderok = this
				.getString(R.string.Constr_createreaderok);

		MyApplication.Constr_sub3readmem = this
				.getString(R.string.Constr_sub3readmem);
		MyApplication.Constr_sub3writemem = this
				.getString(R.string.Constr_sub3writemem);
		MyApplication.Constr_sub3lockkill = this
				.getString(R.string.Constr_sub3lockkill);
		MyApplication.Constr_sub3readfail = this
				.getString(R.string.Constr_sub3readfail);
		MyApplication.Constr_sub3nodata = this
				.getString(R.string.Constr_sub3nodata);
		MyApplication.Constr_sub3wrtieok = this
				.getString(R.string.Constr_sub3wrtieok);
		MyApplication.Constr_sub3writefail = this
				.getString(R.string.Constr_sub3writefail);
		MyApplication.Constr_sub3lockok = this
				.getString(R.string.Constr_sub3lockok);
		MyApplication.Constr_sub3lockfail = this
				.getString(R.string.Constr_sub3lockfail);
		MyApplication.Constr_sub3killok = this
				.getString(R.string.Constr_sub3killok);
		MyApplication.Constr_sub3killfial = this
				.getString(R.string.Constr_sub3killfial);

		MyApplication.Auto = this.getString(R.string.Auto);
	 
		MyApplication.Constr_sub4invenpra = this
				.getString(R.string.Constr_sub4invenpra);
		MyApplication.Constr_sub4antpow = this
				.getString(R.string.Constr_sub4antpow);
		MyApplication.Constr_sub4regionfre = this
				.getString(R.string.Constr_sub4regionfre);
		MyApplication.Constr_sub4gen2opt = this
				.getString(R.string.Constr_sub4gen2opt);
		MyApplication.Constr_sub4invenfil = this
				.getString(R.string.Constr_sub4invenfil);
		MyApplication.Constr_sub4addidata = this
				.getString(R.string.Constr_sub4addidata);
		MyApplication.Constr_sub4others = this
				.getString(R.string.Constr_sub4others);
	 
		MyApplication.Constr_sub4setmodefail = this
				.getString(R.string.Constr_sub4setmodefail);
		MyApplication.Constr_sub4setokresettoab = this
				.getString(R.string.Constr_sub4setokresettoab);
		MyApplication.Constr_sub4ndsapow = this
				.getString(R.string.Constr_sub4ndsapow);
		MyApplication.Constr_sub4unspreg = this
				.getString(R.string.Constr_sub4unspreg);

		MyApplication.Constr_subblmode = this
				.getString(R.string.Constr_subblmode);
		MyApplication.Constr_subblinven = this
				.getString(R.string.Constr_subblinven);
		MyApplication.Constr_subblfil = this
				.getString(R.string.Constr_subblfil);
		MyApplication.Constr_subblfre = this
				.getString(R.string.Constr_subblfre);
		MyApplication.Constr_subblnofre = this
				.getString(R.string.Constr_subblnofre);
		MyApplication.Constr_subbl = this
				.getString(R.string.Constr_subbl);

		MyApplication.Constr_subcsalterpwd = this
				.getString(R.string.Constr_subcsalterpwd);
		MyApplication.Constr_subcslockwpwd = this
				.getString(R.string.Constr_subcslockwpwd);
		MyApplication.Constr_subcslockwoutpwd = this
				.getString(R.string.Constr_subcslockwoutpwd);
		MyApplication.Constr_subcsplsetimeou = this
				.getString(R.string.Constr_subcsplsetimeou);
		MyApplication.Constr_subcsputcnpwd = this
				.getString(R.string.Constr_subcsputcnpwd);
		MyApplication.Constr_subcsplselreg = this
				.getString(R.string.Constr_subcsplselreg);
		MyApplication.Constr_subcsopfail = this
				.getString(R.string.Constr_subcsopfail);
		MyApplication.Constr_subcsputcurpwd = this
				.getString(R.string.Constr_subcsputcurpwd);

		MyApplication.Constr_subdbdisconnreconn = this
				.getString(R.string.Constr_subdbdisconnreconn);
		MyApplication.Constr_subdbhadconnected = this
				.getString(R.string.Constr_subdbhadconnected);
		MyApplication.Constr_subdbconnecting = this
				.getString(R.string.Constr_subdbconnecting);
		MyApplication.Constr_subdbrev = this
				.getString(R.string.Constr_subdbrev);
		MyApplication.Constr_subdbstop = this
				.getString(R.string.Constr_subdbstop);
		MyApplication.Constr_subdbdalennot = this
				.getString(R.string.Constr_subdbdalennot);
		MyApplication.Constr_subdbplpuhexchar = this
				.getString(R.string.Constr_subdbplpuhexchar);
 

		MyApplication.Coname = this.getResources().getStringArray(
				R.array.Coname);

		MyApplication.pdaatpot = this.getResources().getStringArray(
				R.array.pdaatpot);

		MyApplication.spibank = this.getResources().getStringArray(
				R.array.spibank);
		MyApplication.spifbank = this.getResources().getStringArray(
				R.array.spifbank);
		MyApplication.spilockbank = this.getResources().getStringArray(
				R.array.spilockbank);
		MyApplication.spilocktype = this.getResources().getStringArray(
				R.array.spilocktype);

		MyApplication.spireg = this.getResources().getStringArray(
				R.array.spireg);
		MyApplication.spinvmo = this.getResources().getStringArray(
				R.array.spinvmo);
		MyApplication.spitari = this.getResources().getStringArray(
				R.array.spitari);
		MyApplication.spiwmod = this.getResources().getStringArray(
				R.array.spiwmod);

		MyApplication.cusreadwrite = this.getResources().getStringArray(
				R.array.cusreadwrite);
		MyApplication.cuslockunlock = this.getResources().getStringArray(
				R.array.cuslockunlock);
		MyApplication.strconectway=this.getResources().getStringArray(R.array.strconectway);
		
		if(language.contains("en")){
			myapp.spiregbs=new String[]{ "NA", "China", "Europe", "China2" };
		}

	}
	@SuppressLint("SetTextI18n")
	public void ReadExcelFile(Context context, Uri uri) {
		try {
			InputStream inStream;
			Workbook wb = null;

			try {
				inStream = context.getContentResolver().openInputStream(uri);

				if (fileType == extensionXLS)
					wb = new HSSFWorkbook(inStream);

				else
					wb = new XSSFWorkbook(inStream);

				inStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			Sheet sheet1 = wb.getSheetAt(0);
			// ImageView imageView = mContext.findViewById(R.id.images); // Replace with your ImageView id

       /* List<PictureData> pictures = (List<PictureData>) wb.getAllPictures();
            System.out.println("pictures: "+ pictures);
        Map<String, PictureData> pictureMap = new HashMap<>();
        for (PictureData picture : pictures) {
            // Use the identifier from your Excel data as the key
            Row row = sheet1.getRow(pictures.indexOf(picture) + 1); // Adding 1 to match Excel rows
            String identifier = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue().trim();
            System.out.println("identfiers: "+identifier);
            pictureMap.put(identifier, picture);

        }*/
			// System.out.println("picturemap"+pictureMap);
			DataFormatter formatter = new DataFormatter();
			for (int i = 0; i < sheet1.getLastRowNum(); i++) {
				Row row = sheet1.getRow(i+1);
				if (row == null) continue;

				String tag = formatter.formatCellValue(row.getCell(0)).trim();
				String descp = formatter.formatCellValue(row.getCell(1)).trim();
				String size = formatter.formatCellValue(row.getCell(2)).trim();
				String purity = formatter.formatCellValue(row.getCell(3)).trim();
				String weight = formatter.formatCellValue(row.getCell(4)).trim();
				String views = formatter.formatCellValue(row.getCell(5)).trim();
				String info = formatter.formatCellValue(row.getCell(6)).trim();
				String imageUrl = formatter.formatCellValue(row.getCell(7)).trim();

				if (tag.isEmpty()) continue;

				ReadExcelModel readExcelModel = new ReadExcelModel();
				readExcelModel.setTagname(tag);
				readExcelModel.setDescp(descp);
				readExcelModel.setPrice(size);
				readExcelModel.setPricePerGram(purity);
				readExcelModel.setWeight(weight);
				readExcelModel.setViews(views);
				readExcelModel.setInfo(info);
				readExcelModel.setImageUrl(imageUrl);

				readExcelModels.add(readExcelModel);
				
				Log.d("RFID_EXCEL", "Row " + i + " loaded tag: " + tag);
			}
			Log.d("RFID_EXCEL", "Total models loaded: " + readExcelModels.size());
		} catch (Exception ex) {
			Log.e("RFID_EXCEL", "Error loading Excel", ex);
			Toast.makeText(this, "ReadExcelFile Error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	public void compareTags() {
		List<ReadExcelModel> localFoundTags = new ArrayList<>();
		List<String> scannedLocal = new ArrayList<>(scanTags);

		if (readExcelModels.isEmpty()) {
			Log.e("RFID_COMPARE", "Search failed: readExcelModels is EMPTY. Please import Excel first.");
			return;
		}

		Log.d("RFID_COMPARE", "Searching for matches: ScannedCount=" + scannedLocal.size() + ", ExcelCount=" + readExcelModels.size());

		for (String epc : scannedLocal) {
			String cleanEPC = epc != null ? epc.trim() : "";
			if (cleanEPC.isEmpty()) continue;
			
			boolean matched = false;
			for (ReadExcelModel model : readExcelModels) {
				String modelTag = model.getTagname() != null ? model.getTagname().trim() : "";
				if (cleanEPC.equalsIgnoreCase(modelTag)) {
					Log.d("RFID_COMPARE", "!!! MATCH FOUND !!! Scanned[" + cleanEPC + "] == Excel[" + modelTag + "]");
					if (!localFoundTags.contains(model)) {
						localFoundTags.add(model);
					}
					matched = true;
					break;
				}
			}
			if (!matched) {
				Log.w("RFID_COMPARE", "MISSING IN EXCEL: Scanned tag [" + cleanEPC + "] not found in Excel sheet.");
			}
		}

		Log.d("RFID_COMPARE", "Final matched count: " + localFoundTags.size());

		runOnUiThread(() -> {
			foundTags.clear();
			foundTags.addAll(localFoundTags);
			
			if (adapter3 == null) {
				adapter3 = new Adapter3(this, foundTags);
				LvTags.setAdapter(adapter3);
			} else {
				adapter3.notifyDataSetChanged();
			}
			
			if (!foundTags.isEmpty()) {
				claimDiscountButton.setVisibility(View.VISIBLE);
			}
		});
	}


}

