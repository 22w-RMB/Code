package cn.flyaudio.module_music.scan;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;

import androidx.databinding.ObservableArrayList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.flyaudio.module_music.bean.Album;
import cn.flyaudio.module_music.bean.Folder;
import cn.flyaudio.module_music.bean.MediaFile;
import cn.flyaudio.module_music.bean.Singer;
import cn.flyaudio.module_music.bean.Song;
import cn.flyaudio.module_music.module.FlyStorageManager;
import cn.flyaudio.module_music.util.Constants;
import cn.flyaudio.module_music.util.StringUtils;
import me.goldze.mvvmhabit.utils.KLog;

/**
 * MediaScanner 媒体扫描用
 *
 * @author zengyuke
 */
public class MediaScanner {

    public static final String TAG = "MediaScanner";
    private static final String[] REGEXS = new String[]{".wma"};
    private int musicCount = 0;
    private Boolean ISSCANNING = false;

    private Context mContext;
    private Boolean LIMIT_TIME = true;
    private IScanUICallback mScanUICallback;
    private Boolean USBREMOVE = false;
    private boolean mUpateTimerRunning = false;

    private static volatile MediaScanner mInstance;


    public static MediaScanner getInstance(Context context, IScanUICallback mUICallback) {
        if (mInstance == null) {
            synchronized (MediaScanner.class) {
                if (mInstance == null) {
                    mInstance = new MediaScanner(context, mUICallback);
                }
            }
        }
        return mInstance;
    }

    /**
     * 构造
     *
     * @param context
     * @param mUICallback 更新UI和扫描通知
     */
    public MediaScanner(Context context, IScanUICallback mUICallback) {
        mContext = context;
        mScanUICallback = mUICallback;
    }

    private List<String> list;

    /**
     * View点击事件处理
     *
     * @param tag
     * @param bundle
     */
    public void onViewEvent(int tag, Bundle bundle) {
        doScanandPaser();
    }


    List<Song> songList = new ArrayList<>();
    List<Album> albumList = new ArrayList<>();
    List<Folder> folderList = new ArrayList<>();
    List<Singer> singerList = new ArrayList<>();


    private void addToAlbum(Song song) {
        if (song == null || TextUtils.isEmpty(song.getAlbumName())) {
            return;
        }
        int targetPosition = -1;
        for (int i = 0; i < albumList.size(); i++) {
            if (song.getAlbumName().equals(albumList.get(i).getName())) {
                targetPosition = i;
                break;
            }
        }
        if (targetPosition != -1) {
            albumList.get(targetPosition).getList().add(song);
        } else {
            Album album = new Album();
            ObservableArrayList<Song> list = new ObservableArrayList<>();
            list.add(song);
            album.setSinger(song.getSinger());
            album.setName(song.getAlbumName());
            album.setList(list);
            albumList.add(album);
        }
    }

    private void addToFolder(Song song) {
        if (song == null || TextUtils.isEmpty(song.getParentPath())) {
            return;
        }
        int targetPosition = -1;
        for (int i = 0; i < folderList.size(); i++) {
            if (song.getParentPath().equals(folderList.get(i).getPath())) {
                targetPosition = i;
                break;
            }
        }
        if (targetPosition != -1) {
            folderList.get(targetPosition).getList().add(song);
        } else {
            Folder folder = new Folder();
            ObservableArrayList<Song> list = new ObservableArrayList<>();
            list.add(song);
            folder.setPath(song.getParentPath());
            folder.setName(new File(song.getParentPath()).getName());
            folder.setList(list);
            folderList.add(folder);
        }
    }

    private void addToSinger(Song song) {
        if (song == null || TextUtils.isEmpty(song.getSinger())) {
            return;
        }
        int targetPosition = -1;
        for (int i = 0; i < singerList.size(); i++) {
            if (song.getSinger().equals(singerList.get(i).getName())) {
                targetPosition = i;
                break;
            }
        }
        if (targetPosition != -1) {
            singerList.get(targetPosition).getList().add(song);
        } else {
            Singer singer = new Singer();
            ObservableArrayList<Song> list = new ObservableArrayList<>();
            list.add(song);
            singer.setName(song.getSinger());
            singer.setList(list);
            singerList.add(singer);
        }
    }


