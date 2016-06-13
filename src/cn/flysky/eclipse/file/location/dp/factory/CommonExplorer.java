package cn.flysky.eclipse.file.location.dp.factory;

import java.io.IOException;
import java.lang.reflect.Field;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.IJarEntryResource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.internal.core.JarPackageFragmentRoot;
import org.eclipse.jdt.internal.core.PackageFragment;
import org.eclipse.jdt.internal.ui.packageview.ClassPathContainer;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;

import cn.flysky.eclipse.file.location.util.OpenAbleFile;

/**
 * 支持通用的浏览功能，但不具有Server目录浏览能力
 * 
 * @author longshengtang
 * 
 */
@SuppressWarnings("restriction")
public class CommonExplorer implements IExplorer {

	public CommonExplorer() {
		this.openCommand = "explorer";//$NON-NLS-1$
	}

	@Override
	public boolean isEnableOpenResource(ISelection currentSelection) {
		reset();
		if ((currentSelection == null) || (currentSelection.isEmpty())) {
			return false;
		}

		if ((currentSelection instanceof ITreeSelection)) {
			ITreeSelection treeSelection = (ITreeSelection) currentSelection;

			TreePath[] paths = treeSelection.getPaths();
			for (int i = 0; i < paths.length; i++) {
				TreePath path = paths[i];
				Object segment = path.getLastSegment();
				System.out.println(segment.getClass());
				if ((segment instanceof IResource)) {
					file.setResource((IResource) segment);
				} else if ((segment instanceof IClassFile)) {
					IClassFile icf = (IClassFile) segment;
					getFileLocation(icf);
				} else if ((segment instanceof JarPackageFragmentRoot)) {
					JarPackageFragmentRoot jpfr = (JarPackageFragmentRoot) segment;
					getFileLocation(jpfr);
				} else if ((segment instanceof IJarEntryResource)) {// 包含了JarEntyDirectory和JarEntryFile
					IJarEntryResource jer = (IJarEntryResource) segment;
					getFileLocation(jer);
				} else if ((segment instanceof PackageFragment)) {
					IPath pfPath = ((PackageFragment) segment).getPath();
					getFileLocation(pfPath);
				} else if ((segment instanceof IJavaElement)) {
					file.setResource(((IJavaElement) segment).getResource());
				} else if ((segment instanceof ClassPathContainer)) {
				} else {// 其它类型
					fromServer(segment);
				}

			}
		} else if (((currentSelection instanceof ITextSelection))
				|| ((currentSelection instanceof IStructuredSelection))) {
			IEditorPart editor = this.window.getActivePage().getActiveEditor();
			// 从编辑器来获得编辑文件
			IEditorInput editorInput = editor.getEditorInput();
			if (editor != null) {
				file.setResource((IResource) editorInput
						.getAdapter(IResource.class));
			}
			if (file.isOpenable()) {
				// 通过反射获取私有的file属性字段——从而获取该jar包真实路径
				try {
					// 反射的方式是由IPackageFragmentRoot的exists()方法想到的，因为exists里调用了File.exists()
					Field[] fields = editorInput.getClass().getDeclaredFields();
					Field.setAccessible(fields, true);
					for (Field field : fields) {
						Object fieldValue;
						fieldValue = field.get(editorInput);

						if (fieldValue instanceof IJarEntryResource) {
							IJarEntryResource jer = (IJarEntryResource) fieldValue;
							if (getFileLocation(jer)) {
								break;
							}
						} else if (fieldValue instanceof IClassFile) {
							IClassFile icf = (IClassFile) fieldValue;
							if (getFileLocation(icf)) {
								break;
							}
						}// 如果遇到其它类，可以延伸下去
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}

		}

		if (file.isOpenable()) {
			return true;
		}

		return false;
	}

	@Override
	public void fromServer(Object segment) {
	}

	@Override
	public void init(IWorkbenchWindow window) {
		this.window = window;
		this.shell = this.window.getShell();
	}

	@Override
	public void run(IAction action) {
		if (!file.isOpenable()) {
			return;
		}
		StringBuilder command = new StringBuilder(this.openCommand);
		String location = null;
		if (file.getResource() != null) {
			location = file.getResource().getLocation().toOSString();
			if ((file.getResource() instanceof IFile)) {
				// 修改路径
				location = ((IFile) file.getResource()).getLocation()
						.toOSString();
				// 打开并选中文件
				command.append(" /select,");//$NON-NLS-1$
			}
		} else if (file.isOpenable()) {
			location = file.getFileOrDir();
			if (file.isSelected()) {
				command.append(" /select,");//$NON-NLS-1$
			}
		}

		if (location != null) {
			location = location.replaceAll("/", "\\\\");//$NON-NLS-1$ //$NON-NLS-2$// windows的斜杠
		}

		openFileOrDirectory(command.toString(), location);
	}

	/**
	 * 打开目录或者文件所在目录
	 * 
	 * @param command
	 *            打开命令
	 * @param location
	 *            文件或者目录完全限定名称
	 */
	private void openFileOrDirectory(String command, String location) {
		try {
			Runtime.getRuntime().exec(command + " \"" + location + "\"");//$NON-NLS-1$ $NON-NLS-2$
		} catch (IOException e) {
			e.printStackTrace();
			MessageDialog.openError(this.shell,
					"文件浏览出错", "不能打开 \"" + location + "\"");//$NON-NLS-1$ $NON-NLS-2$ $NON-NLS-3$
		}
	}

	/**
	 * 重置上次结果
	 */
	private void reset() {
		file.clear();
	}

	public void getFileLocation(IPath path) {
		if (path != null) {
			file.setFileOrDirAndTrue(path.toOSString());
		}
		if (!file.isOpenable()) {
			// 再从当前工作空间中取路径
			file.setFileOrDirAndTrue(getLocationFromWorkspace(path));
		}
	}

	public void getFileLocation(JarPackageFragmentRoot jpfr) {
		file.setFileOrDirAndTrue(jpfr.getPath().toOSString());
		if (!file.isOpenable()) {
			if (!file.isOpenable()) {
				try {
					String name = jpfr.getJar().getName();
					file.setFileOrDirAndTrue(name);
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public boolean getFileLocation(IClassFile icf) {
		if (icf != null) {
			file.setFileOrDirAndTrue(icf.getPath().toOSString());
			if (!file.isOpenable()) {// 防止是相对路径
				String localPath = getLocationFromWorkspace(icf.getPath());
				file.setFileOrDirAndTrue(localPath);
			}

		}
		return file.isOpenable();
	}

	public boolean getFileLocation(IJarEntryResource jer) {
		if (jer == null) {
			return false;
		}
		IPackageFragmentRoot pfr = jer.getPackageFragmentRoot();
		IPath pfrPath = pfr.getPath();
		if (pfrPath != null)
			file.setFileOrDirAndTrue(pfrPath.toOSString());

		if (!file.isOpenable()) {
			file.setFileOrDirAndTrue(getLocationFromWorkspace(pfrPath));
		}
		return file.isOpenable();
	}

	/**
	 * 从工具空间中获取文件位置
	 * 
	 * @param path
	 *            文件路径(可以是相对或者绝对路径)
	 * @return
	 */
	public String getLocationFromWorkspace(IPath path) {

		if (path == null || root == null) {
			return "";
		}

		IResource resource = root.findMember(path);
		if (resource != null) {
			IPath location = resource.getLocation();
			if (location != null) {
				return location.toOSString();
			}
		}

		return "";
	}

	private IWorkbenchWindow window = null;
	private Shell shell;

	/**
	 * 打开文件的命令
	 */
	private String openCommand;

	/**
	 * 特殊待的可开文件:只能修改属性值，不能改变对象引用
	 */
	final OpenAbleFile file = new OpenAbleFile();

	/**
	 * 工具空间根路径:用于当位置不对时候，的路径获取
	 */
	private IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
}
