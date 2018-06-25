package com.fcbox.spark.common.util;

import com.google.common.collect.Lists;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

public class ResourceUtils {

    /**
     * 配置文件名前缀
     */
    private static final String PROPERTIES_FILE_NAME_PREFIX = "application";

    private static final List<String> PROPERTIES_FILE_NAME_PREFIXS = Lists.newArrayList("application", "db", "redis", "sysconf", "hdfs");

    /**
     * 配置文件名后缀
     */
    private static final String PROPERTIES_FILE_NAME_SUFFIX = ".properties";

    /**
     * 配置文件名连接符
     */
    private static final String PROPERTIES_FILE_NAME_JOINER = "-";

    /**
     * 路径分隔符
     */
    private static final String PATH_SEPARATOR = "/";

    /**
     * 默认配置文件名
     */
    private static final String DEFAULT_PROPERTIES_FILE_NAME = PROPERTIES_FILE_NAME_PREFIX + PROPERTIES_FILE_NAME_SUFFIX;

    /**
     * 配置文件激活参数名
     */
    private static final String PROFILES_ACTIVE_PARAMETER_NAME = "profiles.active";

    private static Properties properties;


    static {
        //获取默认配置
        Optional<Properties> defaultPropertiesOptional = getProperties(DEFAULT_PROPERTIES_FILE_NAME);
        //设置参数配置
        properties = defaultPropertiesOptional.orElseThrow(() -> new NullPointerException(
                MessageFormat.format("load default Properties from {0} is null", DEFAULT_PROPERTIES_FILE_NAME)));
        //获取激活环境
        Optional.ofNullable(properties.get(PROFILES_ACTIVE_PARAMETER_NAME))
                .map(String::valueOf)
                .ifPresent(active -> {
                    //获取激活配置
                    Optional<Properties> activePropertiesOptional = getActiveProperties(active);
                    //合并参数配置
                    activePropertiesOptional.ifPresent(properties::putAll);
                });
    }

    /**
     * 获取激活配置参数
     *
     * @param active 激活环境
     * @return Optional
     */
    private static Optional<Properties> getActiveProperties(String active) {
        if (active == null || "".equals(active)) {
            return Optional.empty();
        }

        List<String> propertiesNames = Lists.newArrayList();
        PROPERTIES_FILE_NAME_PREFIXS.forEach(s -> propertiesNames.add(s + PROPERTIES_FILE_NAME_JOINER + active + PROPERTIES_FILE_NAME_SUFFIX));
        return getProperties(propertiesNames);
    }

    /**
     * 获取配置
     *
     * @param propertiesName 配置文件名
     * @return Optional
     */
    private static Optional<Properties> getProperties(String propertiesName) {
        Properties properties = new Properties();
        propertiesName = Objects.requireNonNull(propertiesName);
        try (
                InputStream resourceAsStream = ResourceUtils.class.getResourceAsStream(PATH_SEPARATOR + propertiesName);
                InputStreamReader in = new InputStreamReader(resourceAsStream);
                BufferedReader bufferedReader = new BufferedReader(in);
        ) {
            properties.load(bufferedReader);
        } catch (Exception e) {
            if (PROPERTIES_FILE_NAME_PREFIX.equals(propertiesName)) {
                throw new RuntimeException(MessageFormat.format("read properties from {0} error!", propertiesName), e);
            }
        }
        return Optional.of(properties);
    }

    /**
     * 获取配置
     *
     * @param propertiesNames 配置文件名
     * @return Optional
     */
    private static Optional<Properties> getProperties(List<String> propertiesNames) {
        Properties res = new Properties();

        for (String propertiesName : propertiesNames) {
            Optional<Properties> properties = getProperties(propertiesName);
            if (properties.isPresent()) {
                Properties properties1 = properties.get();
                for (String name : properties1.stringPropertyNames()) {
                    Object o = properties1.get(name);
                    res.put(name, o);
                }
            }
        }
        return Optional.of(res);
    }

    /**
     * 获取配置文件
     *
     * @param key 键
     * @return 值
     */
    public static String getConfig(String key) {
        return Optional.ofNullable(properties.get(key)).map(String::valueOf).orElse(null);
    }

    public static Integer getInt(String key) {
        return Integer.parseInt(getConfig(key));
    }

    /**
     * 获取配置文件内容
     *
     * @param path 路径
     * @return String
     */
    public static String getFileContent(String path) {
        try (
                InputStream resourceAsStream = ResourceUtils.class.getResourceAsStream(path);
                InputStreamReader in = new InputStreamReader(resourceAsStream);
                BufferedReader bufferedReader = new BufferedReader(in)
        ) {
            return bufferedReader.lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            return null;
        }
    }

}
