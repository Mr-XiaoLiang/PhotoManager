package com.lollipop.pm.data

enum class PhotoSuffix(val key: String) {
    /**
     * 有损压缩图片
     */
    JPG("jpg"),

    /**
     * 有损压缩图片
     */
    JPEG("jpeg"),

    /**
     * 体积小
     */
    WEBP("webp"),

    /**
     * 无损压缩图片
     */
    PNG("png"),

    /**
     * 高效图像文件格式
     */
    HEIC("heic"),

    /**
     * 高效率图像文件格式
     */
    HEIF("heif"),

    /**
     * 动图
     */
    GIF("gif"),

    /**
     * 位图
     */
    BMP("bmp"),

    /**
     * 灵活位图格式
     */
    TIF("tif"),

    /**
     * 灵活位图格式
     */
    TIFF("tiff"),

    /**
     * 矢量图像格式
     */
    SVG("svg"),

    /**
     * 无损图片格式
     */
    RAW("raw"),

    /**
     * 索尼相机无损图片格式
     */
    ARW("arw"),

    /**
     * 佳能相机无损图片格式
     */
    CR2("cr2"),

    /**
     * 佳能相机无损图片格式
     */
    CR3("cr3"),

    /**
     * 尼康相机无损图片格式
     */
    NEF("nef"),

    /**
     * 富士相机无损图片格式
     */
    RAF("raf"),

    /**
     * 松下相机无损图片格式
     */
    RW2("rw2"),

    /**
     * 宾得相机无损图片格式
     */
    PEF("pef"),

    /**
     * 徕卡相机无损图片格式
     */
    DNG("dng"),

    /**
     * 哈苏相机无损图片格式
     */
    FR3("3fr");

    companion object {
        fun findByKey(key: String): PhotoSuffix? {
            return entries.firstOrNull { it.key == key }
        }
    }

}