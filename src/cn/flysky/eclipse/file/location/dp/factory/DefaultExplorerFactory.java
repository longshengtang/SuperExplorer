package cn.flysky.eclipse.file.location.dp.factory;

import cn.flysky.eclipse.file.location.util.Messages;
import cn.flysky.eclipse.file.location.util.ServerHelpler;

/**
 * 生产适合当前eclipse版本的IExplorer具体子类对象
 * 
 * @author longshengtang
 * 
 */
public class DefaultExplorerFactory extends AbstractExplorerFactory {

	@Override
	public IExplorer factory() {
		Class<IExplorer> target = IExplorer.class;
		IExplorer explorer = null;
		if (ServerHelpler.isSupportedServer()) {
			explorer = ServerHelpler.getInstance(Messages.ServerExplorer,
					target);
		}

		if (explorer == null) {
			// 如果获取失败，则获取通用的浏览工具
			explorer = ServerHelpler.getInstance(Messages.CommonExplorer,
					target);
		}

		return explorer;
	}
}
