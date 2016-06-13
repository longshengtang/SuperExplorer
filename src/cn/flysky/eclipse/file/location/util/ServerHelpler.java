package cn.flysky.eclipse.file.location.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Server辅助工具，目前仅仅完成对Tomcat的支持。<br>
 * （若需要对其它Server支持，只需要实现剩余的方法即可）
 * 
 * @author longshengtang
 * 
 */
public class ServerHelpler {

	/**
	 * 是否支持Server
	 * 
	 * @return
	 */
	public static final boolean isSupportedServer() {
		return isSupportedTomcatServer() || isSupportedWeblogicServer()
				|| isSupportedJbossServer();
	}

	/**
	 * 是否支持TomcatServer
	 * 
	 * @return
	 */
	public final static boolean isSupportedTomcatServer() {
		if (serverMap.containsKey(Messages.ServerTomcatKey)) {
			return true;
		}
		// 判定插件是否存在
		if (isClassExists(Messages.TomcatNeedIServer)
				&& isClassExists(Messages.TomcatNeedModuleServer)
				&& isClassExists(Messages.TomcatNeedTomcatServer)) {

			if (!serverMap.containsKey(Messages.ServerTomcatKey)) {
				serverMap.put(Messages.ServerTomcatKey,
						Messages.ServerTomcatValue);
			}
			return true;
		}
		return false;
	}

	/**
	 * 是否支持WeblogicServer
	 * 
	 * @return
	 */
	public final static boolean isSupportedWeblogicServer() {
		return false;
	}

	/**
	 * 是否支持JbossServer
	 * 
	 * @return
	 */
	public final static boolean isSupportedJbossServer() {
		return false;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getInstance(String clasName, Class<T> targetClazz) {
		T ret = null;
		try {
			Class<?> clazz = getClass4Name(clasName);
			ret = (T) clazz.newInstance();

		} catch (Throwable e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static boolean isClassExists(String className) {
		Class<?> clazz = getClass4Name(className);
		return clazz != null;
	}

	public static Class<?> getClass4Name(String className) {
		Class<?> clazz = null;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (Throwable e) {
		}

		return clazz;
	}

	/**
	 * 存放IServerAdapter的实现类
	 */
	public final static Map<String, String> serverMap = new LinkedHashMap<String, String>();
}
