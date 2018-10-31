package com.example.sprite.custommusic.page1.constance;

public class UrlConstance {

    private final static String BASE_URL = "http://tingapi.ting.baidu.com/v1/restserver/";
    private final static String BILL_LIST_METHOD = "baidu.ting.billboard.billList";
    private final static String DOWNLOAD_METHOD = "baidu.ting.song.downWeb";
    private final static String LRC_METHOD = "baidu.ting.song.lry";

    private static String setBaseUrl() {
        return BASE_URL;
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }

    /**
     * @param type   排名类型
     * @param size   获取歌曲个数
     * @param offset 偏移量
     * @return 返回链接
     */
    public static String getBillListUrl(int type, int size, int offset) {
        return setBaseUrl() + BILL_LIST_METHOD + "&type=" + type + "&size=" + size + "&offset=" + offset;
    }

    /**
     * @param songId 歌曲id
     * @return 返回链接
     */
    public static String getDownloadMusicUrl(long songId) {
        return setBaseUrl() + DOWNLOAD_METHOD + "&songid=" + songId;
    }

    /**
     * @param songId 歌曲id
     * @return 返回链接
     */
    public static String getLrcUrl(long songId) {
        return setBaseUrl() + LRC_METHOD + "&songid=" + songId;
    }
}
