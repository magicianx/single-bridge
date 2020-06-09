package com.gaea.single.bridge.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JsonUtils {

  /**
   * 对象转化为json字符串
   *
   * @param obj 实例对象
   * @return json字符串
   */
  public static String toJsonString(Object obj) {
    return JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue);
  }

  /** 将对象转换为json字节数组 */
  public static byte[] toJsonBytes(Object obj) {
    return JSON.toJSONBytes(obj);
  }
  /**
   * 将json转化为对象
   *
   * @param json json字符串
   * @param cls 实例对象cls
   * @return 实例对象
   */
  public static <T> T toObject(String json, Class<T> cls) {
    return JSON.parseObject(json, cls);
  }

  /**
   * 将json字符串转化为{@link JSONObject}
   *
   * @param json json字符串
   * @return {@link JSONObject}
   */
  public static JSONObject toJsonObject(String json) {
    return JSON.parseObject(json);
  }

  /**
   * 将对象转化为{@link JSONObject}
   *
   * @param obj 实例对象
   * @return {@link JSONObject}
   */
  public static JSONObject toJsonObject(Object obj) {
    return toJsonObject(toJsonString(obj));
  }

  /**
   * 将对象转化为{@link JSONArray}
   *
   * @param obj 实例对象
   * @return {@link JSONArray}
   */
  public static JSONArray toJsonArray(Object obj) {
    return JSON.parseArray(toJsonString(obj));
  }
}
