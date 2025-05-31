package com.cfks.telefriends.contact_chat_screen;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.WindowCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cfks.telefriends.R;
import com.cfks.telefriends.ViewModelFactory;
import com.cfks.telefriends.db.Message;
import com.cfks.telefriends.db.User;
import com.cfks.telefriends.utils.Config;
import com.cfks.telefriends.utils.StringUtils;

import java.util.List;
import java.util.Objects;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Kyrylo Avramenko on 6/22/2018.
 */
//TODO [Polishing] 1) make adjecent sent/received messages closer to each other than to opposite type of messages. Padding can be changed in style file -> 'LayoutOfMessage';
//TODO [Polishing] 2) Make bigger paddingTop value in top lastMessageLive. Its needed to have space from toolbar


public class ContactChatActivity extends AppCompatActivity {

    //region properties
    private static final String TAG = "ContactChatActivity";

    private TextView txtChatPersonName, txtLastSeen;
    private ImageButton btnSend;
    private EditText editText;

    private ContactChatViewModel viewModel;
    private long recipientUserId;
    private ContactChatFragment chatFragment;
    private long messageDraftId = -1;
    private int observeTimesQTY = 0;
    private final CompositeDisposable compDisposable = new CompositeDisposable();
    //endregion

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(WindowCompat.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_contact_chat);

        setUpToolBar();
        setUpUI();

