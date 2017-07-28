package com.jasonzhong.nasapictureviewer.util;

import com.jasonzhong.nasapictureviewer.models.NasaPicInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by junzhong on 2017-07-28.
 */

public class JsonParser {

    public static NasaPicInfo parseNasaPicInfo(String jsonResult){
        NasaPicInfo nasaPicInfo = null;
        try {
            nasaPicInfo = new NasaPicInfo();
            JSONObject reader = new JSONObject(jsonResult);
            nasaPicInfo.setDate(reader.optString("date"));
            nasaPicInfo.setExplanation(reader.optString("explanation"));
            nasaPicInfo.setHdurl(reader.optString("hdurl"));
            nasaPicInfo.setMedia_type(reader.optString("media_type"));
            nasaPicInfo.setService_version(reader.optString("service_version"));
            nasaPicInfo.setTitle(reader.optString("title"));
            nasaPicInfo.setUrl(reader.optString("url"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return nasaPicInfo;
    }
}
