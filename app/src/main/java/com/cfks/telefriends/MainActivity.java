package com.cfks.telefriends;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;
import de.hdodenhof.circleimageview.CircleImageView;
import com.cfks.telefriends.DividerCustomPaddingItemDecoration;
import com.cfks.telefriends.MessagePreview;
import com.cfks.telefriends.R;
import com.cfks.telefriends.RecyclerItemClickListener;
import com.cfks.telefriends.ViewModelFactory;
import com.cfks.telefriends.adapter.MessagesGridRecycleViewAdapter;
import com.cfks.telefriends.contact_chat_screen.ContactChatActivity;
import com.cfks.telefriends.db.Message;
import com.cfks.telefriends.db.User;
import com.cfks.telefriends.settings_screen.SettingsActivity;
import com.cfks.telefriends.utils.Config;
import com.cfks.telefriends.utils.Utils;
import com.cfks.telefriends.chats_previews_screen.*;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private final Context context = MainActivity.this;

    private RecyclerView recycleView;
    private MessagesGridRecycleViewAdapter adapterRv;
    private TextView txtUserFullname, txtEmail;
    //private MenuItem menuCurrAccount;
    private ChatPreviewViewModel viewModel;
    private CircleImageView profileImage, headerProfileImage;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        setUpUI();
        observeViewModel();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
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
    
    private void setUpUI() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
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
        toggleBtn.setOnCheckedChangeListener((buttonView, isChecked) -> menuNav.setGroupVisible(R.id.nav_group_accounts, isChecked));

        txtUserFullname = navHeaderView.findViewById(R.id.text_user_fullname);
        txtEmail = navHeaderView.findViewById(R.id.text_email);
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
    }

    private void updateUserDetail(@NonNull User user) {

        if (user == null)
            return;
        //Log.d(TAG, "updateUserDetail: " + user.firstName);
        txtUserFullname.setText(user.getFullName());
        txtEmail.setText(user.email);
        //menuCurrAccount.setTitle(user.firstName);

        GlideRequest<Drawable> glideRequest = GlideApp.with(this)
                .load(user.picUrl)
                .placeholder(R.drawable.profile_default_male)
                .fitCenter();

        glideRequest.into(profileImage);
        glideRequest.into(headerProfileImage);
    }
    
    private void observeViewModel() {

        ViewModelFactory factory = ViewModelFactory.getInstance(getApplication());
        //viewModel = ViewModelProviders.of(this, factory).get(ChatPreviewViewModel.class);
/*
        viewModel.getAllLiveUsers().observe(this, users -> {
            //Log.d(TAG, "observeViewModel: getAllLiveUsers().observe");
            if (users != null) {
                viewModel.setAllUsers(users);
            }
        });

        viewModel.getRecentMessageByChat().observe(this, messages -> {
            setChatToRvAdapter(messages);

        });
*/
        //viewModel.loadPhoneContacts();
    }
    
    private void setChatToRvAdapter(List<Message> listMessages) {

        if (listMessages == null)
            return;

        //Log.d(TAG, "observeViewModel: getRecentMessageByChat() setItemsMessages. QTY IS: " + listMessages.size());

        adapterRv.setItems(viewModel.transformToMsgPreviews(listMessages));

    }
    
    private void clickOnRecycleItem(int position) {
        MessagePreview mp = adapterRv.getMessagePreview(position);
        Intent intent = new Intent(context, ContactChatActivity.class);
        intent.putExtra(Config.USER_ID_EXTRA, mp.getRecipientId());
        startActivity(intent);
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
}