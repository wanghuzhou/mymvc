package com.wanghz.mymvc.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.wanghz.mymvc.exception.MyRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;


/**
 * Json支持工具类
 *
 * @author wanghz
 */
public class JsonUtil {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .setTimeZone(TimeZone.getDefault())
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    private JsonUtil() {
    }

    static {
        // long类型转为String
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        OBJECT_MAPPER.registerModule(simpleModule);
    }

    /**
     * json字符串转 map
     *
     * @param jsonStr JSON字符串
     * @return Map对象
     */
    public static <K, V> Map<K, V> parseMap(String jsonStr) {
        try {
            return OBJECT_MAPPER.readValue(jsonStr, new TypeReference<Map<K, V>>() {
                @Override
                public Type getType() {
                    return super.getType();
                }
            });
        } catch (JsonProcessingException e) {
            logger.error("Json格式化错误", e);
            throw new MyRuntimeException(e);
        }
    }

    /**
     * 实体类转Map
     *
     * @param object 实体类
     * @return Map<String, String>
     */
    public static <K, V> Map<K, V> parseMap(Object object) {
        try {
            String jsonStr = OBJECT_MAPPER.writeValueAsString(object);
            return OBJECT_MAPPER.readValue(jsonStr, new TypeReference<Map<K, V>>() {
                @Override
                public Type getType() {
                    return super.getType();
                }
            });
        } catch (JsonProcessingException e) {
            logger.error("Json格式化错误", e);
            throw new MyRuntimeException(e);
        }
    }

    /**
     * json字符串转 List
     *
     * @param jsonStr JSON字符串
     * @return List对象
     */
    public static <T> List<T> parseList(String jsonStr) {
        try {
            return OBJECT_MAPPER.readValue(jsonStr, new TypeReference<List<T>>() {
                @Override
                public Type getType() {
                    return super.getType();
                }
            });
        } catch (JsonProcessingException e) {
            logger.error("Json格式化错误", e);
            throw new MyRuntimeException(e);
        }
    }


    /**
     * Json字符串转换成Pojo对象
     *
     * @param jsonStr Json字符串
     * @param clazz   pojo类型
     * @param <T>     泛型Class类型
     * @return 转换完毕Pojo的List对象
     */
    public static <T> T parseObject(String jsonStr, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(jsonStr, clazz);
        } catch (JsonProcessingException e) {
            logger.error("Json转换出错", e);
            throw new MyRuntimeException(e);
        }
    }

    /**
     * Pojo对象转换成JSON字符串
     *
     * @param object pojo对象
     * @return Json字符串
     */
    public static String toJSONString(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.error("Json转换出错", e);
            throw new MyRuntimeException(e);
        }
    }

    /**
     * 解析json字符串，返回JsonNode树
     *
     * @param jsonStr json字符串
     * @return JsonNode
     */
    public static JsonNode readTree(String jsonStr) {
        try {
            return OBJECT_MAPPER.readTree(jsonStr);
        } catch (JsonProcessingException e) {
            logger.error("Json转换出错", e);
            throw new MyRuntimeException(e);
        }
    }

    /**
     * 解析json byte数组，返回JsonNode树
     *
     * @param bytes json byte数组
     * @return JsonNode
     */
    public static JsonNode readTree(byte[] bytes) {
        try {
            return OBJECT_MAPPER.readTree(bytes);
        } catch (IOException e) {
            logger.error("Json转换出错", e);
            throw new MyRuntimeException(e);
        }
    }

    /**
     * JsonNode转实体类
     *
     * @param jsonNode  json树
     * @param valueType 实体类型
     * @return 实体类
     */
    public static <T> T treeToValue(JsonNode jsonNode, Class<T> valueType) {
        try {
            return OBJECT_MAPPER.treeToValue(jsonNode, valueType);
        } catch (JsonProcessingException e) {
            logger.error("Json转换出错", e);
            throw new MyRuntimeException(e);
        }
    }

    /**
     * 实体类转JsonNode
     *
     * @param object 实体类
     * @return 继承JsonNode泛型
     */
    public static <T extends JsonNode> T convertValue(Object object) {
        return OBJECT_MAPPER.valueToTree(object);
    }

    public static <T> T convertValue(Object object, Class<T> clazz) {
        return OBJECT_MAPPER.convertValue(object, clazz);
    }


}