        chatFragment = new ContactChatFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_content, chatFragment);
        transaction.commit();

        recipientUserId = getIntent().getLongExtra(Config.USER_ID_EXTRA, 1);
        observeViewModel(recipientUserId);

        initEditText();

        Log.d(TAG, "onCreate: String recipientUserId: " + recipientUserId);
        updateRecipientUI(viewModel.getUser(recipientUserId));
    }

    @Override
    protected void onStart() {
        super.onStart();
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        Objects.requireNonNull(imm).hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (viewModel.getDoAddEmptyMessage()) {
            viewModel.addEmptyMessage(recipientUserId);
        }

        String s = editText.getText().toString();
        if (!TextUtils.isEmpty(s)) {

            viewModel.saveDraft(recipientUserId, editText.getText());
        }


        if (compDisposable != null && compDisposable.isDisposed())
            compDisposable.clear();

        viewModel.clearDisposables();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_convo, menu);
        return true;
    }

    private void setUpToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.convo_toolbar);
        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar == null) throw new AssertionError();

        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setTitle(null);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setUpUI() {

        final View viewConvoTop = findViewById(R.id.convo_header_view_top);
        txtChatPersonName = viewConvoTop.findViewById(R.id.name);
        txtLastSeen = viewConvoTop.findViewById(R.id.last_seen);

        txtChatPersonName.setTextColor(getResources().getColor(R.color.colorWhite));
        txtLastSeen.setTextColor(getResources().getColor(R.color.colorWhite));

        btnSend = (ImageButton) findViewById(R.id.convo_send_btn);
        enableBtnSend(false);

        btnSend.setOnClickListener(v -> sendMessages());
    }

    private void initEditText() {
        editText = (EditText) findViewById(R.id.convo_message_edittext);
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Log.d(TAG, "onTextChanged: s: " + s + "\n start: " + start + " _before: " + before + " _count: " + count);
            }

            @Override
            public void afterTextChanged(Editable s) {
                enableBtnSend(!s.toString().isEmpty());
            }
        });

        editText.setCustomSelectionActionModeCallback(new ToolBarCallback());
    }

    private void observeViewModel(long recipientUserId) {

        ViewModelFactory factory = ViewModelFactory.getInstance(getApplication());
        viewModel = ViewModelProviders.of(this, factory).get(ContactChatViewModel.class);
        viewModel.getListMessages(recipientUserId).observe(this, listMessages -> {
            observeTimesQTY++;
            setChatMessages(listMessages);
        });
    }

    private void setChatMessages(List<Message> listMessages) {

        if (listMessages == null)
            return;

        Log.d(TAG, "observeViewModel: observeTime: " + observeTimesQTY);
        for (Message m : listMessages) {
            Log.d(TAG, "setChatMessages: m text: " + m.text + " id: " + m.getIdMessage());
        }

        int msgQTY = listMessages.size();
        viewModel.setListCachedMessages(listMessages);

        if (msgQTY == 0) {
            showEmptyMessages();
            return;
        }

        Message lastMessage = listMessages.get(msgQTY - 1);
        //check for draft messages only on activity creation, and remove from the list, if there is draft lastMessageLive
        if (observeTimesQTY == 1 && lastMessage.messageType == Message.MessageType.DRAFT) {

            messageDraftId = lastMessage.getIdMessage();

//            viewModel.getToCustomHtml()
            setTextToEditText(StringUtils.fromHtml(lastMessage.text));
            listMessages.remove(msgQTY - 1);
            viewModel.deleteMessageById(messageDraftId);

            Log.d(TAG, "observeViewModel: remove draft lastMessageLive: " + lastMessage.text);
        }

        msgQTY = listMessages.size();
        //check for empty messages. and delete them. Empty messages
        if (msgQTY == 1 || msgQTY == 2) {
            for (int i = 0; i < msgQTY; i++) {
                Message m = listMessages.get(i);
                if (m.messageType != Message.MessageType.EMPTY) continue;

                Log.d(TAG, "observeViewModel: delete empty lastMessageLive from recipientId: " + m.recipientId);
                listMessages.remove(i);
                viewModel.deleteMessage(m);
                break;
            }
        }

        showMessages(listMessages);
    }

    private void showMessages(@NonNull final List<Message> messages) {

        chatFragment.getAdapterRv().setItemsMessages(messages);
        chatFragment.getRecyclerView().scrollToPosition(chatFragment.getAdapterRv().getItemCount() - 1);
        chatFragment.setStatus(ContactChatFragment.ContentStatus.SHOW_MESSAGES);
    }

    private void showEmptyMessages() {
        chatFragment.setStatus(ContactChatFragment.ContentStatus.NO_MESSAGES);
    }

    private void enableBtnSend(boolean doEnable) {
        btnSend.setVisibility(doEnable ? View.VISIBLE : View.GONE);
    }

    private void sendMessages() {

        Log.d(TAG, "sendMessages: items in adapter: " + chatFragment.getAdapterRv().getItemCount());
        if (messageDraftId >= 0) {
            Log.d(TAG, "sendMessages: DELELTE DRAFT MESSAGE FROM DB id: " + messageDraftId);
            viewModel.deleteMessageById(messageDraftId);
            messageDraftId = -1;
        }

        Disposable disposable = viewModel.getToCustomHtml(editText.getEditableText())
                .doOnNext(s -> {
                    editText.getText().clear();
                    viewModel.sendNonEmptyMessage(recipientUserId, s);
                })
                .subscribeOn(Schedulers.io())
                .subscribe();

        compDisposable.add(disposable);
    }

    private void updateRecipientUI(User user) {
        txtChatPersonName.setText(user.firstName);
    }

    private void setTextToEditText(@NonNull CharSequence text) {

        //Set text to editText box only if its empty. This is needed to avoid overwriting
        if (TextUtils.isEmpty(editText.getText().toString())) {
            editText.setText(text);
            editText.setSelection(editText.length());
        } else
            Log.e(TAG, "setTextToEditText: Can NOT set text, because edit box is already set with text: " + text);
    }

    private class ToolBarCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_fonts, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
    int style = StringUtils.FontType.NORMAL;
    int itemId = item.getItemId();

    if (itemId == R.id.bold) {
        style = StringUtils.FontType.BOLD;
    } else if (itemId == R.id.italics) {
        style = StringUtils.FontType.ITALIC;
    } else if (itemId == R.id.mono) {
        style = StringUtils.FontType.MONO;
    } else if (itemId == R.id.regular) {
        style = StringUtils.FontType.NORMAL;
    }

    int selEnd = editText.getSelectionEnd();
    editText.setText(
        viewModel.getSelectedString(
            editText.getText(),
            style,
            editText.getSelectionStart(),
            selEnd
        ),
        EditText.BufferType.EDITABLE
    );
    editText.setSelection(selEnd);
    return false;
}

        @Override
        public void onDestroyActionMode(ActionMode mode) {

        }
    }
}
