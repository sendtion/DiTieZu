package com.sendtion.ditiezu.util;

import android.content.Context;
import android.content.res.Resources;

import com.sendtion.ditiezu.R;


/**
 * Description:
 * CreateTime: 2018/8/13 19:24
 * Author: ShengDecheng
 */

public class ADFilterTool {
    public static boolean hasAd(Context context, String url){
        Resources res= context.getResources();
        String[]adUrls =res.getStringArray(R.array.adBlockUrl);
        for(String adUrl :adUrls){
            if(url.contains(adUrl)){
                return true;
            }
        }
        return false;
    }
}
