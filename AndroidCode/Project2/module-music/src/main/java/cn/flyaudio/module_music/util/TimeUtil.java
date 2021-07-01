package cn.flyaudio.module_music.util;

/**
 * Describe:
 * <p>扫描本地音乐文件</p>
 *
 * @author zhouhuan
 * @Date 2020/11/20
 */
public class TimeUtil {
//    /**
//     * 扫描系统里面的音频文件，返回一个list集合
//     */
//    public static List<Song> getMusicData(Context context) {
//        List<Song> list = new ArrayList<>();
//        String[] selectionArgs = new String[]{"%Music%"};
//        String selection = MediaStore.Audio.Media.DATA + " like ? ";
//        // 媒体库查询语句（写一个工具类MusicUtils）
//        Cursor cursor = context.getContentResolver().query(
//                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, selection,
//                selectionArgs, MediaStore.Audio.AudioColumns.IS_MUSIC
//        );
//        if (cursor != null) {
//            while (cursor.moveToNext()) {
//                Song song = new Song();
//                song.setId(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));
//                song.setName(StringUtils.substringBefore(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)), "."));
//                song.setSinger(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
//                song.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
//                song.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
//                song.setImgPath(loadingCover(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))));
//                song.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));
//                if (song.getSize() > 1000 * 800) {
//                    // 注释部分是切割标题，分离出歌曲名和歌手 （本地媒体库读取的歌曲信息不规范）
//                    String name = song.getName();
//                    if (name != null && name.contains("-")) {
//                        String[] str = name.split("-");
//                        song.setSinger(str[0]);
//                        song.setName(str[1]);
//                    }
//                    list.add(song);
//                }
//            }
//            // 释放资源
//            cursor.close();
//        }
//        return list;
//    }
//    private static byte[] loadingCover(String mediaUri) {
//        MediaMetadataRetriever mediaMetadataRetriever=new MediaMetadataRetriever();
//        mediaMetadataRetriever.setDataSource(mediaUri);
//        byte[] picture = mediaMetadataRetriever.getEmbeddedPicture();
//        return picture;
//    }

    /**
     * 定义一个方法用来格式化获取到的时间
     */
    public static String formatTime(int time) {
        if (time / 1000 % 60 < 10) {
            return (time / 1000 / 60) + ":0" + time / 1000 % 60;
        } else {
            return (time / 1000 / 60) + ":" + time / 1000 % 60;
        }
    }
}
