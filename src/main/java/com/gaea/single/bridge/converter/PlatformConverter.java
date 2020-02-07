package com.gaea.single.bridge.converter;

import com.alibaba.fastjson.JSONObject;
import com.gaea.single.bridge.dto.platform.CheckVersionRes;
import com.gaea.single.bridge.enums.VersionUpdateType;
import org.springframework.core.convert.converter.Converter;

import java.util.Collections;

public class PlatformConverter {
  public static final Converter<Object, CheckVersionRes> toCheckVersionRes =
      (obj) -> {
        JSONObject result = (JSONObject) obj;
        CheckVersionRes res = new CheckVersionRes();
        res.setCode(result.getInteger("versionCode"));
        res.setVersion(result.getString("versionNum"));
        res.setCode(result.getInteger("versionCode"));
        res.setType(VersionUpdateType.ofCode(result.getInteger("updateType")));
        res.setDetails(Collections.singletonList(result.getString("description")));
        res.setUpdateUrl(result.getString("updateUrl"));
        return res;
      };
}
