package cn.flysky.eclipse.file.location.dp.adapter;

import org.eclipse.wst.server.core.IServer;

/**
 * Server适配器<br>
 * 使用统一方式工获取不同Server的路径
 * 
 * @author longshengtang
 * 
 */
public interface IServerAdapter {
	/**
	 * 获取服务部署目录
	 * 
	 * @param server
	 * @return
	 */
	String getServerDeployDirectory(IServer server);
}