    /**
     * doScanandPaser
     * 正在扫描 就通知列表更新，扫描没开始则开启扫描线程
     */
    public synchronized void doScanandPaser() {
        if (ISSCANNING) {
//            mScanUICallback.notifyUpdateData(true);
        } else {
            ISSCANNING = true;
            new ScanThread().start();
        }
    }

    /**
     * @param filename 文件夹路径
     */
    public void scanfile(String filename) {
        if (isNoMediaPath(filename)) {
            KLog.d("zzz", "含有 .nomedia 文件  " + filename);
        } else {
            File dir = new File(filename);
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory() && !file.isHidden()) {
                        mScanUICallback.setScanPath(file.getPath());
                        scanfile(file.getPath());
                    } else {
                        String str = file.getPath();
                        int index = str.lastIndexOf(".");
                        boolean isPattern = false;
                        if (index != -1)
                            for (String regex : REGEXS) {
                                Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
                                Matcher match = pattern.matcher(str.substring(index));

                                if (match.find()) {
                                    isPattern = true;
                                    break;
                                }
                            }
                        if (isPattern) continue;


                        MediaFile.MediaFileType mType = MediaFile.getFileType(file.getPath());

                        if (mType != null && MediaFile.isAudioFileType(mType.fileType) && file.exists()) {
                            mScanUICallback.setScanPath(file.getPath());

                            Song track = parseTrack(file.getPath());
                            songList.add(track);
                            String parentPath = track.getParentPath();

                            musicCount++;
                            mScanUICallback.setScanMusicNum(musicCount);


                            // 文件夹
                            addToFolder(track);

                            // 歌手
                            addToSinger(track);

                            // 专辑
                            addToAlbum(track);

                        }
                    }
                }
            }
        }
    }


    /**
     * ScanThread 扫描线程
     * UI 更新
     * 获取挂载上的路径
     * 遍历目录
     * 结束前删除数据库里不存在文件的数据
     */
    public class ScanThread extends Thread {
        @Override
        public void run() {
            songList.clear();
            folderList.clear();
            singerList.clear();
            albumList.clear();

            ISSCANNING = true;
            musicCount = 0;
            USBREMOVE = false;
            mScanUICallback.setScanMusicNum(musicCount);
            mScanUICallback.setScanState(ISSCANNING);
//            checkSQLpath();

            FlyStorageManager.init(mContext);
            int devicesnum = 0;
            list = FlyStorageManager.getMountedVolumePaths();
            startTimer();
            devicesnum = list.size();
            for (String path : list) {
                scanfile(path);
//                Flog.d("zzz", "list path " + path);
            }
            if (FlyStorageManager.getMountedVolumePaths().size() < devicesnum) {
                USBREMOVE = true;
            }
            ISSCANNING = false;
            mScanUICallback.setScanState(ISSCANNING);
            mScanUICallback.notifyUpdateData(!USBREMOVE);
            mUpdateTimer.cancel();
            mUpateTimerRunning = false;
            mScanUICallback.setScanResult(songList, folderList, singerList, albumList);
        }

    }

