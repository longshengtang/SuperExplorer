package cn.flysky.eclipse.file.location.dp.factory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.ui.internal.view.servers.ModuleServer;

import cn.flysky.eclipse.file.location.dp.adapter.IServerAdapter;
import cn.flysky.eclipse.file.location.util.ServerHelpler;

/**
 * 在CommonExplorer基础上增加浏览Tomcat等等Server目录的功能
 * 
 * @author longshengtang
 * 
 */
@SuppressWarnings("restriction")
public class ServerExplorer extends CommonExplorer {

	public void fromServer(Object segment) {
		if ((segment instanceof IServer)) {
			IServer server = (IServer) segment;
			String serverDeployDirectory = getServerDeployDir(server);
			file.setFileOrDir(serverDeployDirectory);
		} else if ((segment instanceof ModuleServer)) {
			ModuleServer ms = (ModuleServer) segment;
			getFilePathFromModuleServer(ms);
		}
	}

	public ServerExplorer() {
		// 获取Server适配器类
		serverAdapterFactory();
	}

	/**
	 * 产生Server适配器对象
	 */
	private void serverAdapterFactory() {
		for (Map.Entry<String, String> entry : ServerHelpler.serverMap
				.entrySet()) {
			String className = entry.getValue();
			IServerAdapter adapter = ServerHelpler.getInstance(className,
					IServerAdapter.class);
			if (adapter != null) {
				serverTargetList.add(adapter);
			}
		}
	}

	/**
	 * 获取部署路径<br>
	 * 目前只支持从tomcat中获取路径
	 * 
	 * @param server
	 * @return
	 */
	public String getServerDeployDir(IServer server) {
		String path = null;

		if (server == null) {
			return path;
		}

		// 循环所有Server，直到获取真实路径。
		for (IServerAdapter target : serverTargetList) {
			path = target.getServerDeployDirectory(server);
			if (isNotEmpty(path)) {
				return path;
			}
		}

		return path;
	}

	private boolean isNotEmpty(String path) {
		return path != null && path.length() != 0;
	}

	/**
	 * 从ModuleServer中获取真实路径
	 * 
	 * @param ms
	 */
	public void getFilePathFromModuleServer(ModuleServer ms) {
		if (ms == null) {
			return;
		}
		IServer server = ms.getServer();
		String serverDeployDir = getServerDeployDir(server);
		IModule[] modules = ms.getModule();
		if (modules == null || modules.length < 1) {
			return;
		}
		IModule module = modules[0];
		String dir = serverDeployDir + File.separator + module.getName();
		file.setFileOrDir(dir);
	}

	/**
	 * 所支持的服务列表。后续版本会根据用户最常使用的Server进行优化判定的顺序，目前优先判定Tomcat
	 */
	private List<IServerAdapter> serverTargetList = new ArrayList<IServerAdapter>();
}
