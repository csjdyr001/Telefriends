package com.cfks.telefriends;

import android.app.Activity;
import android.content.pm.*;
import android.os.*;
import android.support.v4.app.*;
import android.content.*;
import android.graphics.drawable.*;
import android.support.annotation.*;
import android.support.design.widget.*;
import android.support.v4.content.*;
import android.support.v4.view.*;
import android.support.v4.widget.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import com.cfks.telefriends.settings_screen.SettingsActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import com.cfks.telefriends.databinding.ActivityMainBinding;
import com.cfks.telefriends.adapter.*;
import com.cfks.telefriends.utils.*;
import com.cfks.telefriends.chats_previews_screen.*;
import com.cfks.telefriends.db.*;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ActivityMainBinding binding;
    private final Context context = MainActivity.this;

    private RecyclerView recycleView;
    private MessagesGridRecycleViewAdapter adapterRv;
    private TextView txtUsername, txtUid;
    private CircleImageView profileImage, headerProfileImage;
	
    private AlertDialog alertDialog1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setUpUI();
        共享数据.初始化数据(this,"Telefriends");
        showStatement(this);
        if(Build.VERSION.SDK_INT>=23){
            ActivityCompat.requestPermissions(this,MainActivity.getAllPermissions(this),1001);
        }
        if(!(共享数据.是否包含数据("email") && 共享数据.是否包含数据("password"))) {
        	goToLogin();
        }else{
            getUserInfo();
        }
    }
    
    public static void showStatement(Activity act){
         if(!共享数据.取逻辑("showStatement")) {
         	android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(act)
                .setTitle("免责声明")
                .setMessage("该软件仅供学习和研究使用。其旨在为学术和研究人员提供参考和资料，任何其他目的均不适用。严禁将此软件用于任何商业或非法用途。对于因违反此规定而产生的任何法律后果，用户需自行承担全部责任。该软件的所有资源信息均来源于网络。如有关于版权的争议或问题，请联系原作者或权利人。本声明者与版权问题无关且不承担任何相关责任。\n\n本项目Github开源地址：https://github.com/csjdyr001/Telefriends")
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        共享数据.置逻辑("showStatement",true);
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .create();
            dialog.show();
         }
    }
    
    private void goToLogin(){
        Intent intent = new Intent();
        intent.setClass(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    
    @Override
	public boolean onNavigationItemSelected(MenuItem item) {
    DrawerLayout drawer = findViewById(R.id.drawer_layout);
    int itemId = item.getItemId();

    if (itemId == R.id.nav_groupchat) {
        // 处理群聊
    } else if (itemId == R.id.nav_gallery) {
        // 处理相册
    } else if (itemId == R.id.nav_settings) {
        drawer.closeDrawer(GravityCompat.START);
        SettingsActivity.open(context);
    }

    return true;
}
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.binding = null;
    }
    
    public static String[] getAllPermissions(Activity ctx){
	PackageManager manager = ctx.getPackageManager();
	try {
		PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
		String pkgName = info.packageName;
		PackageInfo packageInfo = manager.getPackageInfo(pkgName, PackageManager.GET_PERMISSIONS);
		return packageInfo.requestedPermissions;
	} catch (PackageManager.NameNotFoundException e) {
		e.printStackTrace();
		return new String[0];
	}
	}
    
    private void setUpUI() {
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        android.support.v7.app.ActionBarDrawerToggle toggle = new android.support.v7.app.ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //region navigation view set-up
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        final Menu menuNav = navigationView.getMenu();
        menuNav.setGroupVisible(R.id.nav_group_accounts, false);
        //menuCurrAccount = menuNav.findItem(R.id.nav_curr_account);

        profileImage = new CircleImageView(context);
        profileImage.setMinimumHeight(62);
        profileImage.setMinimumWidth(62);

        profileImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.profile_default_male));
        //menuCurrAccount.setActionView(profileImage);

        final View navHeaderView = navigationView.getHeaderView(0);
        ToggleButton toggleBtn = navHeaderView.findViewById(R.id.account_view_icon_button);
        toggleBtn.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                // TODO: Implement this method
                menuNav.setGroupVisible(R.id.nav_group_accounts, arg1);
            }
        });

        txtUsername = navHeaderView.findViewById(R.id.text_username);
        txtUid = navHeaderView.findViewById(R.id.text_uid);
        headerProfileImage = navHeaderView.findViewById(R.id.header_profile_image);
        //endregion

        recycleView = findViewById(R.id.main_recycler_view_messages);
        adapterRv = new MessagesGridRecycleViewAdapter(context);
        recycleView.setAdapter(adapterRv);
        recycleView.setLayoutManager(new LinearLayoutManager(context));

        final DividerCustomPaddingItemDecoration itemCustomDecor = new DividerCustomPaddingItemDecoration(context,
                DividerItemDecoration.VERTICAL,
                Utils.dpToPx(getResources().getDimension(R.dimen.message_image_preview_scale), context) -
                        Utils.dpToPx(9, context)
        );
        recycleView.addItemDecoration(itemCustomDecor);
        recycleView.setNestedScrollingEnabled(false);
        recycleView.addOnItemTouchListener(new RecyclerItemClickListener(context, recycleView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                clickOnRecycleItem(position);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                longClickRecycleItem(position);
            }
        }));
        
        ((FloatingActionButton) findViewById(R.id.main_add_fab)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                // TODO: Implement this method
                //选择要添加的手表类型
                final String[] items = {"小天才",            "奇虎360",            "米兔",               "子腾"};
                final int[]    value = {WatchType.WATCH_XTC,WatchType.WATCH_QIHOO,WatchType.WATCH_MITU,WatchType.WATCH_ZITENG};
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
                alertBuilder.setTitle("请选择要添加的手表");
                alertBuilder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, AddFriendActivity.class);
                        intent.putExtra("watch_type",value[i]);
                        alertDialog1.dismiss();
                        MainActivity.this.startActivity(intent);
                        MainActivity.this.finish();
                    }
                });
                alertDialog1 = alertBuilder.create();
                alertDialog1.show();
            }
        });
    }
    
    private void clickOnRecycleItem(int position) {
        MessagePreview mp = adapterRv.getMessagePreview(position);
        /*
        Intent intent = new Intent(context, ContactChatActivity.class);
        intent.putExtra(Config.USER_ID_EXTRA, mp.getRecipientId());
        startActivity(intent);
        */
    }

    private void longClickRecycleItem(int position) {
        MessagePreview mp = adapterRv.getMessagePreview(position);

        ConvoBottomDialog cbDialog = ConvoBottomDialog.newInstance(mp.getRecipientId());
        Bundle bundle = new Bundle();
        bundle.putLong(ConvoBottomDialog.RECIPIENT_ID, mp.getRecipientId());
        cbDialog.setArguments(bundle);

        cbDialog.show(getSupportFragmentManager(),
                "buttom_dialog_fragment");
    }
    
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    private void updateUserDetail(@NonNull User user) {
        if (user == null){
            return;
        }
        txtUsername.setText(user.username);
        txtUid.setText("UID " + Integer.toString(user.getUid()));
        //menuCurrAccount.setTitle(user.firstName);

        GlideRequest<Drawable> glideRequest = GlideApp.with(this)
                .load(user.picUrl)
                .placeholder(R.drawable.profile_default_male)
                .fitCenter();

        glideRequest.into(profileImage);
        glideRequest.into(headerProfileImage);
    }
    
    private void getUserInfo(){
        NetUtils.post(this,ApiConfig.getUserInfoApi(this),共享数据.取文本("token"),new NetUtils.NetCallback(){
            @Override
            public void onSucceed(JSONObject json) throws Exception {
                // TODO: Implement this method
                if(json.getInt("code") == 200){
                    //显示用户数据
                    JSONObject data = json.getJSONObject("data");
                    User mUser = new User(data.getInt("uid"));
                    mUser.email = data.getString("email");
                    mUser.username = data.getString("username");
                    mUser.picUrl = data.getString("profilePhoto");
                    updateUserDetail(mUser);
                }else{
                    //登录过期
                    Toast.makeText(MainActivity.this, "登录已过期，请重新登录", Toast.LENGTH_SHORT).show();
                    goToLogin();
                }
            }
        });
    }
}