//    /**
//     * 检查数据库的path，在存储设备是否存在，不存在则删除此数据
//     */
//    private void checkSQLpath() {
//        ArrayList<FileInfo> fileinfos = DataModule.getInstance().getAllMusicFileList();
//        if (fileinfos != null) {
//            for (FileInfo fileinfo : fileinfos) {
//                File file = new File(fileinfo.getFilePath());
//                if (!file.exists()) {
//                    DataModule.getInstance().deleteNoExistMusic(fileinfo.getFilePath());
//                }
//            }
//        }
//    }

    private void startTimer() {
        if (!mUpateTimerRunning) {
            mUpdateTimer.start();
            mUpateTimerRunning = true;
        }
    }


    CountDownTimer mUpdateTimer = new CountDownTimer(20000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onFinish() {
            // TODO Auto-generated method stub
            if (ISSCANNING) {
                mScanUICallback.notifyUpdateData(!USBREMOVE);
            }
        }
    };


    private void scanbegin() {

    }

    private void scandoing() {

    }

    private void scanend() {

    }

    public Song parseTrack(String path) {
        Song track = null;
        if (!TextUtils.isEmpty(path)) {
            track = parseTrackInfoByReceiver(path);
//            if (track != null && track.getDuration() > 29) {
//			Flog.d("zzz","track"+track.toString());
//            }
        }
        return track;
    }


    // Modified by ZHENGWEIJIA, 2014-05-12
    public static Song parseTrackInfoByReceiver(String path) {
        Song track = null;
        MediaMetadataRetriever retriever = null;
        try {
            retriever = new MediaMetadataRetriever();
            if (retriever != null) {
                retriever.setDataSource(path);
                track = new Song();
                String title = "";
                String artistName = "";
                String albumName = "";
                artistName = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                albumName = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
                title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                if (!TextUtils.isEmpty(title))
                    title = new String(title.getBytes(getEncoding(title)), getOtherEncode(getEncoding(title)));
                if (!TextUtils.isEmpty(artistName))
                    artistName = new String(artistName.getBytes(getEncoding(artistName)), getOtherEncode(getEncoding(artistName)));
                if (!TextUtils.isEmpty(albumName))
                    albumName = new String(albumName.getBytes(getEncoding(albumName)), getOtherEncode(getEncoding(albumName)));
                //	Flog.d("Helper", "parseTrackInfoByReceiver--encoding-- title = " + title);
                title = TextUtils.isEmpty(title) ? StringUtils.getPathName(path) : title;
                artistName = TextUtils.isEmpty(artistName) ? Constants.UNKNOWN : artistName;
                albumName = TextUtils.isEmpty(albumName) ? Constants.UNKNOWN : albumName;

                track.setName(filterStringOfRK(title));
                track.setSinger(filterStringOfRK(artistName.trim()));
//                track.setTrackName(filterStringOfRK(title));
//                track.setArtistName(filterStringOfRK(artistName.trim()));
                track.setAlbumName(filterStringOfRK(albumName.trim()));

                String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                if (TextUtils.isEmpty(duration)) {
                    track.setDuration(0);
                } else {
                    track.setDuration(Integer.parseInt(duration)); //   /1000
                }
//                track.setBitrate(0);
                track.setPath(path);
//                track.setFilePath(path);
                track.setParentPath(path.substring(0, path.lastIndexOf("/")));
                track.setExtName(path.substring(path.lastIndexOf(".") + 1, path.length()));
                //	track.setIsDeleted(0);
                ///track.setQuality(1);
                track.setSize(0);
//                if (!TextUtils.isEmpty(title)) {
//                    String pingyinNameString = filterString(PinyinUtils.getPinyinName(title))
//                            .toLowerCase(Locale.getDefault());
//                    String simplePingyinString = filterString(PinyinUtils.getSimplePinyinName(title))
//                            .toLowerCase(Locale.getDefault());
//
//                    track.setPinyinName(pingyinNameString.equals("") ? "|" : pingyinNameString);
//                    track.setPinyinName_simple(simplePingyinString.equals("") ? "|"
//                            : simplePingyinString);
//                }
//                if (!TextUtils.isEmpty(artistName)) {
//                    String artistPinyinName = filterString(
//                            PinyinUtils.getSimplePinyinName(
//                            artistName).toLowerCase(Locale.getDefault()));
//
//                    track.setPinyinName_artist(artistPinyinName.equals("") ? "|" : artistPinyinName);
//                }
//                if (!TextUtils.isEmpty(albumName)) {
//                    String albumPinyinName = filterString(PinyinUtils.getSimplePinyinName(
//                            albumName).toLowerCase(Locale.getDefault()));
//
//                    track.setPinyinName_album(albumPinyinName.equals("") ? "|" : albumPinyinName);
//                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            track = null;
        } finally {
            if (retriever != null) {
                retriever.release();
                retriever = null;
            }
        }

        return track;
    }

    /**
     * 接口IScanUICallback
     * 用来更新扫描界面和通知
     */
    public interface IScanUICallback {
        public void setScanState(Boolean isScanning);

        public void setScanMusicNum(int hasScan);

        public void setScanPath(String path);

        public void notifyUpdateData(Boolean updateplaylist);

        public void setScanResult(List<Song> list, List<Folder> folderList, List<Singer> singerList, List<Album> albumList);
    }

    /**
     * ScanThread_Factory 扫描线程
     * UI 更新
     * 获取挂载上的路径,只扫描U盘文件
     * 遍历目录
     * 结束前删除数据库里不存在文件的数据
     */
    public class ScanThread_Factory extends Thread {
        @Override
        public void run() {
            ISSCANNING = true;
            musicCount = 0;

            mScanUICallback.setScanMusicNum(musicCount);
            mScanUICallback.setScanState(ISSCANNING);
//            checkSQLpath();

            FlyStorageManager.init(mContext);
            list = FlyStorageManager.getMountedVolumePaths();
            for (String path : list) {
                if (path.endsWith("udisk") || path.contains("udisk")) {
//		    	if (path.equals("/storage/emulated/0")) {
                    Log.d(TAG, "ScanThread_Factory list path " + path);
                    scanfile(path);
                }
            }

            ISSCANNING = false;
            mScanUICallback.setScanState(ISSCANNING);
            mScanUICallback.notifyUpdateData(true);
        }
    }

    /**
     * path判断是否有 /.nomedia
     *
     * @param path 文件夹目录
     * @return
     */
    public static boolean isNoMediaPath(String path) {
        if (path == null) return false;
        // return true if file or any parent directory has name starting with a dot
        if (path.indexOf("/.") >= 0) return true;
        File file = new File(path + "/.nomedia");
        if (file.exists()) {
            return true;
        }
        return false;
    }

    private static String filterStringOfRK(String str) {
        if (str == null) {
            return str;
        }
        String regEx = "rkutf8";
        if (str.equals(regEx)) {
            return Constants.UNKNOWN;
        }
        if (str.startsWith(regEx)) {
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(str);
            return m.replaceAll("").trim();
        }
        return str;
    }

    public static String getEncoding(String str) {
        String[] codes = new String[]{"GB2312", "ISO-8859-1", "UTF-8", "GBK", "ISO-8859-8", "ASCII"
                , "Shift-JIS", "EUC-JP", "ISO-2022-JP"};
        String encode = "GBK";
        try {
            for (String code : codes) {
                if (str.equals(new String(str.getBytes(code), code))) {
                    encode = code;
                    return encode;
                }
            }
        } catch (Exception exception) {
        }
        return encode;
    }

    public static String getOtherEncode(String encode) {
        if (isChina()) {
            if (encode.equals("GB2312") || encode.equals("ISO-8859-1") || encode.equals("GBK"))
                return "GBK";
        } else {
            if (encode.equals("ISO-8859-1")) {
                return "ISO-8859-8";
            } else if (encode.equals("GB2312") || encode.equals("GBK")) {
                return "GBK";
            }
        }
        return encode;
    }

    /**
     * 判断当前国家
     *
     * @return
     */
    private static boolean isChina() {
        String contry = Locale.getDefault().getCountry();
        if (!TextUtils.isEmpty(contry) && (!contry.equalsIgnoreCase("CN"))
                && (!contry.equalsIgnoreCase("HK"))
                && (!contry.equalsIgnoreCase("TW"))) {
            return false;
        }
        return true;
    }

    public static String filterString(String srcString) {
        String regEx = "[^a-z|A-Z|0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(srcString);
        return m.replaceAll("").trim();
    }
}
