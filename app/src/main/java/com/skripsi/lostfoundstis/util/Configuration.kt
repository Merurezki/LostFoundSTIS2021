package com.skripsi.lostfoundstis.util

@Suppress("PropertyName")
class Configuration {
    // Dibawah ini merupakan pengalamatan dimana lokasi skrip CRUD PHP disimpan
    val URL_MAIN         = "http://192.168.100.52/LostFound/"
    val URL_USER_LOGIN   = "http://192.168.100.52/LostFound/Login.php"

    val URL_GET_ALL_CARI = "http://192.168.100.52/LostFound/TampilSemuaPencarian.php"
    val URL_GET_ONE_CARI = "http://192.168.100.52/LostFound/DetailPencarian.php?id_cari="
    val URL_ADD_CARI     = "http://192.168.100.52/LostFound/TambahPencarian.php"
    val URL_UPDATE_CARI  = "http://192.168.100.52/LostFound/UpdatePencarian.php"
    val URL_UPLOAD_CARI  = "http://192.168.100.52/LostFound/UploadFotoPencarian.php"
    val URL_DELETE_CARI  = "http://192.168.100.52/LostFound/HapusPencarian.php?id_cari="
    val URL_FINISH_CARI  = "http://192.168.100.52/LostFound/SelesaiPencarian.php?id_cari="

    val URL_GET_ALL_TEMU = "http://192.168.100.52/LostFound/TampilSemuaPenemuan.php"
    val URL_GET_ONE_TEMU = "http://192.168.100.52/LostFound/DetailPenemuan.php?id_temu="

    val URL_IMG_LOC      = "http://192.168.100.52/LostFound/img/"
    val URL_IMG_LOC_CARI = "http://192.168.100.52/LostFound/img/foto_cari/"
    val URL_IMG_LOC_TEMU = "http://192.168.100.52/LostFound/img/foto_temu/"

    val URL_GET_ALL_SAYA = "http://192.168.100.52/LostFound/MyPencarian.php?id_pencari="

    val URL_GET_PROFIL   = "http://192.168.100.52/LostFound/MyProfil.php?id_user="
    val URL_UPDATE_TLP   = "http://192.168.100.52/LostFound/UpdateTelepon.php?id_user="
    val TAG_PROFIL_JSON  = "result_profil"
    val TAG_NAMA_USER    = "nama_user"
    val TAG_STS_USER     = "sts_user"
    val TAG_JK_USER      = "jk_user"
    val TAG_EMAIL_USER   = "email_user"
    val TAG_PHONE_USER   = "phone_user"

    val URL_GET_JBAR     = "http://192.168.100.52/LostFound/DaftarJenisBarang.php"
    val TAG_JBAR_JSON    = "result_jbar"
    val TAG_ID_JBAR      = "id_jbar"
    val TAG_KEL_BAR      = "kel_bar"
    val TAG_KAT_BAR      = "kat_bar"

    val URL_GET_LOK      = "http://192.168.100.52/LostFound/DaftarLokasiKampus.php"
    val TAG_LOK_JSON     = "result_lok"
    val TAG_ID_LOK       = "id_lok"
    val TAG_GD_LOK       = "gd_lok"
    val TAG_RG_LOK       = "rg_lok"

    // Dibawah ini merupakan kunci untuk pencarian yang akan digunakan untuk mengirim permintaan ke Skrip PHP
    val TAG_CARI_JSON_ARRAY = "result_cari"
    val TAG_CARI_ID      = "id_cari"
    val TAG_CARI_ID_USER = "id_pencari"
    val TAG_CARI_ID_JBAR = "id_jbar_cari"
    val TAG_CARI_JBAR    = "jbar_cari"
    val TAG_CARI_ID_LOK  = "id_lok_cari"
    val TAG_CARI_LOK     = "lok_cari"
    val TAG_CARI_JDL     = "jdl_cari"
    val TAG_CARI_CIRI    = "cir_bar_cari"
    val TAG_CARI_KEL     = "kel_bar_cari"
    val TAG_CARI_KAT     = "kat_bar_cari"
    val TAG_CARI_GD      = "gd_kps_cari"
    val TAG_CARI_RG      = "rg_kps_cari"
    val TAG_CARI_WKT     = "wkt_cari"
    val TAG_CARI_TGL     = "tgl_cari"
    val TAG_CARI_FOTO    = "fot_bar_cari"

    val CARI_ID          = "id_cari"

    // Dibawah ini merupakan kunci untuk penemuan yang akan digunakan untuk mengirim permintaan ke Skrip PHP
    val TAG_TEMU_JSON_ARRAY = "result_temu"
    val TAG_TEMU_ID      = "id_temu"
    val TAG_TEMU_ID_JBAR = "id_jbar_temu"
    val TAG_TEMU_JBAR    = "jbar_temu"
    val TAG_TEMU_ID_LOK  = "id_lok_temu"
    val TAG_TEMU_LOK     = "lok_temu"
    val TAG_TEMU_JDL     = "jdl_temu"
    val TAG_TEMU_CIRI    = "cir_bar_temu"
    val TAG_TEMU_KEL     = "kel_bar_temu"
    val TAG_TEMU_KAT     = "kat_bar_temu"
    val TAG_TEMU_GD      = "gd_kps_temu"
    val TAG_TEMU_RG      = "rg_kps_temu"
    val TAG_TEMU_WKT     = "wkt_temu"
    val TAG_TEMU_TGL     = "tgl_temu"
    val TAG_TEMU_FOTO    = "fot_bar_temu"

    val TEMU_ID          = "id_temu"
}