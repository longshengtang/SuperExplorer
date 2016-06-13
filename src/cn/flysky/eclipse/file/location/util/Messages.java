package cn.flysky.eclipse.file.location.util;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "cn.flysky.eclipse.file.location.util.messages"; //$NON-NLS-1$
	public static String ServerJbossKey;
	public static String ServerJbossValue;
	public static String ServerTomcatKey;
	public static String ServerTomcatValue;
	public static String ServerWeblogicKey;
	public static String ServerWeblogicValue;
	public static String CommonExplorer;
	public static String ServerExplorer;
	// 支持Tomcat Server所必须的类
	public static String TomcatNeedIServer;
	public static String TomcatNeedModuleServer;
	public static String TomcatNeedTomcatServer;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
