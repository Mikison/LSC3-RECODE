package com.sonmiike.lsc3.Utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;

public class util {
    public static boolean ifParsable(String argument) {
        try {
            Integer.parseInt(argument);
        } catch (NumberFormatException e) {
            return false; //TODO error message from messages.yml
        }
        return Integer.parseInt(argument) > 0;
    }

    public static boolean isInt(String argument) {
        if (!ifParsable(argument)) {
            return false;
        }
        return true;
    }

    public static Component mm(String text) {
        MiniMessage mm = MiniMessage.miniMessage();
        return mm.deserialize(text);
    }

    public static List<Component> transferStringtoComponent(List<String> lore, OfflinePlayer player) {
        List<Component> newList = new ArrayList<>();
        for (int i = 0; i < lore.size(); i++) {
            newList.add(mm(lore.get(i).replace("{PLAYER}", "<gray>" + player.getName())));
        }
        return newList;
    }

    public static List<Component> transferStringtoComponentNOTLORE(List<String> lore) {
        List<Component> newList = new ArrayList<>();
        for (int i = 0; i < lore.size(); i++) {
            newList.add(mm(lore.get(i)));
        }
        return newList;
    }

    public static List<String> centerList(List <String> beforeList) {
        List<String> newLIST = new ArrayList<>();
        for (String part : beforeList) {
            newLIST.add(sendCenteredMessage(part));
        }
        return newLIST;
    }


    private final static int CENTER_PX = 154;

    public static String sendCenteredMessage(String message){
        if(message == null || message.equals("")) return "";

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for(char c : message.toCharArray()){
            if(c == '§'){
                previousCode = true;
                continue;
            }else if(previousCode == true){
                previousCode = false;
                if(c == 'l' || c == 'L'){
                    isBold = true;
                    continue;
                }else isBold = false;
            }else{
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while(compensated < toCompensate){
            sb.append(" ");
            compensated += spaceLength;
        }
        return (sb.toString() + message);
    }
}
