package com.wanghz.mymvc.context;

import com.wanghz.mymvc.annotation.*;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 上下文环境扫描
 *
 * @author wanghz
 */
public class ContextScanHelper {


    // 扫描的基包
    private static String basePackage = "com.wanghz.mymvc";
    // 基包下面所有带包路径全限定类名
    private static final List<String> packageNames = new ArrayList<>();
    // 注解实例化 注解名称: 实例化对象
    public static final Map<String, Object> instanceMap = new HashMap<>();
    // 带包路径的全限定名: 注解上的名称
    public static final Map<String, String> nameMap = new HashMap<>();
    // url和方法映射关系 springMvc 方法调用链
    public static final Map<String, Method> urlMethodMap = new HashMap<>();
    // method和全限定类名映射关系 通过method找到方法对象利用反射执行
    public static final Map<Method, String> methodPackageMap = new HashMap<>();


    public static void initContext() {
//        basePackage = config.getInitParameter("base-package");
        try {
            // 1.扫描基包下面所有带包路径全限定类名
            scanBasePackage(basePackage);
            // 2.把所有带 @Controller @Service @Repository的类实例化到map中，key为注解上的名称
            instance(packageNames);
            // 3.springIOC注入
            springIOC();
            // 4.完成url地址和method的映射关系
            handlerUrlMethodMap();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }


    /**
     * 扫描基包
     *
     * @param basePackage 基包名称
     */
    public static void scanBasePackage(String basePackage) {
//        URL url = this.getClass().getClassLoader().getResource(basePackage.replace(".", "/"));
        URL url = Thread.currentThread().getContextClassLoader().getResource(basePackage.replace(".", "/"));
        File basePackageFile = new File(url.getPath());
        System.out.println("scan: " + basePackageFile);
        File[] childFiles = basePackageFile.listFiles();
        if (childFiles != null) {
            for (File file : childFiles) {
                if (file.isDirectory()) {
                    scanBasePackage(basePackage + "." + file.getName());
                } else if (file.isFile()) {
                    // 去除如  com.wanghz.mymvc.servlet.DispatcherServlet.class 的class
                    packageNames.add(basePackage + "." + file.getName().split("\\.")[0]);
                }
            }
        }
    }

    /**
     * 实例化
     *
     * @param packageNames 基包下面所有带包路径全限定类名
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static void instance(List<String> packageNames) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        if (packageNames.size() < 1) {
            return;
        }
        for (String packageName : packageNames) {
            Class clazz = Class.forName(packageName);
            if (clazz.isAnnotationPresent(Controller.class)) {
                Controller controller = (Controller) clazz.getAnnotation(Controller.class);
                String controllerName = controller.value();

                instanceMap.put(controllerName, clazz.newInstance());
                nameMap.put(packageName, controllerName);
                System.out.println("Controller: " + packageName + ", value: " + controllerName);
            } else if (clazz.isAnnotationPresent(Service.class)) {
                Service service = (Service) clazz.getAnnotation(Service.class);
                String serviceName = service.value();

                instanceMap.put(serviceName, clazz.newInstance());
                nameMap.put(packageName, serviceName);
                System.out.println("Service: " + packageName + ", value: " + serviceName);
            } else if (clazz.isAnnotationPresent(Repository.class)) {
                Repository repository = (Repository) clazz.getAnnotation(Repository.class);
                String repositoryName = repository.value();

                instanceMap.put(repositoryName, clazz.newInstance());
                nameMap.put(packageName, repositoryName);
                System.out.println("Repository: " + packageName + ", value: " + repositoryName);
            }
        }
    }

    /**
     * springIOC注入
     *
     * @throws IllegalAccessException
     */
    public static void springIOC() throws IllegalAccessException {
        for (Map.Entry<String, Object> entry : instanceMap.entrySet()) {
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Qualifier.class)) {
                    String name = field.getAnnotation(Qualifier.class).value();
                    field.setAccessible(true);
                    field.set(entry.getValue(), instanceMap.get(name));
                }
            }
        }
    }

    /**
     * URL映射处理
     *
     * @throws ClassNotFoundException
     */
    public static void handlerUrlMethodMap() throws ClassNotFoundException {
        if (packageNames.size() < 1) {
            return;
        }
        for (String packageName : packageNames) {
            Class clazz = Class.forName(packageName);
            if (clazz.isAnnotationPresent(Controller.class)) {
                Method[] methods = clazz.getMethods();
                StringBuilder baseUrl = new StringBuilder();
                if (clazz.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping requestMapping = (RequestMapping) clazz.getAnnotation(RequestMapping.class);
                    baseUrl.append(requestMapping.value());
                }
                for (Method method : methods) {
                    if (method.isAnnotationPresent(RequestMapping.class)) {
                        RequestMapping requestMapping = (RequestMapping) clazz.getAnnotation(RequestMapping.class);
                        baseUrl.append(requestMapping.value());

                        urlMethodMap.put(baseUrl.toString(), method);
                        methodPackageMap.put(method, packageName);
                    }
                }
            }
        }
    }


}