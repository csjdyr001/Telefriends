package com.cfks.telefriends.settings_screen;

import com.cfks.telefriends.SettingsTextEntity;

import java.util.ArrayList;

/**
 * Created by Kyrylo Avramenko on 8/22/2018.
 */
public class SettingsTextEntitiesGenerator {


    public static ArrayList<Object> getUserInfoList() {

        ArrayList<Object> items = new ArrayList<>();

        items.add(new SettingsTextEntity(SettingsTextEntity.TextType.SubTitle, "", "邮箱地址"));
        items.add(new SettingsTextEntity(SettingsTextEntity.TextType.SubTitle, "", "用户名"));
        items.add(new SettingsTextEntity(SettingsTextEntity.TextType.SubTitle, "", "简介"));

        return items;
    }

    public static ArrayList<Object> getSettingsList() {
        ArrayList<Object> items = new ArrayList<>();
        items.add(new SettingsTextEntity(SettingsTextEntity.TextType.PlainText, "通知和声音", ""));
        items.add(new SettingsTextEntity(SettingsTextEntity.TextType.PlainText, "隐私和安全", ""));
        items.add(new SettingsTextEntity(SettingsTextEntity.TextType.PlainText, "数据和存储", ""));
        items.add(new SettingsTextEntity(SettingsTextEntity.TextType.PlainText, "聊天背景", ""));

        items.add(new SettingsTextEntity(SettingsTextEntity.TextType.TextWithSelect, "主题", "默认"));
        items.add(new SettingsTextEntity(SettingsTextEntity.TextType.TextWithSelect, "语言", "简体中文"));
        items.add(new SettingsTextEntity(SettingsTextEntity.TextType.TextWithToggle, "启用动画", ""));

        return items;
    }

    public static ArrayList<Object> getMessagesList() {
        ArrayList<Object> items = new ArrayList<>();
        items.add(new SettingsTextEntity(SettingsTextEntity.TextType.TextWithToggle, "应用内浏览器", ""));
        //items.add(new SettingsTextEntity(SettingsTextEntity.TextType.TextWithSelect, "贴纸", "18"));
        items.add(new SettingsTextEntity(SettingsTextEntity.TextType.TextWithSelect, "消息文本大小", "16"));

        items.add(new SettingsTextEntity(SettingsTextEntity.TextType.TextWithToggle, "加入发言", ""));
        //items.add(new SettingsTextEntity(SettingsTextEntity.TextType.TextWithToggle, "通过Enter发送", ""));
        items.add(new SettingsTextEntity(SettingsTextEntity.TextType.TextWithToggle, "自动播放GIF", ""));
        items.add(new SettingsTextEntity(SettingsTextEntity.TextType.TextWithToggle, "保存到相册", ""));

        return items;
    }

    public static ArrayList<Object> getSupporList() {
        ArrayList<Object> items = new ArrayList<>();
        items.add(new SettingsTextEntity(SettingsTextEntity.TextType.PlainText, "Telefriends FAQ", ""));
        items.add(new SettingsTextEntity(SettingsTextEntity.TextType.PlainText, "隐私政策", ""));

        return items;
    }
}
