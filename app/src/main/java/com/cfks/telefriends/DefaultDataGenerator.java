package com.cfks.telefriends;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cfks.telefriends.db.Message;
import com.cfks.telefriends.db.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Kyrylo Avramenko on 8/16/2018.
 */
public class DefaultDataGenerator {

    private static final String TAG = "DefaultDataGenerator";

    public static List<User> generateUsers() {

        final List<User> list = new ArrayList<>();
        list.add(createUserEntity(114514, "Deepseekro Deepseeklala", "AI", "service@deepseek.com",
                "Deepseekro Deepseeklala"));
        list.add(createUserEntity(2, "无处不在的草方块", "", "caofangkuai2022@gmail.com", "无处不在的草方块"));

        return list;
    }

    public static User createUserEntity(long id, String name, @Nullable String bio, String email
            , String username) {
        User user = new User(id);
        user.firstName = name;
        user.bio = bio;
        user.email = email;
        user.username = username;
        return user;
    }

    @NonNull
    public static List<Message> generateMessages() {
        final List<Message> listMessages = new ArrayList<>();
        long recipientId = 114514;

        Calendar cal1 = GregorianCalendar.getInstance();
        cal1.set(2018, Calendar.AUGUST, 21, 12, 33);

        //Imitate todays date
        final Calendar cal2 = Calendar.getInstance();
        cal2.setTime(cal2.getTime());
        cal2.add(Calendar.HOUR, -3);
        cal2.add(Calendar.MINUTE, -10);
        listMessages.add(createMessage(recipientId, "Hi, bruv. Today I did not see you at the gym", cal2.getTime()));
        cal2.add(Calendar.MINUTE, 2);

        listMessages.add(createMessage(recipientId, "Are you even lifting bro", cal2.getTime()));

        cal2.add(Calendar.MINUTE, 1);
        listMessages.add(createMessage(recipientId, "I do \n THIS \n EVERY \n DAY", cal2.getTime()));

        cal2.add(Calendar.MINUTE, 1);
        listMessages.add(createMessage(recipientId, "you should too", cal2.getTime()));

        listMessages.add(createMessage(recipientId, "promise me", cal2.getTime()));
        listMessages.add(createMessage(recipientId, "Not tommorow", cal2.getTime()));
        listMessages.add(createMessage(recipientId, "NOW!", cal2.getTime()));
        listMessages.add(createMessage(recipientId, "REMEMBER! \nI  \ndo \n THIS \n EVERY \n DAY", cal2.getTime()));

        cal2.add(Calendar.MINUTE, 2);
        listMessages.add(createMessage(recipientId, "ALRIGHT THEN" +
                "\n MORE TEXT \n AND EVEN more text", cal2.getTime()));

        cal2.add(Calendar.MINUTE, 1);
        listMessages.add(createMessage(recipientId, "Hahahahaha-\bahahaha hahah hahahah hahahah " +
                "hahahahahaha\n hahahaha\nhahah\nhaha", cal2.getTime()));

        //===================================================================================
        recipientId = 2;

        cal1.set(2018, Calendar.AUGUST, 17, 11, 0);
        listMessages.add(createMessage(recipientId, "So, at 1pm at coffee shop?", cal1.getTime()));
        cal1.set(2018, Calendar.AUGUST, 17, 11, 1);
        listMessages.add(createMessage(recipientId, "see you there", cal1.getTime()));
        //===================================================================================
        return listMessages;
    }

    public static Message createMessage(long recipientId, String text, Date date) {
        Message message = new Message(0);
        message.recipientId = recipientId;
        message.text = text;
        message.setDate(date);
//        lastMessageLive.date = date;
        message.messageType = Message.MessageType.RECEIVED;
        return message;
    }
}

