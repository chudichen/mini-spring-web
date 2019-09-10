package com.michael.core;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Class扫描器
 *
 * @author Michael Chu
 * @since 2019-09-09 15:51
 */
@Slf4j
public class ClassScanner {

    /**
     * 扫描包下面所有的class对象
     *
     * @param packageName 包路径
     * @return class集合
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws URISyntaxException
     */
    public static List<Class<?>> scanClass(String packageName) throws IOException, ClassNotFoundException, URISyntaxException {
        // 用于保存结果的容器
        List<Class<?>> classList = new ArrayList<>();
        // 把文件名改为文件路径
        String path = packageName.replace(".", "/");
        // 获取默认的类加载器
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        // 通过文件路径获取改文件夹下所有资源的URL
        Enumeration<URL> resources = classLoader.getResources(path);

        while (resources.hasMoreElements()) {
            // 拿到下一个资源
            URL resource = resources.nextElement();
            // 先判断是否是Jar包，因为默认.class文件被打包成jar包
            if (resource.getProtocol().contains("jar")) {
                // 把URL强转为jar包链接
                JarURLConnection jarURLConnection = (JarURLConnection) resource.openConnection();
                // 根据jar包获取jar包的路径名
                String jarFilePath = jarURLConnection.getJarFile().getName();
                // 把jar包下所有的类添加的结果保存在容器中
                classList.addAll(getClassFromJar(jarFilePath, path, classLoader));
            } else if (resource.getProtocol().contains("file")) {
                File file = new File(resource.toURI().getPath());
                classList.addAll(getAllClass(file, classLoader, packageName));
            }
        }
        return classList;
    }

    /**
     * 通过jar包获取所有路径符合的类文件
     *
     * @param jarFilePath jar包的路径
     * @param path 类路径
     * @param classLoader 类加载器
     * @return 加载的Class
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static List<Class<?>> getClassFromJar(String jarFilePath, String path, ClassLoader classLoader) throws IOException, ClassNotFoundException {
        // 保存结果的集合
        List<Class<?>> classes = new ArrayList<>();
        // 创建对应jar包的句柄
        JarFile jarFile = new JarFile(jarFilePath);
        // 拿到jar包中所有的文件
        Enumeration<JarEntry> jarEntries = jarFile.entries();
        while (jarEntries.hasMoreElements()) {
            // 拿到一个文件
            JarEntry jarEntry = jarEntries.nextElement();
            // 拿到文件名
            String entryName = jarEntry.getName();
            // 判断是否是类文件
            if (entryName.startsWith(path) && entryName.endsWith(".class")) {
                classes.add(getClass(entryName, classLoader));
            }
        }
        return classes;
    }

    /**
     * 使用类加载器加载class文件
     *
     * @param entryName 待处理路径
     * @param classLoader 类加载器
     * @return {@link Class} 对象
     * @throws ClassNotFoundException 路径错误未找到对应的class
     */
    private static Class<?> getClass(String entryName, ClassLoader classLoader) throws ClassNotFoundException {
        String classFullName = entryName.replace("/", ".")
                .substring(0, entryName.length() - 6);
        return classLoader.loadClass(classFullName);
    }

    /**
     * 递归加载路径下所有的class
     *
     * @param file 文件路径
     * @param classLoader 类加载器
     * @param packageName 包名
     * @return 所有的class集合
     */
    private static List<Class<?>> getAllClass(File file, ClassLoader classLoader, String packageName) {
        List<Class<?>> classList = new ArrayList<>();
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File subFile : files) {
                    if (subFile.isDirectory()) {
                        classList.addAll(getAllClass(subFile, classLoader, packageName + "." + subFile.getName()));
                    } else {
                        classList.addAll(getAllClass(subFile, classLoader, packageName));
                    }
                }
            }
        } else {
            try {
                String className = file.getName().substring(0,
                        file.getName().length() - 6);
                // 添加到集合中去
                // 经过回复同学的提醒，这里用forName有一些不好，会触发static方法，
                // 没有使用classLoader的load干净
                classList.add(classLoader.loadClass(packageName + '.' + className));
            } catch (ClassNotFoundException e) {
                log.error("Load class failure", e);
            }
        }
        return classList;
    }
}
