package cn.flysky.eclipse.file.location.util;

import java.io.File;

import org.eclipse.core.resources.IResource;

/**
 * 保存待打开文件信息
 * 
 * @author longshengtang
 * 
 */
public class OpenAbleFile {

	/**
	 * 是否可打开
	 * 
	 * @return
	 */
	public boolean isOpenable() {

		if (resource != null) {
			return true;
		}

		if (fileOrDir == null || fileOrDir.length() == 0) {
			return false;
		}

		// 应该还有更简单的判定文件是否存在的方法-不用new对象
		return new File(this.fileOrDir).exists();
	}

	/**
	 * 将对象清空
	 */
	public void clear() {
		isSelected = false;
		fileOrDir = null;
		resource = null;
	}

	public String getFileOrDir() {
		return fileOrDir;
	}

	public void setFileOrDir(String fileOrDir) {
		this.fileOrDir = fileOrDir;
	}

	public void setFileOrDirAndTrue(String fileOrDir) {
		this.fileOrDir = fileOrDir;
		this.isSelected = true;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public IResource getResource() {
		return resource;
	}

	public void setResource(IResource resource) {
		this.resource = resource;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	@Override
	public String toString() {
		return "MyOpenFile [fileOrDir=" + fileOrDir + ", isSelected="
				+ isSelected + "]";
	}

	/**
	 * 特殊的可打开的文件
	 */
	private String fileOrDir = null;

	/**
	 * 用于记录当前可打开的文件或者目录资源
	 */
	private IResource resource = null;

	/**
	 * 是否需要选中
	 */
	private boolean isSelected = false;

}