package cn.flyaudio.module_music.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.util.Log;

import androidx.documentfile.provider.DocumentFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class DocumentsUtils {

    private static final String TAG = DocumentsUtils.class.getSimpleName();

    // U盘插入广播 的请求授权
    public static final int OPEN_DOCUMENT_TREE_CODE = 8000;
    public static final int OPEN_DOCUMENT_TREE_CODE_UDISK1 = 8002;

    public static final int OPEN_DOCUMENT_TREE_EVI = 8001;

    // 进入FileList 的请求授权
    public static final int OPEN_DOCUMENT_TREE_CODE_TO_FILELIST = 8003;
    public static final int OPEN_DOCUMENT_TREE_CODE_TO_FILELIST_UDISK1 = 8004;

    // 导出Log 的请求授权
    public static final int OPEN_DOCUMENT_TREE_CODE_TO_EXPORT_LOG = 8005;
    public static final int OPEN_DOCUMENT_TREE_CODE_TO_EXPORT_LOG_UDISK1 = 8006;


    private static List<String> sExtSdCardPaths = new ArrayList<>();
    private static String NAME = "com_flyaudio_flyfilemanager_DocumentsUtils";

    private DocumentsUtils() {

    }

    public static void cleanCache() {
        sExtSdCardPaths.clear();
    }

    /**
     * 获取外部SD卡路径列表。(Kitkat或更高)。
     *
     * @return A list of external SD card paths.
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static String[] getExtSdCardPaths(Context context) {
        if (sExtSdCardPaths.size() > 0) {
            return sExtSdCardPaths.toArray(new String[0]);
        }
        for (File file : context.getExternalFilesDirs("external")) {
            if (file != null && !file.equals(context.getExternalFilesDir("external"))) {
                int index = file.getAbsolutePath().lastIndexOf("/Android/data");
                if (index < 0) {
                    Log.w(TAG, "Unexpected external file dir: " + file.getAbsolutePath());
                } else {
                    String path = file.getAbsolutePath().substring(0, index);
                    try {
                        path = new File(path).getCanonicalPath();
                    } catch (IOException e) {
                        // Keep non-canonical path.
                    }
                    sExtSdCardPaths.add(path);
                }
            }
        }
        if (sExtSdCardPaths.isEmpty()) sExtSdCardPaths.add("/storage/sdcard1");
        return sExtSdCardPaths.toArray(new String[0]);
    }

    /**
     * Determine the main folder of the external SD card containing the given file.
     *
     * @param file the file.
     * @return The main folder of the external SD card containing this file, if the file is on an SD
     * card. Otherwise,
     * null is returned.
     * 确定包含给定文件的外部SD卡的主文件夹。
     * *
     * * @param文件。
     * * @如果文件在SD卡上，则返回包含该文件的外部SD卡的主文件夹
     * *卡。否则,
     * 返回null。
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static String getExtSdCardFolder(final File file, Context context) {
        String[] extSdPaths = getExtSdCardPaths(context);
        try {
            for (int i = 0; i < extSdPaths.length; i++) {
                Log.d(TAG, "外部SD卡路径：" + extSdPaths[i]);
                if (file.getCanonicalPath().startsWith(extSdPaths[i])) {
                    return extSdPaths[i];
                }
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }

    /**
     * Determine if a file is on external sd card. (Kitkat or higher.)
     *
     * @param file The file.
     * @return true if on external sd card.
     * 确定文件是否在外部sd卡上。(Kitkat或更高)。
     * *
     * * @param文件。
     * *如果在外部sd卡上，@return true。
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean isOnExtSdCard(final File file, Context c) {
        return getExtSdCardFolder(file, c) != null;
    }

    /**
     * Get a DocumentFile corresponding to the given file (for writing on ExtSdCard on Android 5).
     * If the file is not
     * existing, it is created.
     *
     * @param file        The file.
     * @param isDirectory flag indicating if the file should be a directory.
     * @return The DocumentFile
     * / * *
     * *获取与给定文件对应的文档文件(用于在Android 5的ExtSdCard上编写)。
     * *如果文件不是
     * *存在，它被创建。
     * *
     * * @param文件。
     * * @param isDirectory标志，指示文件是否应该是一个目录。
     * * @返回文档文件
     * * /
     */
    public static DocumentFile getDocumentFile(final File file, final boolean isDirectory, Context context) {

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            return DocumentFile.fromFile(file);
        }
        String baseFolder = getExtSdCardFolder(file, context);
        Log.i(TAG, "lum_ baseFolder " + baseFolder);
        boolean originalDirectory = false;
        if (baseFolder == null) {
            Log.d(TAG, "getDocumentFile baseFolder == null");
            return null;
        }
        String relativePath = null;
        try {
            String fullPath = file.getCanonicalPath();
            if (!baseFolder.equals(fullPath)) {
                relativePath = fullPath.substring(baseFolder.length() + 1);
            } else {
                originalDirectory = true;
            }
        } catch (IOException e) {
            return null;
        } catch (Exception f) {
            originalDirectory = true;
            //continue
        }

        // String as = PreferenceManager.getDefaultSharedPreferences(context).getString(baseFolder, null);
        SharedPreferences sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        String as = sharedPreferences.getString(baseFolder, null);
        Uri treeUri = null;
        if (as != null) {
            treeUri = Uri.parse(as);
        } else {
            Log.d(TAG, "getDocumentFile as == null");
        }
        if (treeUri == null) {
            Log.d(TAG, "getDocumentFile treeUri == null");
            return null;
        }
        // start with root of SD card and then parse through document tree.
        DocumentFile document = DocumentFile.fromTreeUri(context, treeUri);

        if (originalDirectory) {
            return document;
        }
        try {
            String[] parts = relativePath.split("/");
            for (int i = 0; i < parts.length; i++) {
                DocumentFile nextDocument = document.findFile(parts[i]);
                if (nextDocument == null) {
                    if ((i < parts.length - 1) || isDirectory) {
                        nextDocument = document.createDirectory(parts[i]);
                    } else {
                        nextDocument = document.createFile("image", parts[i]);
                    }
                }
                document = nextDocument;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return document;
    }

    public static boolean mkdirs(Context context, File dir) {
        boolean res = dir.mkdirs();
        if (!res) {
            if (DocumentsUtils.isOnExtSdCard(dir, context)) {
                DocumentFile documentFile = DocumentsUtils.getDocumentFile(dir, true, context);
                res = documentFile != null && documentFile.canWrite();
            }
        }
        return res;
    }

    public static boolean delete(Context context, File file) {
        boolean ret = file.delete();

        if (!ret && DocumentsUtils.isOnExtSdCard(file, context)) {
            DocumentFile f = DocumentsUtils.getDocumentFile(file, false, context);
            if (f != null) {
                ret = f.delete();
            }
        }
        return ret;
    }

    public static boolean canWrite(File file) {
        boolean res = file.exists() && file.canWrite();
        Log.d(TAG, "file.exists()：" + file.exists());
        Log.d(TAG, "ffile.canWrite()：" + file.canWrite());

        if (!res && !file.exists()) {
            try {
                if (!file.isDirectory()) {
                    Log.d(TAG, "canWrite panduan:" + file.getAbsolutePath() + "");
                    res = file.createNewFile() && file.delete();
                } else {
                    res = file.mkdirs() && file.delete();
                }
            } catch (IOException e) {
                res = false;
                e.printStackTrace();
            }
        }
        return res;
    }

    public static boolean canWrite(Context context, File file) {
        boolean res = canWrite(file);

        if (!res && DocumentsUtils.isOnExtSdCard(file, context)) {
            DocumentFile documentFile = DocumentsUtils.getDocumentFile(file, true, context);
            res = documentFile != null && documentFile.canWrite();
        }
        return res;
    }

    public static boolean renameTo(Context context, File src, File dest) {
        boolean res = src.renameTo(dest);

        if (!res && isOnExtSdCard(dest, context)) {
            DocumentFile srcDoc;
            if (isOnExtSdCard(src, context)) {
                srcDoc = getDocumentFile(src, false, context);
            } else {
                srcDoc = DocumentFile.fromFile(src);
            }
            DocumentFile destDoc = getDocumentFile(dest.getParentFile(), true, context);
            if (srcDoc != null && destDoc != null) {
                try {
                    if (src.getParent().equals(dest.getParent())) {
                        res = srcDoc.renameTo(dest.getName());
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        res = DocumentsContract.moveDocument(context.getContentResolver(),
                                srcDoc.getUri(),
                                srcDoc.getParentFile().getUri(),
                                destDoc.getUri()) != null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return res;
    }

    public static InputStream getInputStream(Context context, File destFile) {
        InputStream in = null;
        try {
            Log.d(TAG, "getInputStream: canWrite " + canWrite(destFile));
            Log.d(TAG, "getInputStream: isOnExtSdCard " + isOnExtSdCard(destFile, context));
            if (!canWrite(destFile) &&isOnExtSdCard(destFile, context)) {
                DocumentFile file = DocumentsUtils.getDocumentFile(destFile, false, context);
                if (file != null && file.canWrite()) {
                    in = context.getContentResolver().openInputStream(file.getUri());
                }else{
                    Log.d(TAG, "getInputStream: new FileInputStream(destFile)");
                    in = new FileInputStream(destFile);
                }
            } else {
                in = new FileInputStream(destFile);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return in;
    }

//    public boolean isUpanFile() {
//
//    }

    public static OutputStream getOutputStream(Context context, File destFile) {
        OutputStream out = null;
        try {
            if (isOnExtSdCard(destFile, context)) { //!canWrite(destFile) && isOnExtSdCard(destFile, context)
                // quan:这里先只要判断输出流是 U盘的话，则使用DocumentFile，否则，有可能出现 out = new FileOutputStream(destFile) 没权限
                DocumentFile file = DocumentsUtils.getDocumentFile(destFile, false, context);
                if (file != null && file.canWrite()) {
                    out = context.getContentResolver().openOutputStream(file.getUri());
                    Log.d(TAG, " out = context.getContentResolver().openOutputStream");
                }
            } else {
                Log.d(TAG, " out = new FileOutputStream(destFile)");
                out = new FileOutputStream(destFile);
            }
        } catch (FileNotFoundException e) {
            Log.d(TAG, destFile.getAbsolutePath() + "File Output Stream FileNotFoundException");
//            android.os.Process.killProcess(android.os.Process.myPid());
            e.printStackTrace();
        }
        return out;
    }

    public static boolean saveTreeUri(Context context, String rootPath, Uri uri) {
        DocumentFile file = DocumentFile.fromTreeUri(context, uri);
        if (file != null && file.canWrite()) {
//            SharedPreferences perf = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences perf = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
            perf.edit().putString(rootPath, uri.toString()).commit();
            Log.e(TAG, "save uri" + rootPath);
            return true;
        } else {
            Log.e(TAG, "no write permission: " + rootPath);
        }
        return false;
    }

    public static boolean checkWritableRootPath(Context context, String rootPath) {
        File root = new File(rootPath);
        if (!root.canWrite()) {
            if (DocumentsUtils.isOnExtSdCard(root, context)) {
                Log.i(TAG, "lum_ isOnExtSdCard");
                DocumentFile documentFile = DocumentsUtils.getDocumentFile(root, true, context);
//                if (documentFile != null) {
//                    Log.d(TAG, "chenxinquan documentFile != null");
//                }
//                if (documentFile.canWrite()) {
//                    Log.d(TAG, "chenxinquan documentFile.canWrite()");
//                }
                return documentFile == null || !documentFile.canWrite();

            } else {
                Log.i(TAG, "lum_2 get perf");
                //SharedPreferences perf = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences perf = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
                String documentUri = perf.getString(rootPath, "");
                if (documentUri == null || documentUri.isEmpty()) {
                    return true;
                } else {
                    DocumentFile file = DocumentFile.fromTreeUri(context, Uri.parse(documentUri));
                    return !(file != null && file.canWrite());
                }
            }
        }
        return false;
    }
}

