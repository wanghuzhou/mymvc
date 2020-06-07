package com.wanghz.mymvc.context;

import com.wanghz.mymvc.annotation.*;
import com.wanghz.mymvc.common.util.SqlSessionFactoryUtil;
import com.wanghz.mymvc.common.util.VariableUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletContextEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
    private static String basePackage;
    private static final String BASE_PACKAGE_NAME = "base-package";
    // 基包下面所有带包路径全限定类名
    private static final List<String> classFullNames = new ArrayList<>();
    // 注解实例化 注解名称: 实例化对象
    public static final Map<String, Object> instanceMap = new HashMap<>();
    // 带包路径的全限定名: 注解上的名称
    public static final Map<String, String> nameMap = new HashMap<>();
    // url和方法映射关系 springMvc 方法调用链
    public static final Map<String, Method> urlMethodMap = new HashMap<>();
    // method和全限定类名映射关系 通过method找到方法对象利用反射执行
    public static final Map<Method, String> methodPackageMap = new HashMap<>();

    // DAO层注解实例化 注解名称: 实例化对象
    public static final Map<String, Object> DAOMap = new HashMap<>();

    public static final String CONFIG_LOCATION_PARAM = "contextConfigLocation";

    public static void initContext(ServletContextEvent sce) {
//        basePackage = config.getInitParameter("base-package");
        String configLocationParam = sce.getServletContext().getInitParameter(CONFIG_LOCATION_PARAM);


        try {
            // 0.初始化配置文件变量
            VariableUtil.init(configLocationParam);
            basePackage = VariableUtil.getVariableValue(BASE_PACKAGE_NAME);
            if (StringUtils.isBlank(basePackage)) {
                throw new ClassNotFoundException("未设置扫包路径：请检查base-package的值");
            }

            // 1.扫描基包下面所有带包路径全限定类名
            scanBasePackage(basePackage);
            // 2.把所有带 @Controller @Service @Repository的类实例化到map中，key为注解上的名称
            instance(classFullNames);
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
        } catch (IOException e) {
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
//        URL url = Thread.currentThread().getContextClassLoader().getResource(basePackage.replace(".", "/"));
        String path = ContextScanHelper.class.getClassLoader().getResource(basePackage.replace(".", "/")).getPath();
        File basePackageFile = new File(path);
        System.out.println("scan: " + basePackageFile);
        File[] childFiles = basePackageFile.listFiles();
        if (childFiles != null) {
            for (File file : childFiles) {
                if (file.isDirectory()) {
                    scanBasePackage(basePackage + "." + file.getName());
                } else if (file.isFile()) {
                    // 去除如  com.wanghz.mymvc.servlet.DispatcherServlet.class 的class
                    classFullNames.add(basePackage + "." + file.getName().split("\\.")[0]);
                }
            }
        }
        /*try {
            JarFile jarFile = new JarFile(path);
            JarFile[] childJarFiles = jarFile.listFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
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
                DAOMap.put(repositoryName, clazz);
                instanceMap.put(repositoryName, clazz);
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
                    if (field.getType().isAnnotationPresent(Repository.class)
                            && field.getType().isAnnotationPresent(Repository.class)) {
                        String name = field.getAnnotation(Qualifier.class).value();
                        field.setAccessible(true);
//                        field.set(SqlSessionFactoryUtil.sqlSession.getMapper(field.getType()), instanceMap.get(name));
                        field.set(entry.getValue(), SqlSessionFactoryUtil.sqlSession.getMapper(field.getType()));
                    } else {
                        String name = field.getAnnotation(Qualifier.class).value();
                        field.setAccessible(true);
                        field.set(entry.getValue(), instanceMap.get(name));
                    }

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
        if (classFullNames.size() < 1) {
            return;
        }
        for (String classFullName : classFullNames) {
            Class clazz = Class.forName(classFullName);
            if (clazz.isAnnotationPresent(Controller.class)) {
                Method[] methods = clazz.getMethods();
                StringBuilder baseUrl = new StringBuilder();
                if (clazz.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping requestMapping = (RequestMapping) clazz.getAnnotation(RequestMapping.class);
                    baseUrl.append(requestMapping.value());
                }
                for (Method method : methods) {
                    if (method.isAnnotationPresent(RequestMapping.class)) {
                        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

                        urlMethodMap.put(baseUrl + requestMapping.value(), method);
                        methodPackageMap.put(method, classFullName);
                    }
                }
            }
        }
    }


}
