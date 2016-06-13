package cn.flysky.eclipse.file.location.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import cn.flysky.eclipse.file.location.dp.factory.DefaultExplorerFactory;
import cn.flysky.eclipse.file.location.dp.factory.AbstractExplorerFactory;
import cn.flysky.eclipse.file.location.dp.factory.IExplorer;

/**
 * @author longshengtang
 * 
 */
public class FileLocationAction implements IActionDelegate,
		IWorkbenchWindowActionDelegate {

	public FileLocationAction() {
		// 根据eclipse不同版本，创建不同浏览方案
		fitJseAndJee();
	}

	/**
	 * 适配eclipse的jse版本及jee版本
	 */
	private void fitJseAndJee() {
		AbstractExplorerFactory factory = new DefaultExplorerFactory();
		explorer = factory.factory();
	}

	public void init(IWorkbenchWindow window) {
		explorer.init(window);
	}

	public void dispose() {
	}

	public void run(IAction action) {
		explorer.run(action);
	}

	public void selectionChanged(IAction action, ISelection selection) {
		try {
			action.setEnabled(explorer.isEnableOpenResource(selection));
		} catch (Exception e) {
			e.printStackTrace();
			//MessageDialog.openError(null, "error", e.toString());//$NON-NLS-1$
		}
	}

	/**
	 * 默认使用不支持Server浏览的工具
	 */
	private IExplorer explorer = null;

}