package com.vortexwolf.dvach.interfaces;

import java.io.File;

import android.content.DialogInterface;

public interface IDownloadFileView {

	public void setProgress(int value);

	public void setMax(int value);

	public void showLoading(String message);

	public void hideLoading();

	public void setOnCancelListener(final DialogInterface.OnCancelListener listener);
	
	public void showSuccess(File file);
	
	public void showError(String error);

	public void showFileExists(File file);
}