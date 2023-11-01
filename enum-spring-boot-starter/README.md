# 优雅的使用枚举参数

## 接受格式为 json (Content-Type: application/json)

推荐使用 jackson 注解，最终将显示 value 字段，前端传值也是 传 int型的 value 字段

```
@Getter
@AllArgsConstructor
public enum UserStatus {

    NORMAL(1, "正常"),
    FREEZE(2, "冻结");

    @JsonValue
    private final int value;
    private final String text;

    @JsonCreator
    public static UserStatus valueOf(int value) {
        for (UserStatus status : UserStatus.values()) {
            if (status.value == value) {
                return status;
            }
        }
        return null;
    }
}
```

## 接受格式为 form (GET 或者 POST FORM)

列如

```
@RestController
@RequestMapping
public class EnumConverterController {
    @RequestMapping("UserStatus")
    public (@UserStatus userStatusRequestParam UserStatus userStatus) {

        return userStatus;
    }
}
```

此时使用不到jackson注解`@JsonValue`,因为他使用了 Spring 的 Converter，所以我们需要自定义Converter
直接使用

```
<dependency>
    <groupId>com.admin4j.framework</groupId>
    <artifactId>enum-spring-boot-starter</artifactId>
    <version>0.8.0</version>
</dependency>
```

参考 `EnumConverterFactory`

```
@RequiredArgsConstructor
public class EnumConverterFactory implements ConverterFactory<String, Enum> {

    @SuppressWarnings("rawtypes")
    private static final Map<Class, Converter> CONVERTERS = new ConcurrentHashMap<>(64);
    private final List<EnumConverter> enumConverters;


    @Override
    public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
        return CONVERTERS.computeIfAbsent(targetType, (key) -> new StringToEnum<T>(targetType, enumConverters));
    }

    private static class StringToEnum<T extends Enum> implements Converter<String, T> {

        private final Class<T> enumType;
        private final List<EnumConverter> enumConverters;

        StringToEnum(Class<T> enumType, List<EnumConverter> enumConverters) {
            this.enumType = enumType;
            this.enumConverters = enumConverters;
        }


        @Override
        public T convert(String source) {
            if (source.isEmpty()) {
                // It's an empty enum identifier: reset the enum value to null.
                return null;
            }
            for (EnumConverter enumConverter : enumConverters) {
                T convert = enumConverter.convert(source, enumType);
                if (convert != null) {
                    return convert;
                }
            }
            return null;
        }
    }
}
```