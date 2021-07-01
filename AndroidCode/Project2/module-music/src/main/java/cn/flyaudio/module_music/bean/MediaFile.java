package cn.flyaudio.module_music.bean;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


//import android.media.DecoderCapabilities.AudioDecoder;
//import android.media.DecoderCapabilities.VideoDecoder;
//import android.os.SystemProperties;
//import com.mediatek.media.MediaFactory;
//import com.mediatek.media.mediascanner.MediaFileEx;

import java.util.HashMap;
import java.util.Locale;

public class MediaFile {
    //    private static boolean ATC_AOSP_ENHANCEMENT = SystemProperties.getBoolean("ro.atc.aosp_enhancement", false);
    public static final int FILE_TYPE_MP3 = 101;
    public static final int FILE_TYPE_M4A = 102;
    public static final int FILE_TYPE_WAV = 103;
    public static final int FILE_TYPE_AMR = 104;
    public static final int FILE_TYPE_AWB = 105;
    public static final int FILE_TYPE_WMA = 106;
    public static final int FILE_TYPE_OGG = 107;
    public static final int FILE_TYPE_AAC = 108;
    public static final int FILE_TYPE_MKA = 109;
    public static final int FILE_TYPE_FLAC = 110;
    public static final int FILE_TYPE_APE = 111;
    public static final int FILE_TYPE_CAF = 112;
    public static final int FILE_TYPE_AC3 = 114;
    public static final int FILE_TYPE_MP1 = 115;
    public static final int FILE_TYPE_DTS = 116;
    public static final int FILE_TYPE_3GA = 193;
    public static final int FILE_TYPE_QUICKTIME_AUDIO = 194;
    public static final int FILE_TYPE_FLA = 196;
    public static final int FILE_TYPE_RA = 198;
    public static final int FILE_TYPE_3GPP3 = 199;
    private static final int FIRST_AUDIO_FILE_TYPE = 101;
    private static final int LAST_AUDIO_FILE_TYPE = 199;
    public static final int FILE_TYPE_MID = 201;
    public static final int FILE_TYPE_SMF = 202;
    public static final int FILE_TYPE_IMY = 203;
    private static final int FIRST_MIDI_FILE_TYPE = 201;
    private static final int LAST_MIDI_FILE_TYPE = 203;
    public static final int FILE_TYPE_MP4 = 301;
    public static final int FILE_TYPE_M4V = 302;
    public static final int FILE_TYPE_3GPP = 303;
    public static final int FILE_TYPE_3GPP2 = 304;
    public static final int FILE_TYPE_WMV = 305;
    public static final int FILE_TYPE_ASF = 306;
    public static final int FILE_TYPE_MKV = 307;
    public static final int FILE_TYPE_MP2TS = 308;
    public static final int FILE_TYPE_AVI = 309;
    public static final int FILE_TYPE_WEBM = 310;
    public static final int FILE_TYPE_MPG = 311;
    public static final int FILE_TYPE_DIVX = 312;
    public static final int FILE_TYPE_MP2PS = 393;
    public static final int FILE_TYPE_OGM = 394;
    public static final int FILE_TYPE_RV = 395;
    public static final int FILE_TYPE_RMVB = 396;
    public static final int FILE_TYPE_QUICKTIME_VIDEO = 397;
    public static final int FILE_TYPE_FLV = 398;
    public static final int FILE_TYPE_RM = 399;
    private static final int FIRST_VIDEO_FILE_TYPE = 301;
    private static final int LAST_VIDEO_FILE_TYPE = 399;
    public static final int FILE_TYPE_QT = 200;
    private static final int FIRST_VIDEO_FILE_TYPE2 = 200;
    private static final int LAST_VIDEO_FILE_TYPE2 = 200;
    public static final int FILE_TYPE_JPEG = 401;
    public static final int FILE_TYPE_GIF = 402;
    public static final int FILE_TYPE_PNG = 403;
    public static final int FILE_TYPE_BMP = 404;
    public static final int FILE_TYPE_WBMP = 405;
    public static final int FILE_TYPE_WEBP = 406;
    public static final int FILE_TYPE_HEIF = 407;
    public static final int FILE_TYPE_MPO = 499;
    private static final int FIRST_IMAGE_FILE_TYPE = 401;
    private static final int LAST_IMAGE_FILE_TYPE = 499;
    public static final int FILE_TYPE_DNG = 800;
    public static final int FILE_TYPE_CR2 = 801;
    public static final int FILE_TYPE_NEF = 802;
    public static final int FILE_TYPE_NRW = 803;
    public static final int FILE_TYPE_ARW = 804;
    public static final int FILE_TYPE_RW2 = 805;
    public static final int FILE_TYPE_ORF = 806;
    public static final int FILE_TYPE_RAF = 807;
    public static final int FILE_TYPE_PEF = 808;
    public static final int FILE_TYPE_SRW = 809;
    private static final int FIRST_RAW_IMAGE_FILE_TYPE = 800;
    private static final int LAST_RAW_IMAGE_FILE_TYPE = 809;
    public static final int FILE_TYPE_M3U = 501;
    public static final int FILE_TYPE_PLS = 502;
    public static final int FILE_TYPE_WPL = 503;
    public static final int FILE_TYPE_HTTPLIVE = 504;
    private static final int FIRST_PLAYLIST_FILE_TYPE = 501;
    private static final int LAST_PLAYLIST_FILE_TYPE = 504;
    public static final int FILE_TYPE_FL = 601;
    private static final int FIRST_DRM_FILE_TYPE = 601;
    private static final int LAST_DRM_FILE_TYPE = 601;
    public static final int FILE_TYPE_TEXT = 700;
    public static final int FILE_TYPE_HTML = 701;
    public static final int FILE_TYPE_PDF = 702;
    public static final int FILE_TYPE_XML = 703;
    public static final int FILE_TYPE_MS_WORD = 704;
    public static final int FILE_TYPE_MS_EXCEL = 705;
    public static final int FILE_TYPE_MS_POWERPOINT = 706;
    public static final int FILE_TYPE_ZIP = 707;
    //    private static MediaFileEx sMediaFileEx = MediaFactory.getInstance().getMediaFileEx();
    private static final HashMap<String, MediaFile.MediaFileType> sFileTypeMap = new HashMap();
    private static final HashMap<String, Integer> sMimeTypeMap = new HashMap();
    private static final HashMap<String, Integer> sFileTypeToFormatMap = new HashMap();
    private static final HashMap<String, Integer> sMimeTypeToFormatMap = new HashMap();
    private static final HashMap<Integer, String> sFormatToMimeTypeMap = new HashMap();

