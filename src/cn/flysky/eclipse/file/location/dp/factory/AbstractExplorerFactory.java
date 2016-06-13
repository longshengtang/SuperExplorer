package cn.flysky.eclipse.file.location.dp.factory;

/**
 * Explorer对象工厂
 * 
 * @author longshengtang
 * 
 */
public abstract class AbstractExplorerFactory {
	/**
	 * 生产IExplorer子类对象。
	 * 
	 * @return
	 */
	public abstract IExplorer factory();
}
