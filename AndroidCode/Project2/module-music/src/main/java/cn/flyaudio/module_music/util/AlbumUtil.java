package cn.flyaudio.module_music.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.images.Artwork;

import java.io.File;

public class AlbumUtil {

    /**
     * 耗时操作
     *
     * @param path
     * @return
     */
    public static Bitmap getAlbumBitmap(String path) {
        Bitmap bm = null;
        AudioFile file = null;
        try {
            file = AudioFileIO.read(new File(path));
            if (file != null && file.getTag() != null) {
                Artwork artWork = file.getTag().getFirstArtwork();
                if (artWork != null) {
                    byte[] img_bytes = artWork.getBinaryData();
                    if (img_bytes != null) {
                        bm = BitmapFactory.decodeByteArray(img_bytes, 0, img_bytes.length);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }
}