    public MediaFile() {
    }

    static void addFileType(String extension, int fileType, String mimeType) {
        sFileTypeMap.put(extension, new MediaFile.MediaFileType(fileType, mimeType));
        sMimeTypeMap.put(mimeType, fileType);
    }

    private static void addFileType(String extension, int fileType, String mimeType, int mtpFormatCode, boolean primaryType) {
        addFileType(extension, fileType, mimeType);
        sFileTypeToFormatMap.put(extension, mtpFormatCode);
        sMimeTypeToFormatMap.put(mimeType, mtpFormatCode);
        if (primaryType && !sFormatToMimeTypeMap.containsKey(mtpFormatCode)) {
            sFormatToMimeTypeMap.put(mtpFormatCode, mimeType);
        }

    }

//    private static boolean isWMAEnabled() {
//        if (!SystemProperties.getBoolean("ro.vendor.mtk_wmv_playback_support", false)) {
//            return false;
//        } else {
//            List<AudioDecoder> decoders = DecoderCapabilities.getAudioDecoders();
//            int count = decoders.size();
//
//            for (int i = 0; i < count; ++i) {
//                AudioDecoder decoder = (AudioDecoder) decoders.get(i);
//                if (decoder == AudioDecoder.AUDIO_DECODER_WMA) {
//                    return true;
//                }
//            }
//
//            return false;
//        }
//    }

//    private static boolean isWMVEnabled() {
//        if (!SystemProperties.getBoolean("ro.vendor.mtk_wmv_playback_support", false)) {
//            return false;
//        } else {
//            List<VideoDecoder> decoders = DecoderCapabilities.getVideoDecoders();
//            int count = decoders.size();
//
//            for (int i = 0; i < count; ++i) {
//                VideoDecoder decoder = (VideoDecoder) decoders.get(i);
//                if (decoder == VideoDecoder.VIDEO_DECODER_WMV) {
//                    return true;
//                }
//            }
//
//            return false;
//        }
//    }

    public static boolean isAudioFileType(int fileType) {
        return fileType >= 101 && fileType <= 199 || fileType >= 201 && fileType <= 203;
    }

    public static boolean isVideoFileType(int fileType) {
        return fileType >= 301 && fileType <= 399 || fileType >= 200 && fileType <= 200;
    }

    public static boolean isImageFileType(int fileType) {
        return fileType >= 401 && fileType <= 499 || fileType >= 800 && fileType <= 809;
    }

    public static boolean isRawImageFileType(int fileType) {
        return fileType >= 800 && fileType <= 809;
    }

    public static boolean isPlayListFileType(int fileType) {
        return fileType >= 501 && fileType <= 504;
    }

