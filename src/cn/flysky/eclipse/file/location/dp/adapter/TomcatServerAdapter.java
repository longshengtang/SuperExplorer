package cn.flysky.eclipse.file.location.dp.adapter;

import org.eclipse.jst.server.tomcat.core.internal.TomcatServer;
import org.eclipse.wst.server.core.IServer;

/**
 * Tomcat适配器
 * 
 * @author longshengtang
 * 
 */
@SuppressWarnings("restriction")
public class TomcatServerAdapter implements IServerAdapter {

	TomcatServer adaptee;

	@Override
	public String getServerDeployDirectory(IServer server) {
		String path = null;
		if (server == null) {
			return path;
		}
		Object serverAdapter = server.loadAdapter(TomcatServer.class, null);
		adaptee = (TomcatServer) serverAdapter;
		if (adaptee != null && adaptee.getServerDeployDirectory() != null) {
			path = adaptee.getServerDeployDirectory().toOSString();
		}
		return path;
	}

}
