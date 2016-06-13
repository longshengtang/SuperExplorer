package cn.flysky.eclipse.file.location.dp.factory;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;

/**
 * 为了兼容eclipse的jse及jee版本而抽象出来的目录浏览接口。<br>
 * 该接口实例由AbstractExplorerFactory工厂方法的具体子类生产
 * 
 * @author longshengtang
 * 
 */
public interface IExplorer {

	/**
	 * 选择的东西是否可以打开，并且不是选择文件里的字符
	 * 
	 * @return
	 */
	boolean isEnableOpenResource(ISelection currentSelection);

	/**
	 * 默认没有server实现。使用模板方法实现。子类可以覆盖此方法来支持各种Server的操作。
	 * 
	 * @param segment
	 */
	void fromServer(Object segment);

	void init(IWorkbenchWindow window);

	void run(IAction action);

}