    public static boolean isDrmFileType(int fileType) {
        return fileType >= 601 && fileType <= 601;
    }

    public static MediaFile.MediaFileType getFileType(String path) {
        int lastDot = path.lastIndexOf(46);
        return lastDot < 0 ? null : (MediaFile.MediaFileType) sFileTypeMap.get(path.substring(lastDot + 1).toUpperCase(Locale.ROOT));
    }

    public static boolean isMimeTypeMedia(String mimeType) {
        int fileType = getFileTypeForMimeType(mimeType);
        return isAudioFileType(fileType) || isVideoFileType(fileType) || isImageFileType(fileType) || isPlayListFileType(fileType);
    }

    public static String getFileTitle(String path) {
        int lastSlash = path.lastIndexOf(47);
        if (lastSlash >= 0) {
            ++lastSlash;
            if (lastSlash < path.length()) {
                path = path.substring(lastSlash);
            }
        }

        int lastDot = path.lastIndexOf(46);
        if (lastDot > 0) {
            path = path.substring(0, lastDot);
        }

        return path;
    }

    public static int getFileTypeForMimeType(String mimeType) {
        Integer value = (Integer) sMimeTypeMap.get(mimeType);
        return value == null ? 0 : value;
    }

//    public static String getMimeTypeForFile(String path) {
//        MediaFile.MediaFileType mediaFileType = getFileType(path);
//        return mediaFileType == null ? null : mediaFileType.mimeType;
//    }

    public static int getFormatCode(String fileName, String mimeType) {
        if (mimeType != null) {
            Integer value = (Integer) sMimeTypeToFormatMap.get(mimeType);
            if (value != null) {
                return value;
            }
        }

        int lastDot = fileName.lastIndexOf(46);
        if (lastDot > 0) {
            String extension = fileName.substring(lastDot + 1).toUpperCase(Locale.ROOT);
            Integer value = (Integer) sFileTypeToFormatMap.get(extension);
            if (value != null) {
                return value;
            }
        }

        return 12288;
    }

    public static String getMimeTypeForFormatCode(int formatCode) {
        return (String) sFormatToMimeTypeMap.get(formatCode);
    }

