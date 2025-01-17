package com.carbarn.common.utils;

import com.carbarn.common.exception.UncheckedJsonProcessingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import static com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_COMMENTS;
import static com.fasterxml.jackson.core.json.JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * @Description json转换工具, 建议使用jackson，放弃fastjson。
 * 字段别名注解使用 {@link com.fasterxml.jackson.annotation.JsonAlias}
 * 去除null值使用 {@link com.fasterxml.jackson.annotation.JsonInclude}
 * @Author lxzou
 * @Date 2024/1/11
 */
public class ObjectJsonMapper {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    public static final String EMPTY_JSON = "{}";

    static {
        MAPPER.disable(FAIL_ON_UNKNOWN_PROPERTIES);
        MAPPER.enable(ALLOW_COMMENTS);
        MAPPER.enable(ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature());
        // 解决protobuf序列化失败问题
        MAPPER.findAndRegisterModules();
        // 解决joda序列化问题
        MAPPER.registerModule(new JodaModule());
    }


    public static String toJSON(@Nullable Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new UncheckedJsonProcessingException(e);
        }
    }

    public static <T> T fromJSON(@Nullable String json, Class<T> clazz) {
        if (json == null) {
            return null;
        }
        try {
            return MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new UncheckedJsonProcessingException(e);
        }
    }

    public static <T> T fromJSON(@Nullable byte[] bytes, Class<T> clazz) {
        if (bytes == null) {
            return null;
        }
        try {
            return MAPPER.readValue(bytes, clazz);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <E, T extends Collection<E>> T fromJSON(@Nullable String json,
                                                          Class<? extends Collection> collectionType, Class<E> valueType) {
        if (json == null) {
            return null;
        }
        try {
            return MAPPER.readValue(json,
                    TypeFactory.defaultInstance().constructCollectionType(collectionType, valueType));
        } catch (JsonProcessingException e) {
            throw new UncheckedJsonProcessingException(e);
        }
    }

    public static <E, T extends Collection<E>> T fromJSON(@Nullable byte[] bytes,
                                                          Class<? extends Collection> collectionType, Class<E> valueType) {
        if (bytes == null) {
            return null;
        }
        try {
            return MAPPER.readValue(bytes,
                    TypeFactory.defaultInstance().constructCollectionType(collectionType, valueType));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <K, V, T extends Map<K, V>> T fromJSON(@Nullable String json,
                                                         Class<? extends Map> mapClass, Class<K> keyType, Class<V> valueType) {
        if (json == null) {
            return null;
        }
        try {
            return MAPPER.readValue(json,
                    TypeFactory.defaultInstance().constructMapType(mapClass, keyType, valueType));
        } catch (JsonProcessingException e) {
            throw new UncheckedJsonProcessingException(e);
        }
    }

    public static <K, V, T extends Map<K, V>> T fromJSON(@Nullable byte[] bytes,
                                                         Class<? extends Map> mapClass, Class<K> keyType, Class<V> valueType) {
        if (bytes == null) {
            return null;
        }
        try {
            return MAPPER.readValue(bytes,
                    TypeFactory.defaultInstance().constructMapType(mapClass, keyType, valueType));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * 将string转换为map的简化方法，前提保证json是map类型
     *
     * @param json map类型的jsonString
     * @return map
     */
    public static Map<String, Object> fromJson(@Nullable String json) {
        return fromJSON(json, Map.class, String.class, Object.class);
    }

    /**
     * 将数组转换为map的简化方法，前提保证bytes数组是map类型
     *
     * @param bytes map类型的细节数组
     * @return map
     */
    public static Map<String, Object> fromJson(@Nullable byte[] bytes) {
        return fromJSON(bytes, Map.class, String.class, Object.class);
    }

    public static String readString(String json, String... fieldNames) {
        return Optional.ofNullable(readProperty(json, fieldNames)).map(JsonNode::asText).orElse(null);
    }

    public static Integer readInt(String json, String... fieldNames) {
        return Optional.ofNullable(readProperty(json, fieldNames)).map(JsonNode::asInt).orElse(null);
    }

    public static Long readLong(String json, String... fieldNames) {
        return Optional.ofNullable(readProperty(json, fieldNames)).map(JsonNode::asLong).orElse(null);
    }

    public static Double readDouble(String json, String... fieldNames) {
        return Optional.ofNullable(readProperty(json, fieldNames)).map(JsonNode::asDouble).orElse(null);
    }

    public static Boolean readBoolean(String json, String... fieldNames) {
        return Optional.ofNullable(readProperty(json, fieldNames)).map(JsonNode::asBoolean).orElse(null);
    }

    public static <E, T extends Collection<E>> T readCollectionProperty(String json,
                                                                        Class<? extends Collection> collectionType, Class<E> valueType, String... fieldNames) {
        String collection = Optional.ofNullable(readProperty(json, fieldNames)).map(JsonNode::toString)
                .orElse(null);
        return fromJSON(collection, collectionType, valueType);
    }

    private static JsonNode readProperty(String json, String... fieldNames) {
        try {
            JsonNode jsonNode = MAPPER.readTree(json);
            for (String fieldName : fieldNames) {
                jsonNode = jsonNode.get(fieldName);
                if (jsonNode == null) {
                    return null;
                }
            }
            return jsonNode;
        } catch (Exception e) {
            throw new UncheckedJsonProcessingException(e);
        }
    }

    public static <K, V, T extends Map<K, V>> T toMap(@Nullable Object obj, Class<K> keyType, Class<V> valueType) {
        if (obj == null) {
            return null;
        }
        return fromJSON(toJSON(obj), Map.class, keyType, valueType);
    }
}
