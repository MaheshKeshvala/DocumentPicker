package com.mahesh.documentpicker2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;


public class DocumentPickerClass {
    private static final int PERMISSION_ALL = 1;
    private static int DOCUMENT_PICKER_CODE = 0;
    private static File file;
    private static ArrayList<File> fileList = new ArrayList<>();
    private static boolean allowsMultiple = false;

    static String[] PERMISSIONS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };


    public static void browseDocuments(Activity context, int requestCode, boolean allowMultiple) {
        fileList = new ArrayList<>();
        hasPermissions(context, PERMISSIONS);
        DOCUMENT_PICKER_CODE = requestCode;
        allowsMultiple = allowMultiple;
        if (!hasPermissions(context, PERMISSIONS)) {
            ActivityCompat.requestPermissions(context, PERMISSIONS, PERMISSION_ALL);
        } else {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            String[] mimetypes = {"image/*", "application/pdf", "video/*"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, allowsMultiple);
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            context.startActivityForResult(Intent.createChooser(intent, "ChooseFile"), requestCode);
        }
    }


    @SuppressLint("NewApi")
    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        final boolean needToCheckUri = Build.VERSION.SDK_INT >= 19;
        String selection = null;
        String[] selectionArgs = null;

        // Uri is different in versions after KITKAT (Android 4.4), we need to
        // deal with different Uris.
        if (needToCheckUri && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                if (id.startsWith("raw:")) {
                    return id.replaceFirst("raw:", "");
                }
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{split[1]};
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.getMessage();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }


    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }


    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }


    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    public static ArrayList<File> onActivityResult(Context context, int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == DOCUMENT_PICKER_CODE) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    if (data.getClipData() != null) {
                        return getFileList(data.getClipData(), context);
                    } else {
                        return getFile(data.getData(), context);
                    }
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static ArrayList<File> getFileList(ClipData data, Context context) throws URISyntaxException {
        ClipData mClipData = data;
        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
        for (int i = 0; i < mClipData.getItemCount(); i++) {
            ClipData.Item item = mClipData.getItemAt(i);
            Uri uri = item.getUri();
            String path = getPath(context, uri);
            ArrayList<String> video_extentions = new ArrayList<>();
            video_extentions.add("3g2");
            video_extentions.add("mp4");
            video_extentions.add("mkv");
            video_extentions.add("3gp");
            video_extentions.add("asf");
            video_extentions.add("avi");
            video_extentions.add("flv");
            video_extentions.add("m4v");
            video_extentions.add("mov");
            video_extentions.add("mpg");
            video_extentions.add("rm");
            video_extentions.add("srt");
            video_extentions.add("swf");
            video_extentions.add("vob");
            video_extentions.add("wmv");
            video_extentions.add("pdf");
            video_extentions.add("jpg");
            video_extentions.add("png");
            video_extentions.add("jpeg");

            if (path != null) {
                file = new File(path);
                int extensionDelimiter = file.getPath().lastIndexOf(".");
                String extension1 = "";
                if (extensionDelimiter != -1) {
                    extension1 = file.getPath().substring(extensionDelimiter + 1, file.getPath().length());
                }

                //max size 10MB Modified by mahesh 150319
                if (file != null && file.length() > 0) {
                    int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));
                    if (file_size > 10240) {
                        Toast.makeText(context, "The file you are trying to send is too large.Please modify it before sending.", Toast.LENGTH_SHORT).show();
                        return null;
                    }
                }

                if (extension1 != null && !extension1.equals("") && video_extentions.contains(extension1)) {
                    fileList.add(file);
                    if (i == mClipData.getItemCount() - 1) {
                        return fileList;
                    }
                } else {
                    Toast.makeText(context, "File Formate not supported.", Toast.LENGTH_LONG).show();
                }
            }
        }
        return null;
    }


    private static ArrayList<File> getFile(Uri data, Context context) throws URISyntaxException {
        String path = getPath(context, data);
        ArrayList<String> video_extentions = new ArrayList<>();
        video_extentions.add("3g2");
        video_extentions.add("mp4");
        video_extentions.add("mkv");
        video_extentions.add("3gp");
        video_extentions.add("asf");
        video_extentions.add("avi");
        video_extentions.add("flv");
        video_extentions.add("m4v");
        video_extentions.add("mov");
        video_extentions.add("mpg");
        video_extentions.add("rm");
        video_extentions.add("srt");
        video_extentions.add("swf");
        video_extentions.add("vob");
        video_extentions.add("wmv");
        video_extentions.add("pdf");
        video_extentions.add("jpg");
        video_extentions.add("png");
        video_extentions.add("jpeg");

        if (path != null) {
            file = new File(path);
            int extensionDelimiter = file.getPath().lastIndexOf(".");
            String extension1 = "";
            if (extensionDelimiter != -1) {
                extension1 = file.getPath().substring(extensionDelimiter + 1, file.getPath().length());
            }

            //max size 10MB Modified by mahesh 150319
            if (file != null && file.length() > 0) {
                int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));
                if (file_size > 10240) {
                    Toast.makeText(context, "The file you are trying to send is too large.Please modify it before sending.", Toast.LENGTH_SHORT).show();
                    return null;
                }
            }

            if (extension1 != null && !extension1.equals("") && video_extentions.contains(extension1)) {
                fileList.add(file);
                return fileList;
            } else {
                Toast.makeText(context, "File Formate not supported.", Toast.LENGTH_LONG).show();
            }
        }
        return null;
    }


    public static boolean hasPermissions(Activity context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void onRequestPermissionsResult(Activity context, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            String[] mimetypes = {"image/*", "application/pdf", "video/*"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, allowsMultiple);
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            context.startActivityForResult(Intent.createChooser(intent, "ChooseFile"), DOCUMENT_PICKER_CODE);
        }
    }

}