    static {
//        sMediaFileEx.addMoreAudioFileType();
        addFileType("MP2", 101, "audio/mpeg");
        addFileType("MP3", 101, "audio/mpeg", 12297, true);
        addFileType("MPGA", 101, "audio/mpeg", 12297, false);
        addFileType("M4A", 102, "audio/mp4", 12299, false);
        addFileType("WAV", 103, "audio/x-wav", 12296, true);
        addFileType("AMR", 104, "audio/amr");
        addFileType("AWB", 105, "audio/amr-wb");
//        if (isWMAEnabled()) {
//            addFileType("WMA", 106, "audio/x-ms-wma", 47361, true);
//        }

        addFileType("OGG", 107, "audio/ogg", 47362, false);
        addFileType("OGG", 107, "application/ogg", 47362, true);
        addFileType("OGA", 107, "application/ogg", 47362, false);
        addFileType("AAC", 108, "audio/aac", 47363, true);
        addFileType("AAC", 108, "audio/aac-adts", 47363, false);
        addFileType("MKA", 109, "audio/x-matroska");
        addFileType("MID", 201, "audio/midi");
        addFileType("MIDI", 201, "audio/midi");
        addFileType("XMF", 201, "audio/midi");
        addFileType("RTTTL", 201, "audio/midi");
        addFileType("SMF", 202, "audio/sp-midi");
        addFileType("IMY", 203, "audio/imelody");
        addFileType("RTX", 201, "audio/midi");
        addFileType("OTA", 201, "audio/midi");
        addFileType("MXMF", 201, "audio/midi");
//        sMediaFileEx.addMoreVideoFileType();
        addFileType("MPEG", 301, "video/mpeg", 12299, true);
        addFileType("MPG", 301, "video/mpeg", 12299, false);
        addFileType("MP4", 301, "video/mp4", 12299, false);
        addFileType("M4V", 302, "video/mp4", 12299, false);
        addFileType("MOV", 200, "video/quicktime", 12299, false);
        addFileType("3GP", 303, "video/3gpp", 47492, true);
        addFileType("3GPP", 303, "video/3gpp", 47492, false);
        addFileType("3G2", 304, "video/3gpp2", 47492, false);
        addFileType("3GPP2", 304, "video/3gpp2", 47492, false);
        addFileType("MKV", 307, "video/x-matroska");
        addFileType("WEBM", 310, "video/webm");
        addFileType("TS", 308, "video/mp2ts");
        addFileType("AVI", 309, "video/avi");
//        if (isWMVEnabled()) {
//            addFileType("WMV", 305, "video/x-ms-wmv", 47489, true);
//            addFileType("ASF", 306, "video/x-ms-asf");
//        }

//        sMediaFileEx.addMoreImageFileType();
        addFileType("JPG", 401, "image/jpeg", 14337, true);
        addFileType("JPEG", 401, "image/jpeg", 14337, false);
        addFileType("GIF", 402, "image/gif", 14343, true);
        addFileType("PNG", 403, "image/png", 14347, true);
        addFileType("BMP", 404, "image/x-ms-bmp", 14340, true);
        addFileType("WBMP", 405, "image/vnd.wap.wbmp", 14336, false);
        addFileType("WEBP", 406, "image/webp", 14336, false);
        addFileType("HEIC", 407, "image/heif", 14354, true);
        addFileType("HEIF", 407, "image/heif", 14354, false);
        addFileType("DNG", 800, "image/x-adobe-dng", 14353, true);
        addFileType("CR2", 801, "image/x-canon-cr2", 14349, false);
        addFileType("NEF", 802, "image/x-nikon-nef", 14338, false);
        addFileType("NRW", 803, "image/x-nikon-nrw", 14349, false);
        addFileType("ARW", 804, "image/x-sony-arw", 14349, false);
        addFileType("RW2", 805, "image/x-panasonic-rw2", 14349, false);
        addFileType("ORF", 806, "image/x-olympus-orf", 14349, false);
        addFileType("RAF", 807, "image/x-fuji-raf", 14336, false);
        addFileType("PEF", 808, "image/x-pentax-pef", 14349, false);
        addFileType("SRW", 809, "image/x-samsung-srw", 14349, false);
        addFileType("M3U", 501, "audio/x-mpegurl", 47633, true);
        addFileType("M3U", 501, "application/x-mpegurl", 47633, false);
        addFileType("PLS", 502, "audio/x-scpls", 47636, true);
        addFileType("WPL", 503, "application/vnd.ms-wpl", 47632, true);
        addFileType("M3U8", 504, "application/vnd.apple.mpegurl");
        addFileType("M3U8", 504, "audio/mpegurl");
        addFileType("M3U8", 504, "audio/x-mpegurl");
        addFileType("FL", 601, "application/x-android-drm-fl");
        addFileType("TXT", 700, "text/plain", 12292, true);
        addFileType("HTM", 701, "text/html", 12293, true);
        addFileType("HTML", 701, "text/html", 12293, false);
        addFileType("PDF", 702, "application/pdf");
        addFileType("DOC", 704, "application/msword", 47747, true);
        addFileType("XLS", 705, "application/vnd.ms-excel", 47749, true);
        addFileType("PPT", 706, "application/vnd.ms-powerpoint", 47750, true);
        addFileType("FLAC", 110, "audio/flac", 47366, true);
        addFileType("ZIP", 707, "application/zip");
        addFileType("MPG", 393, "video/mp2p");
        addFileType("MPEG", 393, "video/mp2p");
//        sMediaFileEx.addMoreOtherFileType();
//        if (ATC_AOSP_ENHANCEMENT) {
        addFileType("AC3", 114, "audio/ac3");
        addFileType("EC3", 114, "audio/ac3");
        addFileType("RA", 198, "audio/ra");
        addFileType("RAM", 198, "audio/ra");
        addFileType("DTS", 116, "audio/dts");
        addFileType("F4A", 196, "audio/x-flv");
        addFileType("F4B", 196, "audio/x-flv");
        addFileType("PCM", 103, "audio/x-wav");
        addFileType("MP1", 115, "audio/mpeg");
        addFileType("MP4V", 301, "video/mpeg");
        addFileType("M1V", 311, "video/mpeg");
        addFileType("M2V", 311, "video/mpeg");
        addFileType("F4P", 398, "video/x-flv");
        addFileType("HLV", 398, "video/x-flv");
        addFileType("XVID", 312, "video/divx");
        addFileType("DIVX", 312, "video/divx");
        addFileType("RM", 396, "video/rmvb");
        addFileType("RMVB", 396, "video/rmvb");
        addFileType("M2T", 308, "video/mp2ts");
        addFileType("TP", 308, "video/mp2ts");
        addFileType("TRP", 308, "video/mp2ts");
        addFileType("VOB", 393, "video/mp2p");
        addFileType("DAT", 393, "video/mp2p");
    }

    public static class MediaFileType {
        public final int fileType;
        public final String mimeType;

        MediaFileType(int fileType, String mimeType) {
            this.fileType = fileType;
            this.mimeType = mimeType;
        }
    }
}



