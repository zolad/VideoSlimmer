package com.zolad.videoslimmer.sample;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;

public class PermissionUtil {


	public static boolean isNeedPermission(Activity context, String permission, int requestCode) {
		if (!isGranted(context, permission)) {


			ActivityCompat.requestPermissions(context, new String[] { permission }, requestCode);
			return true;
		} else {

			return false;
		}
	}


	public static boolean isNeedPermission(Activity context, String permission) {
		if (!isGranted(context, permission)) {


			ActivityCompat.requestPermissions(context, new String[] { permission }, 99);
			return true;
		} else {


			return false;
		}
	}


	public static boolean isNeedPermissionForStorage(Activity context) {
		if (!isGranted(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
				|| !isGranted(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {

			ActivityCompat.requestPermissions(context, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE,
					Manifest.permission.READ_EXTERNAL_STORAGE }, 99);
			return true;
		} else {

			return false;
		}
	}

	@SuppressLint("NewApi")
	public static boolean isGranted(Context context, String permission) {

		boolean result = true;

		int targetSdkVersion = 23;

		try {
			final PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			targetSdkVersion = info.applicationInfo.targetSdkVersion;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (targetSdkVersion >= Build.VERSION_CODES.M) {

				result = context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
			} else {

				result = PermissionChecker.checkSelfPermission(context,
						permission) == PermissionChecker.PERMISSION_GRANTED;
			}
		}

		return result;
	}




}
