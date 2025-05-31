package com.cfks.telefriends.contact_chat_screen;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cfks.telefriends.R;
import com.cfks.telefriends.RecyclerItemClickListener;
import com.cfks.telefriends.RollingFabState;
import com.cfks.telefriends.adapter.MessageListAdapter;
import com.cfks.telefriends.db.Message;
import com.cfks.telefriends.utils.FabHelper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;

/**
 * Created by Kyrylo Avramenko on 9/4/2018.
 */
public class ContactChatFragment extends Fragment {

    @IntDef({ContentStatus.SHOW_MESSAGES, ContentStatus.NO_MESSAGES})
    @Retention(RetentionPolicy.SOURCE)
    @interface ContentStatus {
        int SHOW_MESSAGES = 0;
        int NO_MESSAGES = 1;
    }

    //region properties
    private static final String TAG = "ContactChatFragment";

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private MessageListAdapter adapterRv;
    private FabHelper fabHelper;
    private ScrollView tvNoMessages;
    private ActionMode actionMode;
    private final ActionModeCallback actionModeCallback = new ActionModeCallback();
    private ContactChatViewModel viewModel;

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public MessageListAdapter getAdapterRv() {
        return adapterRv;
    }
    //endregion

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(ContactChatViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_contact_chat, container, false);
        setUpUI(view);
        return view;
    }

    private void setUpUI(@NonNull final View view) {

        tvNoMessages = view.findViewById(R.id.tv_no_messages);

        recyclerView = view.findViewById(R.id.convo_messages);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapterRv = new MessageListAdapter();
        recyclerView.setAdapter(adapterRv);

        final FloatingActionButton fabScrollDown = (FloatingActionButton) view.findViewById(R.id.fab_scroll_down);
        fabScrollDown.setVisibility(View.INVISIBLE);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                //Initialize FabHelper object
                if (fabHelper == null && Math.abs(dy) > 1) {
                    fabHelper = new FabHelper(fabScrollDown, 200L);
                    fabScrollDown.setTranslationY(fabHelper.getOffscreenTranslation());
                    //Log.d(TAG, "onScrolled: create FabHelper: height: " + fabScrollDown.getHeight());
                }

                if (fabHelper != null) {
                    scrollButton(fabScrollDown, dy);
                }
            }
        });

        fabScrollDown.setOnClickListener(v -> {
            // make scroll down
            //Log.d(TAG, "onClick:  fabScrollDown with fab getTranslationY: " + fabScrollDown.getTranslationY());
            fabHelper.postRollFabOutCompletely(fabScrollDown);
            recyclerView.smoothScrollToPosition(adapterRv.getItemCount() - 1);
        });

        //TODO  When user exits from certain chat,remember position of item in recyclerView and save it.
        // When user reopens chat, scroll to saved position
        recyclerView.scrollToPosition(adapterRv.getItemCount() - 1);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                clickOnMessage(adapterRv.getItem(position));
            }

            @Override
            public void onItemLongClick(View view, int position) {
                longClickOnMessage(adapterRv.getItem(position));
            }
        }));
    }


    private void clickOnMessage(@NonNull Message message) {
        if (actionMode == null)
            return;

        //Log.d(TAG, "clickOnMessage: ");

        toggleMessage(message);
    }

    @SuppressLint("RestrictedApi")
    private void longClickOnMessage(@NonNull Message message) {
        //Log.d(TAG, "longClickOnMessage: ");

        //create Action mode only on long click
        if (actionMode == null) {
            actionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(actionModeCallback);
        }
        toggleMessage(message);
    }

    private void toggleMessage(@NonNull Message message) {
        adapterRv.toggleSelection(message);
        if (adapterRv.getSizeOfSelectedItems() == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(adapterRv.getSizeOfSelectedItems()));
        }
    }

    private void scrollButton(FloatingActionButton fab, int dy) {
//        //Log.d(TAG, "onScrolled: dy: " + dy + "; RollingState: " + fabHelper.getRollingState());

        int lastVisible = linearLayoutManager.findLastCompletelyVisibleItemPosition();
//        //Log.d(TAG, "onScrolled: lastVisible: " + lastVisible);
        //hide fab when user scrolls to the f
        if (lastVisible >= adapterRv.getItemsMessages().size() - 2) {
            //Log.d(TAG, "scrollButton: scroll to FirstCompletelyVisibleItem");
            if (fabHelper.getRollingState() == RollingFabState.IDLE)
                fabHelper.postRollFabOutCompletely(fab);
        }

//        Scroll down to fresh messages. if user scrolls a lot of vertical
        if (dy > 4 && lastVisible < adapterRv.getItemsMessages().size() - 3 &&
                (fabHelper.getRollingState() == RollingFabState.IDLE || fabHelper.getRollingState() == RollingFabState.ROLLED_OUT)) {

            fab.setVisibility(View.VISIBLE);
            fabHelper.postRollFabInCompletely(fab);
        }

//        Scroll up to older messages
        else if (dy < 0 && fabHelper.getRollingState() == RollingFabState.IDLE) {
            fabHelper.postRollFabOutCompletely(fab);
        }
    }

    public void setStatus(@ContentStatus int status) {

        switch (status) {

            case ContentStatus.SHOW_MESSAGES:
                recyclerView.setVisibility(View.VISIBLE);
                tvNoMessages.setVisibility(View.GONE);
                break;

            default:
                recyclerView.setVisibility(View.GONE);
                tvNoMessages.setVisibility(View.VISIBLE);
                break;
        }
    }

    private class ActionModeCallback implements ActionMode.Callback {

        private MenuInflater inflater;

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_message_context, menu);

            mode.setTitle(String.valueOf(adapterRv.getSizeOfSelectedItems()));
            Window window = Objects.requireNonNull(getActivity()).getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.action_mode_status_bar));

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
    int itemId = item.getItemId();

    if (itemId == R.id.menu_msg_copy) {
        viewModel.handleCopyMessage(adapterRv.getSelectedItems());
        actionMode.finish();
        return true;
    } else if (itemId == R.id.menu_msg_delete) {
        Message[] arrMsg = adapterRv.getSelectedItems()
                .toArray(new Message[adapterRv.getSelectedItems().size()]);
        showDeletionDialog(arrMsg);
        return true;
    }

    return false;
}

        private void showDeletionDialog(Message[] arrMsg) {
            DeletionDialogFragment dialogDel = DeletionDialogFragment.newInstance(arrMsg.length);
            dialogDel.show(getActivity().getFragmentManager(), "dialog");

            dialogDel.setDelOptions(new DeletionDialogFragment.OnDeletionOptions() {
                @Override
                public void onOk() {
                    viewModel.deleteMessage(arrMsg);
                    actionMode.finish();
                }

                @Override
                public void onCancel() {
                    actionMode.finish();
                }
            });
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            //Log.d(TAG, "destroy");

            adapterRv.clearSelectedItems();
            actionMode = null;
        }
    }


}
